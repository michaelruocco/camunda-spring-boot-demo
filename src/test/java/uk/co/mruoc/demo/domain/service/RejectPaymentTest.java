package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RejectPaymentTest {

    private final PaymentLoader loader = mock(PaymentLoader.class);
    private final PaymentRepository repository = mock(PaymentRepository.class);

    private final RejectPayment rejectPayment = new RejectPayment(loader, repository);

    @Test
    void shouldRejectAndSavePayment() {
        Payment payment = PaymentMother.pending();
        when(loader.load(payment.getId())).thenReturn(payment);

        rejectPayment.reject(payment.getId());

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(repository).save(captor.capture());
        Payment saved = captor.getValue();
        assertThat(saved).usingRecursiveComparison().isEqualTo(payment.reject());
    }

}
