package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

import java.util.Collection;

@RequiredArgsConstructor
public class PaymentLoader {

    private final PaymentRepository repository;

    public Payment load(String id) {
        return repository.read(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

    public Collection<Payment> load() {
        return repository.readAll();
    }

}
