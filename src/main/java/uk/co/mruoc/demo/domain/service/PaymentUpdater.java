package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

@RequiredArgsConstructor
public class PaymentUpdater {

    private final PreparePayment preparePayment;
    private final PaymentRepository repository;
    private final UpdateApproval updateApproval;

    public void update(Payment payment) {
        Payment prepared = preparePayment.prepare(payment);
        repository.save(prepared);
        updateApproval.update(prepared);
    }

}
