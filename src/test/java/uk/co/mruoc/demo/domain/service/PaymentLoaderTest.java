package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentLoaderTest {

    private final PaymentRepository repository = mock(PaymentRepository.class);

    private final PaymentLoader loader = new PaymentLoader(repository);

    @Test
    void shouldThrowExceptionIfPaymentDoesNotExist() {
        String id = "12345";
        when(repository.read(id)).thenReturn(Optional.empty());

        Throwable error = catchThrowable(() -> loader.load(id));

        assertThat(error)
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessage(id);
    }

    @Test
    void shouldReturnPaymentIfExists() {
        String id = "12345";
        Payment expectedPayment = mock(Payment.class);
        when(repository.read(id)).thenReturn(Optional.of(expectedPayment));

        Payment payment = loader.load(id);

        assertThat(payment).isEqualTo(expectedPayment);
    }

}
