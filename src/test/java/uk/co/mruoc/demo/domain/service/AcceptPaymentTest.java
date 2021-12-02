package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AcceptPaymentTest {

    private final PaymentLoader loader = mock(PaymentLoader.class);
    private final PaymentRepository repository = mock(PaymentRepository.class);

    private final AcceptPayment acceptPayment = new AcceptPayment(loader, repository);

    @Test
    void shouldAcceptAndSavePayment() {
        Payment payment = PaymentMother.pending();
        when(loader.load(payment.getId())).thenReturn(payment);

        acceptPayment.accept(payment.getId());

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(repository).save(captor.capture());
        Payment saved = captor.getValue();
        assertThat(saved).usingRecursiveComparison().isEqualTo(payment.accept());
    }

}
