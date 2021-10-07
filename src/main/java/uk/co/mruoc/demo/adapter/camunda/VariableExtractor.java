package uk.co.mruoc.demo.adapter.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import uk.co.mruoc.demo.domain.entity.Product;

import java.util.Optional;

public interface VariableExtractor {

    default String extractProductId(DelegateExecution execution) {
        return extractProduct(execution).getId();
    }

    Product extractProduct(DelegateExecution execution);

    double extractCost(DelegateExecution execution);

    Optional<Double> extractRiskScore(DelegateExecution execution);

}
