package uk.co.mruoc.demo.rest;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.PaymentService;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

    private final PaymentService service = mock(PaymentService.class);

    private final PaymentController controller = new PaymentController(service);

    @Test
    void shouldProcessPaymentOnCreate() {
        String id = "1";
        Payment payment = PaymentMother.withId(id);

        controller.create(payment);

        verify(service).process(payment);
    }

    @Test
    void shouldReturnLoadedPaymentOnCreate() {
        String id = "2";
        Payment payment = PaymentMother.withId(id);
        Payment expectedPayment = givenPaymentLoadedById(id);

        Payment created = controller.create(payment);

        assertThat(created).isEqualTo(expectedPayment);
    }

    @Test
    void shouldGetPayment() {
        String id = "3";
        Payment expectedPayment = givenPaymentLoadedById(id);

        Payment payment = controller.get(id);

        assertThat(payment).isEqualTo(expectedPayment);
    }

    @Test
    void shouldGetAllPayments() {
        Collection<Payment> expectedPayments = givenPaymentsLoaded();

        Collection<Payment> payments = controller.getAll();

        assertThat(payments).isEqualTo(expectedPayments);
    }

    private Payment givenPaymentLoadedById(String id) {
        Payment payment = PaymentMother.withId(id);
        when(service.load(id)).thenReturn(payment);
        return payment;
    }

    private Collection<Payment> givenPaymentsLoaded() {
        Collection<Payment> payments = Collections.singleton(givenPaymentLoadedById("1"));
        when(service.load()).thenReturn(payments);
        return payments;
    }

}
