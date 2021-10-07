package uk.co.mruoc.demo.adapter.camunda.bpmn;

import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;

class PaymentAutoApproveDmnTest {

    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension.builder()
            .configurationResource("test.camunda.cfg.xml")
            .build();

    private final DecisionService decisionService = extension.getProcessEngine().getDecisionService();

    @ParameterizedTest
    @MethodSource("productIdsAndApproved")
    @Deployment(resources = {"bpmns/payment-auto-approve.dmn"})
    void shouldAutoApproveSpecificProducts(String productId, boolean approved) {
        Map<String, Object> variables = buildVariables(productId);

        DmnDecisionTableResult result = decisionService.evaluateDecisionTableByKey("approve-payment", variables);

        assertThat(result.getSingleResult()).containsOnly(entry("approved", approved));
    }

    private static Stream<Arguments> productIdsAndApproved() {
        return Stream.of(
                Arguments.of("abc-123", true),
                Arguments.of("xyz-789", true),
                Arguments.of("abc-124",false),
                Arguments.of("other-product",false)
        );
    }

    private static Map<String, Object> buildVariables(String item) {
        return withVariables("productId", item);
    }

}
