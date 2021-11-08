package uk.co.mruoc.demo.adapter.camunda;

public class ExternalNotificationFailedException extends RuntimeException {

    public ExternalNotificationFailedException(String id) {
        super(String.format("failed to send external notification for %s", id));
    }

}
