package uk.co.mruoc.demo.domain.service;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.domain.entity.Payment;

@RequiredArgsConstructor
public class PaymentLoader {

    private final PaymentRepository repository;

    public Payment load(String id) {
        return repository.read(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

}
