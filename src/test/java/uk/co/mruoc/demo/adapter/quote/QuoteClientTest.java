package uk.co.mruoc.demo.adapter.quote;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuoteClientTest {

    private static final String HOST = "http://localhost:8080";

    private final RestTemplate restTemplate = mock(RestTemplate.class);

    private final QuoteClient client = new QuoteClient(HOST, restTemplate);

    @Test
    void shouldThrowExceptionIfNoResponse() {
        givenResponseReturned(null);

        Throwable error = catchThrowable(client::loadRandomQuote);

        assertThat(error).isInstanceOf(CouldNotRetrieveQuoteException.class);
    }

    @Test
    void shouldThrowExceptionIfResponseHasEmptyQuote() {
        QuoteResponse response = givenResponseWith(null);
        givenResponseReturned(response);

        Throwable error = catchThrowable(client::loadRandomQuote);

        assertThat(error).isInstanceOf(CouldNotRetrieveQuoteException.class);
    }

    @Test
    void shouldReturnQuote() {
        String expectedQuote = "test quote";
        QuoteResponse response = givenResponseWith(expectedQuote);
        givenResponseReturned(response);

        String quote = client.loadRandomQuote();

        assertThat(quote).isEqualTo(expectedQuote);
    }

    private void givenResponseReturned(QuoteResponse response) {
        String url = String.format("%s/api/v1/random?count=1", HOST);
        when(restTemplate.getForObject(url, QuoteResponse.class)).thenReturn(response);
    }

    private QuoteResponse givenResponseWith(String quote) {
        QuoteResponse response = mock(QuoteResponse.class);
        when(response.getFirstQuote()).thenReturn(Optional.ofNullable(quote));
        return response;
    }

}
