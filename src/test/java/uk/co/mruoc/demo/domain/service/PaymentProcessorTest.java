package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.entity.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentProcessorTest {

    private final PaymentRepository repository = mock(PaymentRepository.class);

    private final PaymentProcessor processor = new PaymentProcessor(repository);

    @Test
    void shouldThrowExceptionIfPaymentAlreadyExists() {
        Payment payment = PaymentMother.build();
        when(repository.exists(payment.getId())).thenReturn(true);

        Throwable error = catchThrowable(() -> processor.process(payment));

        assertThat(error)
                .isInstanceOf(PaymentAlreadyExistsException.class)
                .hasMessage(payment.getId());
    }

    @Test
    void shouldSavePaymentWithPendingStatusIfPaymentDoesNotAlreadyExist() {
        Payment payment = PaymentMother.build();
        when(repository.exists(payment.getId())).thenReturn(false);

        processor.process(payment);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(repository).save(captor.capture());
        Payment savedPayment = captor.getValue();
        assertThat(savedPayment)
                .usingRecursiveComparison()
                .isEqualTo(payment.withStatus(Status.PENDING));
    }

}
