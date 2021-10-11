package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.PaymentLoader;
import uk.co.mruoc.demo.domain.service.PaymentRepository;

@RequiredArgsConstructor
@Slf4j
public class RejectPayment implements JavaDelegate {

    private final VariableExtractor extractor;
    private final PaymentLoader loader;
    private final PaymentRepository repository;

    @Override
    public void execute(DelegateExecution execution) {
        var payment = loadPayment(execution);
        reject(payment);
    }

    private Payment loadPayment(DelegateExecution execution) {
        String paymentId = extractor.extractPaymentId(execution);
        return loader.load(paymentId);
    }

    private void reject(Payment payment) {
        var rejected = payment.reject();
        repository.save(rejected);
        log.info("rejected payment {}", rejected);
    }

}
