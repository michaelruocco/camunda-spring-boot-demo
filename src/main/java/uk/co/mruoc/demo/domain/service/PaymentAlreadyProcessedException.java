package uk.co.mruoc.demo.domain.service;

public class PaymentAlreadyProcessedException extends RuntimeException {

    public PaymentAlreadyProcessedException(String id) {
        super(id);
    }

}
