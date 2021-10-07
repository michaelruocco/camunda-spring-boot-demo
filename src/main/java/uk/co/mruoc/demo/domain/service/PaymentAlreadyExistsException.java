package uk.co.mruoc.demo.domain.service;

public class PaymentAlreadyExistsException extends RuntimeException {

    public PaymentAlreadyExistsException(String id) {
        super(id);
    }

}
