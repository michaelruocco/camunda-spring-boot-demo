package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.demo.domain.entity.Payment;

@RequiredArgsConstructor
@Slf4j
public class RejectPayment {

    private final PaymentLoader loader;
    private final PaymentRepository repository;

    public void reject(String paymentId) {
        var payment = loader.load(paymentId);
        reject(payment);
    }

    private void reject(Payment payment) {
        var rejected = payment.reject();
        repository.save(rejected);
        log.info("rejected payment {}", rejected);
    }

}
