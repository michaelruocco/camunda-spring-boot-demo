package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.PaymentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RejectPaymentTest {


    private final PaymentRepository repository = mock(PaymentRepository.class);
    private final VariableExtractor extractor = mock(VariableExtractor.class);

    private final RejectPayment acceptPayment = new RejectPayment(extractor, repository);

    @Test
    void shouldRejectAndSavePayment() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Payment payment = PaymentMother.pending();
        when(extractor.extractPayment(execution)).thenReturn(payment);

        acceptPayment.execute(execution);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(repository).save(captor.capture());
        Payment saved = captor.getValue();
        assertThat(saved).usingRecursiveComparison().isEqualTo(payment.reject());
    }

}
