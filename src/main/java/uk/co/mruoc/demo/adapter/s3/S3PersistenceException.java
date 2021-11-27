package uk.co.mruoc.demo.adapter.s3;

public class S3PersistenceException extends RuntimeException {

    public S3PersistenceException(String message) {
        super(message);
    }

    public S3PersistenceException(Throwable cause) {
        super(cause);
    }

}
