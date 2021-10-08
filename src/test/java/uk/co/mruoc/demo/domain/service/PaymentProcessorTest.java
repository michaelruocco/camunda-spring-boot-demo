package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentProcessorTest {

    private final PaymentRepository repository = mock(PaymentRepository.class);
    private final PaymentUpdater updater = mock(PaymentUpdater.class);
    private final PaymentCreator creator = mock(PaymentCreator.class);

    private final PaymentProcessor processor = new PaymentProcessor(repository, updater, creator);

    @Test
    void shouldUpdatePaymentIfPaymentAlreadyExists() {
        Payment payment = PaymentMother.build();
        when(repository.exists(payment.getId())).thenReturn(true);

        processor.process(payment);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(updater).update(captor.capture());
        assertThat(captor.getValue()).isEqualTo(payment);
    }

    @Test
    void shouldCreatePaymentIfPaymentDoesNotExist() {
        Payment payment = PaymentMother.build();
        when(repository.exists(payment.getId())).thenReturn(false);

        processor.process(payment);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(creator).create(captor.capture());
        assertThat(captor.getValue()).isEqualTo(payment);
    }

}
