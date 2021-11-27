package uk.co.mruoc.demo.adapter.s3;

public class InvalidUriException extends RuntimeException {

    public InvalidUriException(String uri) {
        super(String.format("invalid endpoint override value %s", uri));
    }

}
