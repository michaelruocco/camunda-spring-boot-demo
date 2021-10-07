package uk.co.mruoc.demo.bpmn;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.camunda.spin.json.SpinJsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.reset;
import static org.camunda.bpm.extension.mockito.CamundaMockito.verifyJavaDelegateMock;
import static org.camunda.spin.Spin.JSON;

@Deployment(resources = {"bpmns/payment-request.bpmn", "bpmns/payment-auto-approve.dmn"})
class PaymentRequestBpmnTest {

    private static final String PROCESS_DEFINITION_KEY = "payment-requested";
    private static final String RETRIEVE_QUOTE = "retrieveQuote";

    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension.builder()
            .configurationResource("test.camunda.cfg.xml")
            .build();

    private final RuntimeService runtimeService = extension.getProcessEngine().getRuntimeService();

    @BeforeEach
    public void setUp() {
        registerJavaDelegateMock("userApprovalFormExecutionListener");
    }

    @AfterEach
    public void tearDown() {
        reset();
    }

    @ParameterizedTest
    @MethodSource("autoDecisionProductCostAndRiskScore")
    void shouldAcceptOrRejectChargeBasedOnProductIdCostAndRiskScore(String productId, double cost, double riskScore, String delegateName, String endName) {
        registerJavaDelegateMock(delegateName);
        registerJavaDelegateMock(RETRIEVE_QUOTE);
        Map<String, Object> variables = buildVariables(productId, cost, riskScore);

        ProcessInstance process = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);

        assertThat(process).hasPassed(endName).isEnded();
        verifyJavaDelegateMock(RETRIEVE_QUOTE).executed();
        verifyJavaDelegateMock(delegateName).executed();
    }

    @ParameterizedTest
    @MethodSource("userApprovalProductCostAndRiskScore")
    void shouldRequireUserApprovalBasedOnItemNameValueAndRiskScore(String productId, double cost, double riskScore, boolean approved, String delegateName, String endName) {
        registerJavaDelegateMock(RETRIEVE_QUOTE);
        registerJavaDelegateMock(delegateName);
        Map<String, Object> variables = buildVariables(productId, cost, riskScore);

        ProcessInstanceWithVariables process = runtimeService.createProcessInstanceByKey(PROCESS_DEFINITION_KEY)
                .setVariables(variables)
                .executeWithVariablesInReturn();

        assertThat(process).isNotEnded().isWaitingAt("user-approval");

        complete(task(), Map.of("approved", approved));

        assertThat(process).hasPassed(endName).isEnded();
        verifyJavaDelegateMock(RETRIEVE_QUOTE).executed();
        verifyJavaDelegateMock(delegateName).executed();
    }

    private static Stream<Arguments> autoDecisionProductCostAndRiskScore() {
        return Stream.of(
                Arguments.of("any-item", 999.99, 150, "acceptPayment", "payment-accepted"),
                Arguments.of("any-item", 1000, 150, "rejectPayment", "payment-rejected"),
                Arguments.of("abc-123", 1000, 150, "acceptPayment", "payment-accepted"),
                Arguments.of("xyz-789", 1000, 150, "acceptPayment", "payment-accepted")
        );
    }

    private static Stream<Arguments> userApprovalProductCostAndRiskScore() {
        return Stream.of(
                Arguments.of("any-item", 1000, 0, true, "acceptPayment", "payment-accepted"),
                Arguments.of("any-item", 1000, 149.99, false, "rejectPayment", "payment-rejected")
        );
    }

    private static Map<String, Object> buildVariables(String productId, double cost, double riskScore) {
        return withVariables(
                "productId", productId,
                "cost", cost,
                "riskScore", riskScore,
                "riskPayload", toPayload(riskScore)
        );
    }

    private static SpinJsonNode toPayload(double riskScore) {
        return JSON(String.format("{\"data\":{\"score\":%.2f}}", riskScore));
    }

}