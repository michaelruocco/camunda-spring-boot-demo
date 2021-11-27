package uk.co.mruoc.demo.domain.service;

import lombok.Builder;
import uk.co.mruoc.demo.domain.entity.Payment;

@Builder
public class PaymentCreator {

    private final PreparePayment preparePayment;
    private final PaymentPersistor persistor;
    private final PaymentRepository repository;
    private final RequestApproval requestApproval;

    public void create(Payment payment) {
        var prepared = preparePayment.prepare(payment);
        persistor.persist(prepared);
        repository.save(prepared);
        requestApproval.request(prepared);
    }

}
