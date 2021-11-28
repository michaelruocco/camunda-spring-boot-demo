package uk.co.mruoc.demo.adapter.camunda.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import uk.co.mruoc.demo.adapter.s3.S3Config;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class S3BucketGetter {

    private final S3Config config;
    private final S3Client client;

    public S3BucketGetter(S3Config config) {
        this(config, config.toClient());
    }

    public boolean bucketExists(String name) {
        try {
            log.info("checking if bucket exists with name {}", name);
            HeadBucketRequest request = HeadBucketRequest.builder()
                    .bucket(name)
                    .build();
            client.headBucket(request);
            return true;
        } catch (S3Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private static String toKey(String id) {
        return String.format("payment-%s", id);
    }

}
