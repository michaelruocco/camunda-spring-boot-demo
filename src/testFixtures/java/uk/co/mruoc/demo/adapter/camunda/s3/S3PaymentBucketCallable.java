package uk.co.mruoc.demo.adapter.s3;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.demo.adapter.camunda.s3.S3ObjectGetter;

import java.util.Optional;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class S3BucketCreatedCallable implements Callable<Boolean> {

    private final S3ObjectGetter getter;
    private final String paymentId;
    private String content;

    public S3BucketCreatedCallable(S3Config config, String paymentId) {
        this(new S3ObjectGetter(config), paymentId);
    }

    @Override
    public Boolean call() {
        Optional<String> payment = getter.getPaymentContent(paymentId);
        if (payment.isPresent()) {
            this.content = payment.get();
            return true;
        }
        return false;
    }

    public String getContent() {
        return content;
    }

}