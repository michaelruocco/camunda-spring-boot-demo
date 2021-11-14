package uk.co.mruoc.demo.adapter.quote;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class QuoteClient {

    private static final String DEFAULT_HOST = "https://api.quotable.io";
    private static final String URL_TEMPLATE = "%s/random";

    private final String host;
    private final RestTemplate template;

    public QuoteClient() {
        this(DEFAULT_HOST, new RestTemplate());
    }

    public String loadRandomQuote() {
        String url = toUrl(host);
        log.debug("retrieving quote from {}", url);
        QuoteResponse response = template.getForObject(url, QuoteResponse.class);
        if (response == null) {
            throw new CouldNotRetrieveQuoteException();
        }
        return response.getContent();
    }

    private static String toUrl(String host) {
        return String.format(URL_TEMPLATE, host);
    }

}
