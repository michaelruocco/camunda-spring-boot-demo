package uk.co.mruoc.demo.security.rest;

public class InvalidAuthTokenException extends RuntimeException {

    public InvalidAuthTokenException(String message) {
        super(message);
    }

}
