package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class RejectPayment implements JavaDelegate {

    private final VariableUpdater updater;
    private final VariableExtractor extractor;

    @Override
    public void execute(DelegateExecution execution) {
        updater.setRejected(execution);
        String productId = extractor.extractProductId(execution);
        double cost = extractor.extractCost(execution);
        Optional<Double> riskScore = extractor.extractRiskScore(execution);
        if (riskScore.isPresent()) {
            log.info("Rejected payment of {} for product {} with risk score {}...", cost, productId, riskScore.get());
            return;
        }
        log.info("Rejected payment of {} for product {}...", cost, productId);
    }

}
