package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentProcessorTest {

    private final PreparePayment preparePayment = mock(PreparePayment.class);
    private final PaymentRepository repository = mock(PaymentRepository.class);

    private final PaymentProcessor processor = new PaymentProcessor(preparePayment, repository);

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
    void shouldPrepareAndSavePaymentIfPaymentDoesNotAlreadyExist() {
        Payment payment = PaymentMother.build();
        when(repository.exists(payment.getId())).thenReturn(false);
        Payment preparedPayment = mock(Payment.class);
        when(preparePayment.prepare(payment)).thenReturn(preparedPayment);

        processor.process(payment);

        verify(repository).save(preparedPayment);
    }

}
