package uk.co.mruoc.demo.adapter.camunda.s3;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import uk.co.mruoc.demo.adapter.s3.S3Config;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class S3PaymentBucketCallable implements Callable<Boolean> {

    private final S3PaymentBucketGetter getter;

    public S3PaymentBucketCallable(S3Config config, S3Client client) {
        this(new S3PaymentBucketGetter(config, client));
    }

    @Override
    public Boolean call() {
        return getter.paymentBucketExists();
    }

}
