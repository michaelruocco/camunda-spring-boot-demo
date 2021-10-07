package uk.co.mruoc.demo.adapter.quote;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class QuoteClient {

    private static final String DEFAULT_HOST = "https://private-anon-08dad75e4b-goquotes.apiary-proxy.com";
    private static final String URL_TEMPLATE = "%s/api/v1/random?count=1";

    private final String host;
    private final RestTemplate template;

    public QuoteClient() {
        this(DEFAULT_HOST, new RestTemplate());
    }

    public String loadRandomQuote() {
        String url = toUrl(host);
        log.info("retrieving quote from {}", url);
        QuoteResponse response = template.getForObject(url, QuoteResponse.class);
        if (response == null) {
            throw new CouldNotRetrieveQuoteException();
        }
        return response.getFirstQuote().orElseThrow(CouldNotRetrieveQuoteException::new);
    }

    private static String toUrl(String host) {
        return String.format(URL_TEMPLATE, host);
    }

}
