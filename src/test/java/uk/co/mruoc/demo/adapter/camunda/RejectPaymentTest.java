package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.PaymentLoader;
import uk.co.mruoc.demo.domain.service.PaymentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RejectPaymentTest {

    private final VariableExtractor extractor = mock(VariableExtractor.class);
    private final PaymentLoader loader = mock(PaymentLoader.class);
    private final PaymentRepository repository = mock(PaymentRepository.class);

    private final RejectPayment acceptPayment = new RejectPayment(extractor, loader, repository);

    @Test
    void shouldRejectAndSavePayment() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Payment payment = PaymentMother.pending();
        when(extractor.extractPaymentId(execution)).thenReturn(payment.getId());
        when(loader.load(payment.getId())).thenReturn(payment);

        acceptPayment.execute(execution);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(repository).save(captor.capture());
        Payment saved = captor.getValue();
        assertThat(saved).usingRecursiveComparison().isEqualTo(payment.reject());
    }

}
