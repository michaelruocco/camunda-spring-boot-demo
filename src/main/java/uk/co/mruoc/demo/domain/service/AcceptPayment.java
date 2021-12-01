package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.demo.domain.entity.Payment;

@RequiredArgsConstructor
@Slf4j
public class AcceptPayment {

    private final PaymentLoader loader;
    private final PaymentRepository repository;

    public void accept(String paymentId) {
        var payment = loader.load(paymentId);
        accept(payment);
    }

    private void accept(Payment payment) {
        var accepted = payment.accept();
        repository.save(accepted);
        log.info("accepted payment {}", accepted);
    }

}
