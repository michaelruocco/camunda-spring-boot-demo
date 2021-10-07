package uk.co.mruoc.demo.adapter.quote;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteResponseTest {

    @Test
    void shouldReturnEmptyFirstQuoteIfQuotesIsNull() {
        QuoteResponse response = new QuoteResponse();

        Optional<String> quote = response.getFirstQuote();

        assertThat(quote).isEmpty();
    }

    @Test
    void shouldReturnEmptyFirstQuoteIfQuotesIsEmpty() {
        QuoteResponse response = new QuoteResponse();
        response.setQuotes(Collections.emptyList());

        Optional<String> quote = response.getFirstQuote();

        assertThat(quote).isEmpty();
    }

    @Test
    void shouldReturnFirstQuoteIfPresent() {
        String expectedQuote = "test quote";
        QuoteResponse response = new QuoteResponse();
        response.setQuotes(Collections.singleton(toQuote(expectedQuote)));

        Optional<String> quote = response.getFirstQuote();

        assertThat(quote).contains(expectedQuote);
    }

    private Quote toQuote(String text) {
        Quote quote = new Quote();
        quote.setText(text);
        return quote;
    }

}
