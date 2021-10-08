package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

@RequiredArgsConstructor
public class PaymentProcessor {

    private final PaymentRepository repository;
    private final PaymentUpdater updater;
    private final PaymentCreator creator;

    public void process(Payment payment) {
        if (repository.exists(payment.getId())) {
            updater.update(payment);
        } else {
            creator.create(payment);
        }
    }

}
