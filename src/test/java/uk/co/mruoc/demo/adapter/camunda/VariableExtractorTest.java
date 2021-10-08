package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VariableExtractorTest {

    private static final String PAYMENT_ID = "12345";

    private final VariableExtractor extractor = new VariableExtractor();

    @Test
    void shouldExtractPaymentId() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getVariables()).thenReturn(Map.of("paymentId", PAYMENT_ID));

        String paymentId = extractor.extractPaymentId(execution);

        assertThat(paymentId).isEqualTo(PAYMENT_ID);
    }

}
