package uk.co.mruoc.demo.adapter.repository;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.service.PaymentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class InMemoryPaymentRepository implements PaymentRepository {

    private final Map<String, Payment> payments;

    public InMemoryPaymentRepository() {
        this(new HashMap<>());
    }

    @Override
    public void save(Payment payment) {
        payments.put(payment.getId(), payment);
    }

    @Override
    public Optional<Payment> read(String id) {
        return Optional.ofNullable(payments.get(id));
    }

}
