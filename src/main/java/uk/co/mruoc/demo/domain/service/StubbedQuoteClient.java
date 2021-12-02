package uk.co.mruoc.demo.domain.service;

import java.util.UUID;

//TODO unit test
public class StubbedQuoteClient implements QuoteClient {

    @Override
    public String loadRandomQuote() {
        return String.format("stubbed-random-quote-%s", UUID.randomUUID());
    }

}
