package uk.co.mruoc.demo.config;

public class InvalidUriException extends RuntimeException {

    public InvalidUriException(String uri) {
        super(String.format("invalid endpoint override value %s", uri));
    }

}
