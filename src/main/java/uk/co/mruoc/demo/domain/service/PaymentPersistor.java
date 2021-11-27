package uk.co.mruoc.demo.domain.service;

import uk.co.mruoc.demo.domain.entity.Payment;

public interface PaymentPersistor {

    void persist(Payment payment);

}
