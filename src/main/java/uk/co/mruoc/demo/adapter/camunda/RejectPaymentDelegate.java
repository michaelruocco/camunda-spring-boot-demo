package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import uk.co.mruoc.demo.domain.service.RejectPayment;

@RequiredArgsConstructor
@Slf4j
public class RejectPaymentDelegate implements JavaDelegate {

    private final VariableExtractor extractor;
    private final RejectPayment rejectPayment;

    @Override
    public void execute(DelegateExecution execution) {
        String paymentId = extractor.extractPaymentId(execution);
        rejectPayment.reject(paymentId);
    }

}
