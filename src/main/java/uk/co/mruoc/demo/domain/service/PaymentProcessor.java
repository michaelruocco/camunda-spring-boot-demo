package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.Status;

@RequiredArgsConstructor
public class PaymentProcessor {

    private final PaymentRepository repository;

    public void process(Payment payment) {
        String id = payment.getId();
        if (repository.exists(id)) {
            throw new PaymentAlreadyExistsException(id);
        }
        repository.save(payment.withStatus(Status.PENDING));
    }

}
