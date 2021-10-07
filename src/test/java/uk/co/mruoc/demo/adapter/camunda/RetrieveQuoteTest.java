package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.adapter.quote.QuoteClient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RetrieveQuoteTest {

    private final QuoteClient client = mock(QuoteClient.class);
    private final VariableUpdater variableUpdater = mock(VariableUpdater.class);

    private final DelegateExecution execution = mock(DelegateExecution.class);

    private final RetrieveQuote retrieveQuote = new RetrieveQuote(client, variableUpdater);

    @Test
    void shouldStoreRandomQuote() {
        String quote = givenQuoteReturned();

        retrieveQuote.execute(execution);

        verify(variableUpdater).setQuote(execution, quote);
    }

    private String givenQuoteReturned() {
        String quote = "random-quote";
        when(client.loadRandomQuote()).thenReturn(quote);
        return quote;
    }


}
