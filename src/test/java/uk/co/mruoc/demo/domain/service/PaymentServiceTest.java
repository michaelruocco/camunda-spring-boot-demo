package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    private final PaymentProcessor processor = mock(PaymentProcessor.class);
    private final PaymentLoader loader = mock(PaymentLoader.class);

    private final PaymentService service = new PaymentService(processor, loader);

    @Test
    void shouldProcessPayment() {
        Payment payment = PaymentMother.build();

        service.process(payment);

        verify(processor).process(payment);
    }

    @Test
    void shouldLoadPayment() {
        Payment expectedPayment = PaymentMother.build();
        when(loader.load(expectedPayment.getId())).thenReturn(expectedPayment);

        Payment payment = service.load(expectedPayment.getId());

        assertThat(payment).isEqualTo(expectedPayment);
    }

    @Test
    void shouldLoadPayments() {
        Collection<Payment> expectedPayments = Collections.singleton(PaymentMother.build());
        when(loader.load()).thenReturn(expectedPayments);

        Collection<Payment> payments = service.load();

        assertThat(payments).isEqualTo(expectedPayments);
    }

}
