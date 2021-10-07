package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import uk.co.mruoc.demo.domain.entity.Status;

import java.util.Map;

public interface VariableUpdater {

    default void setQuote(DelegateExecution execution, String quote) {
        setQuote(execution.getVariables(), quote);
    }

    default void setAccepted(DelegateExecution execution) {
        setAccepted(execution.getVariables());
    }

    default void setAccepted(Map<String, Object> variables) {
        setStatus(variables, Status.ACCEPTED);
    }

    default void setRejected(DelegateExecution execution) {
        setRejected(execution.getVariables());
    }

    default void setRejected(Map<String, Object> variables) {
        setStatus(variables, Status.REJECTED);
    }

    void setQuote(Map<String, Object> variables, String quote);

    void setStatus(Map<String, Object> variables, Status status);

}
