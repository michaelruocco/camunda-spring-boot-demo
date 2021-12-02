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
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.execute;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.job;
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
    void shouldMakeAutoDecisionBasedOnProductIdCostAndRiskScore(String productId, double cost, double riskScore, String delegateName, String[] activityIds) {
        registerJavaDelegateMock(delegateName);
        registerJavaDelegateMock("sendExternalNotificationDelegate");
        Map<String, Object> variables = buildVariables(productId, cost, riskScore);

        ProcessInstance process = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);

        assertThat(process).isNotEnded().isWaitingAt("send-external-notification");
        execute(job(process));

        assertThat(process).hasPassed(activityIds).isEnded();
        verifyJavaDelegateMock(delegateName).executed();
    }

    @ParameterizedTest
    @MethodSource("userApprovalProductCostAndRiskScore")
    void shouldRequireUserApprovalBasedOnItemNameValueAndRiskScore(String productId, double cost, double riskScore, boolean approved, String delegateName, String[] activityIds) {
        registerJavaDelegateMock(delegateName);
        registerJavaDelegateMock("sendExternalNotificationDelegate");
        Map<String, Object> variables = buildVariables(productId, cost, riskScore);

        ProcessInstanceWithVariables process = runtimeService.createProcessInstanceByKey(PROCESS_DEFINITION_KEY)
                .setVariables(variables)
                .executeWithVariablesInReturn();

        assertThat(process).isNotEnded().isWaitingAt("user-approval");
        complete(task(), isApproved(approved));
        execute(job(process));

        assertThat(process).isNotEnded().isWaitingAt("send-external-notification");
        execute(job(process));

        assertThat(process).hasPassed(activityIds).isEnded();
        verifyJavaDelegateMock(delegateName).executed();
    }

    private static Stream<Arguments> autoDecisionProductCostAndRiskScore() {
        return Stream.of(
                Arguments.of("any-item", 999.99, 150, "acceptPaymentDelegate", acceptPaymentIds()),
                Arguments.of("any-item", 1000, 150, "rejectPaymentDelegate", rejectPaymentIds()),
                Arguments.of("abc-123", 1000, 150, "acceptPaymentDelegate", acceptPaymentIds()),
                Arguments.of("xyz-789", 1000, 150, "acceptPaymentDelegate", acceptPaymentIds())
        );
    }

    private static Stream<Arguments> userApprovalProductCostAndRiskScore() {
        return Stream.of(
                Arguments.of("any-item", 1000, 0, true, "acceptPaymentDelegate", acceptPaymentIds()),
                Arguments.of("any-item", 1000, 149.99, false, "rejectPaymentDelegate", rejectPaymentIds())
        );
    }

    private static String[] acceptPaymentIds() {
        return new String[]{"accept-payment", sendExternalNotificationId()};
    }

    private static String[] rejectPaymentIds() {
        return new String[]{"reject-payment", sendExternalNotificationId()};
    }

    private static String sendExternalNotificationId() {
        return "send-external-notification";
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
