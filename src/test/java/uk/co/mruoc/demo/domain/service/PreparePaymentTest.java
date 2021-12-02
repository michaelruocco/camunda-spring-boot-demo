package uk.co.mruoc.demo.domain.service;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.adapter.quote.RestQuoteClient;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.entity.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PreparePaymentTest {

    private final RestQuoteClient client = mock(RestQuoteClient.class);

    private final PreparePayment preparePayment = new PreparePayment(client);

    @Test
    void shouldLeaveAllFieldsUnchangedExceptStatusAndQuote() {
        Payment payment = PaymentMother.withStatus(null);

        Payment prepared = preparePayment.prepare(payment);

        assertThat(prepared)
                .usingRecursiveComparison()
                .ignoringFields("status", "quote")
                .isEqualTo(payment);
    }

    @Test
    void shouldSetStatusAsPending() {
        Payment payment = PaymentMother.withStatus(null);

        Payment prepared = preparePayment.prepare(payment);

        assertThat(prepared.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void shouldSetRandomlyLoadedQuote() {
        String expectedQuote = "my quote";
        when(client.loadRandomQuote()).thenReturn(expectedQuote);
        Payment payment = PaymentMother.withStatus(null);

        Payment prepared = preparePayment.prepare(payment);

        assertThat(prepared.getQuote()).isEqualTo(expectedQuote);
    }

}
