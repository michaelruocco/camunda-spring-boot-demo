package uk.co.mruoc.demo.adapter.camunda.bpmn;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
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

@Deployment(resources = {"bpmns/request-payment-approval.bpmn", "bpmns/auto-approve-payment.dmn"})
class PaymentRequestBpmnTest {

    private static final String PROCESS_DEFINITION_KEY = "request-payment-approval";

    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension.builder()
            .configurationResource("test.camunda.cfg.xml")
            .build();

    private final RuntimeService runtimeService = extension.getProcessEngine().getRuntimeService();

    @AfterEach
    public void tearDown() {
        reset();
    }

    @ParameterizedTest
    @MethodSource("autoDecisionProductCostAndRiskScore")
    void shouldAcceptOrRejectChargeBasedOnProductIdCostAndRiskScore(String productId, double cost, double riskScore, String delegateName, String endName) {
        registerJavaDelegateMock(delegateName);
        Map<String, Object> variables = buildVariables(productId, cost, riskScore);

        ProcessInstance process = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);

        assertThat(process).hasPassed(endName).isEnded();
        verifyJavaDelegateMock(delegateName).executed();
    }

    @ParameterizedTest
    @MethodSource("userApprovalProductCostAndRiskScore")
    void shouldRequireUserApprovalBasedOnItemNameValueAndRiskScore(String productId, double cost, double riskScore, boolean approved, String delegateName, String endName) {
        registerJavaDelegateMock(delegateName);
        Map<String, Object> variables = buildVariables(productId, cost, riskScore);

        ProcessInstanceWithVariables process = runtimeService.createProcessInstanceByKey(PROCESS_DEFINITION_KEY)
                .setVariables(variables)
                .executeWithVariablesInReturn();

        assertThat(process).isNotEnded().isWaitingAt("user-approval");

        complete(task(), isApproved(approved));

        assertThat(process).hasPassed(endName).isEnded();
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
                "riskScore", riskScore
        );
    }

    private static Map<String, Object> isApproved(boolean approved) {
        return withVariables("approved", approved);
    }

}
