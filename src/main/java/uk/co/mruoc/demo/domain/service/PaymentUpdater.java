package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

@RequiredArgsConstructor
public class PaymentUpdater {

    private final PaymentLoader paymentLoader;
    private final PaymentRepository repository;
    private final UpdateApproval updateApproval;

    public void update(Payment payment) {
        Payment existing = paymentLoader.load(payment.getId());
        if (existing.isPending()) {
            repository.save(payment);
            updateApproval.update(payment);
        } else {
            throw new PaymentAlreadyProcessedException(payment.getId());
        }
    }

}
