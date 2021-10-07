package uk.co.mruoc.demo.domain.service;

import uk.co.mruoc.demo.domain.entity.Payment;

public interface PaymentService {

    Payment process(Payment payment);

    Payment load(String id);

}
