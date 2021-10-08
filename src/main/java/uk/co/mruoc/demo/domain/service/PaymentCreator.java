package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

@RequiredArgsConstructor
public class PaymentCreator {

    private final PreparePayment preparePayment;
    private final PaymentRepository repository;
    private final RequestApproval requestApproval;

    public void create(Payment payment) {
        String id = payment.getId();
        if (repository.exists(id)) {
            throw new PaymentAlreadyExistsException(id);
        }
        Payment prepared = preparePayment.prepare(payment);
        repository.save(prepared);
        requestApproval.request(prepared);
    }

}
