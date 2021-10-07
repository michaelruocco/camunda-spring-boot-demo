package uk.co.mruoc.demo.domain.service;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String id) {
        super(id);
    }

}
