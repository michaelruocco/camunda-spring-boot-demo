package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentProcessorTest {

    private final PaymentRepository repository = mock(PaymentRepository.class);
    private final PaymentCreator creator = mock(PaymentCreator.class);
    private final PaymentUpdater updater = mock(PaymentUpdater.class);

    private final PaymentProcessor processor = new PaymentProcessor(repository, creator, updater);

    @Test
    void shouldUpdatePaymentIfPaymentAlreadyExistsAndIsPending() {
        Payment payment = PaymentMother.pending();
        when(repository.read(payment.getId())).thenReturn(Optional.of(payment));

        processor.process(payment);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(updater).update(captor.capture());
        assertThat(captor.getValue()).isEqualTo(payment);
    }

    @Test
    void shouldCreatePaymentIfPaymentDoesNotExist() {
        Payment payment = PaymentMother.build();
        when(repository.read(payment.getId())).thenReturn(Optional.empty());

        processor.process(payment);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(creator).create(captor.capture());
        assertThat(captor.getValue()).isEqualTo(payment);
    }

    @Test
    void shouldThrowExceptionIfPaymentAlreadyExistsAndIsNotPending() {
        Payment payment = PaymentMother.accepted();
        when(repository.read(payment.getId())).thenReturn(Optional.of(payment));

        Throwable error = catchThrowable(() -> processor.process(payment));

        assertThat(error)
                .isInstanceOf(PaymentAlreadyProcessedException.class)
                .hasMessage(payment.getId());
    }

}
