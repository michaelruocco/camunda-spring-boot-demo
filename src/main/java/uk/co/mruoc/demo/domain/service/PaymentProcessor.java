package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

import java.util.Optional;

@RequiredArgsConstructor
public class PaymentProcessor {

    private final PaymentRepository repository;
    private final PaymentCreator creator;
    private final PaymentUpdater updater;


    public void process(Payment payment) {
        Optional<Payment> existing = repository.read(payment.getId());
        if (existing.isEmpty()) {
            creator.create(payment);
        } else if (existing.get().isPending()) {
            updater.update(payment);
        } else {
            throw new PaymentAlreadyProcessedException(payment.getId());
        }
    }

}
