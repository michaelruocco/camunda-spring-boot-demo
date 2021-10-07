package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.util.Map;

public interface VariableUpdater {

    default void setQuote(DelegateExecution execution, String quote) {
        setQuote(execution.getVariables(), quote);
    }

    void setQuote(Map<String, Object> variables, String quote);

}
