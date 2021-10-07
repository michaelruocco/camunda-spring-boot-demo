package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import uk.co.mruoc.demo.adapter.quote.QuoteClient;

@RequiredArgsConstructor
@Slf4j
public class RetrieveQuote implements JavaDelegate {

    private final QuoteClient client;
    private final VariableUpdater variableUpdater;

    public RetrieveQuote(VariableUpdater variableUpdater) {
        this(new QuoteClient(), variableUpdater);
    }

    @Override
    public void execute(DelegateExecution execution) {
        String quote = client.loadRandomQuote();
        log.info("loaded random quote {}", quote);
        variableUpdater.setQuote(execution, quote);
    }

}
