package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.adapter.quote.QuoteClient;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.Status;

@RequiredArgsConstructor
public class PreparePayment {

    private final QuoteClient client;

    public Payment prepare(Payment payment) {
        return payment.toBuilder()
                .quote(client.loadRandomQuote())
                .status(Status.PENDING)
                .build();
    }

}
