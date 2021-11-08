package uk.co.mruoc.demo.domain.service;

import uk.co.mruoc.demo.domain.entity.Payment;

import java.util.Collection;
import java.util.Optional;

public interface PaymentRepository {

    void save(Payment payment);

    Optional<Payment> read(String id);

    Collection<Payment> readAll();

}
