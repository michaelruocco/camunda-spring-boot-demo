package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.util.Map;

public class VariableExtractor {

    public String extractPaymentId(DelegateExecution execution) {
        return extractPaymentId(execution.getVariables());
    }

    public String extractPaymentId(Map<String, Object> variables) {
        return variables.get("paymentId").toString();
    }

}
