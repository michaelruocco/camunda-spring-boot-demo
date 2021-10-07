package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class StubPaymentService implements PaymentService {

    private final Map<String, Payment> payments;

    public StubPaymentService() {
        this(new HashMap<>());
    }

    @Override
    public Payment process(Payment payment) {
        return payments.put(payment.getId(), payment);
    }

    @Override
    public Payment load(String id) {
        return payments.get(id);
    }

}
