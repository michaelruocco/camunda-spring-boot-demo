package uk.co.mruoc.demo.security.rest;

public class InvalidAuthenticationException extends RuntimeException {

    public InvalidAuthenticationException(String message) {
        super(message);
    }

}
