package uk.co.mruoc.demo.kafka;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.PaymentService;
import uk.co.mruoc.json.JsonConverter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProcessPaymentListenerTest {

    private final JsonConverter jsonConverter = mock(JsonConverter.class);
    private final PaymentService service = mock(PaymentService.class);

    private final ProcessPaymentListener listener = new ProcessPaymentListener(jsonConverter, service);

    @Test
    void shouldProcessPayment() {
        String json = "json";
        Payment payment = givenConvertsToPayment(json);

        listener.processPayment(json);

        verify(service).process(payment);
    }

    private Payment givenConvertsToPayment(String json) {
        Payment payment = PaymentMother.build();
        when(jsonConverter.toObject(json, Payment.class)).thenReturn(payment);
        return payment;
    }

}
