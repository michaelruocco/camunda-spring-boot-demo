package uk.co.mruoc.demo.adapter.quote;

import lombok.Data;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Data
public class QuoteResponse {

    private Collection<Quote> quotes;

    public Optional<String> getFirstQuote() {
        return Optional.ofNullable(quotes)
                .map(Collection::stream)
                .flatMap(Stream::findFirst)
                .map(Quote::getText);
    }

}
