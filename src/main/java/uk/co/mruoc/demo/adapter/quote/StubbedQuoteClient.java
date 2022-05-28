package uk.co.mruoc.demo.adapter.quote;

import uk.co.mruoc.demo.domain.service.QuoteClient;

import java.util.UUID;

public class StubbedQuoteClient implements QuoteClient {

    @Override
    public String loadRandomQuote() {
        return String.format("stubbed-random-quote-%s", UUID.randomUUID());
    }

}
