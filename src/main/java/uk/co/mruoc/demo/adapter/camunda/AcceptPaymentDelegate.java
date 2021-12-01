package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import uk.co.mruoc.demo.domain.service.AcceptPayment;

@RequiredArgsConstructor
@Slf4j
public class AcceptPaymentDelegate implements JavaDelegate {

    private final VariableExtractor extractor;
    private final AcceptPayment acceptPayment;

    @Override
    public void execute(DelegateExecution execution) {
        String paymentId = extractor.extractPaymentId(execution);
        acceptPayment.accept(paymentId);
    }

}
