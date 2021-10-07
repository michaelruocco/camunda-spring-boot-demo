package uk.co.mruoc.demo.rest;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.PaymentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

    private final PaymentService service = mock(PaymentService.class);

    private final PaymentController controller = new PaymentController(service);

    @Test
    void shouldProcessPayment() {
        Payment payment = mock(Payment.class);
        Payment expectedPayment = mock(Payment.class);
        when(service.process(payment)).thenReturn(expectedPayment);

        Payment created = controller.create(payment);

        assertThat(created).isEqualTo(expectedPayment);
    }

    @Test
    void shouldGetPayment() {
        String id = "payment-id";
        Payment expectedPayment = mock(Payment.class);
        when(service.load(id)).thenReturn(expectedPayment);

        Payment payment = controller.get(id);

        assertThat(payment).isEqualTo(expectedPayment);
    }

}
