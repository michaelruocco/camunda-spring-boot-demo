package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

@RequiredArgsConstructor
public class PaymentUpdater {

    private final PaymentRepository repository;
    private final UpdateApproval updateApproval;

    public void update(Payment payment) {
        repository.save(payment);
        updateApproval.update(payment);
    }

}
