package uk.co.mruoc.demo.adapter.quote;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.service.QuoteClient;

import static org.assertj.core.api.Assertions.assertThat;

class StubbedQuoteClientTest {

    private static final String STUBBED_PREFIX = "stubbed-random-quote-";

    private final QuoteClient client = new StubbedQuoteClient();

    @Test
    void shouldReturnStubbedQuoteWithUniqueSuffix() {
        String quote1 = client.loadRandomQuote();
        String quote2 = client.loadRandomQuote();

        assertThat(quote1).startsWith(STUBBED_PREFIX);
        assertThat(quote2).startsWith(STUBBED_PREFIX).isNotEqualTo(quote1);
    }

}
