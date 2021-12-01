package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.RejectPayment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RejectPaymentDelegateTest {

    private final VariableExtractor extractor = mock(VariableExtractor.class);
    private final RejectPayment rejectPayment = mock(RejectPayment.class);

    private final RejectPaymentDelegate delegate = new RejectPaymentDelegate(extractor, rejectPayment);

    @Test
    void shouldRejectPayment() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Payment payment = PaymentMother.pending();
        when(extractor.extractPaymentId(execution)).thenReturn(payment.getId());

        delegate.execute(execution);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(rejectPayment).reject(captor.capture());
        assertThat(captor.getValue()).isEqualTo(payment.getId());
    }

}
