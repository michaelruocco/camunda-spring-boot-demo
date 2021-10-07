package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.mruoc.demo.CaptureLogLines.captureLogLines;

class AcceptPaymentTest {

    private final VariableUpdater updater = mock(VariableUpdater.class);
    private final VariableExtractor extractor = mock(VariableExtractor.class);

    private final AcceptPayment acceptPayment = new AcceptPayment(updater, extractor);

    @Test
    void shouldSetAccepted() {
        DelegateExecution execution = mock(DelegateExecution.class);

        acceptPayment.execute(execution);

        verify(updater).setAccepted(execution);
    }

    @Test
    void shouldLogWithoutRiskScoreIfNotPresent() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(extractor.extractCost(execution)).thenReturn(50d);
        when(extractor.extractProductId(execution)).thenReturn("123456");
        when(extractor.extractRiskScore(execution)).thenReturn(Optional.empty());

        List<String> lines = captureLogLines(() -> acceptPayment.execute(execution));

        assertThat(lines).hasSize(1);
        assertThat(lines.get(0)).endsWith("Accepted payment of 50.0 for product 123456...");
    }

    @Test
    void shouldLogWithRiskScoreIfPresent() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(extractor.extractCost(execution)).thenReturn(51d);
        when(extractor.extractProductId(execution)).thenReturn("789123");
        when(extractor.extractRiskScore(execution)).thenReturn(Optional.of(123.45d));

        List<String> lines = captureLogLines(() -> acceptPayment.execute(execution));

        assertThat(lines).hasSize(1);
        assertThat(lines.get(0)).endsWith("Accepted payment of 51.0 for product 789123 with risk score 123.45...");
    }

}
