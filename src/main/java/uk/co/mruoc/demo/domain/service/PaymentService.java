package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProcessor processor;
    private final PaymentLoader loader;

    public void process(Payment payment) {
        processor.process(payment);
    }

    public Payment load(String id) {
        return loader.load(id);
    }

}
