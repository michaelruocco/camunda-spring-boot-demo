package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.service.SendExternalNotification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SendExternalNotificationDelegateTest {

    private final VariableExtractor extractor = mock(VariableExtractor.class);
    private final SendExternalNotification sendNotification = mock(SendExternalNotification.class);

    private final SendExternalNotificationDelegate delegate = new SendExternalNotificationDelegate(extractor, sendNotification);

    @Test
    void shouldSendNotification() {
        String paymentId = "123";
        DelegateExecution execution = givenExecutionWithId(paymentId);

        delegate.execute(execution);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(sendNotification).send(captor.capture());
        assertThat(captor.getValue()).isEqualTo(paymentId);
    }

    private DelegateExecution givenExecutionWithId(String id) {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(extractor.extractPaymentId(execution)).thenReturn(id);
        return execution;
    }

}
