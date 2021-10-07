package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import uk.co.mruoc.demo.domain.entity.Payment;

public interface VariableExtractor {

    Payment extractPayment(DelegateExecution execution);

}
