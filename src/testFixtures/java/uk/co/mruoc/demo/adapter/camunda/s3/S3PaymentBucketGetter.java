package uk.co.mruoc.demo.adapter.camunda.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import uk.co.mruoc.demo.adapter.s3.S3Config;

@RequiredArgsConstructor
@Slf4j
public class S3PaymentBucketGetter {

    private final S3Config config;
    private final S3Client client;

    public S3PaymentBucketGetter(S3Config config) {
        this(config, config.toClient());
    }

    public boolean paymentBucketExists() {
        try {
            String name = config.getPaymentBucketName();
            log.info("checking if bucket exists with name {}", name);
            HeadBucketRequest request = HeadBucketRequest.builder()
                    .bucket(name)
                    .build();
            client.headBucket(request);
            return true;
        } catch (NoSuchBucketException e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
