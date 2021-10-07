package uk.co.mruoc.demo.adapter.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.PaymentRepository;

@RequiredArgsConstructor
@Slf4j
public class AcceptPayment implements JavaDelegate {

    private final VariableExtractor extractor;
    private final PaymentRepository repository;

    @Override
    public void execute(DelegateExecution execution) {
        Payment payment = extractor.extractPayment(execution);
        Payment acceptedPayment = payment.accept();
        repository.save(acceptedPayment);
        log.info("accepted payment {}", payment);
    }

}
