package uk.co.mruoc.demo.adapter.s3;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.json.JsonConverter;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class S3PutObjectRequestFactory {

    private final PutObjectRequest.Builder builder;
    private final JsonConverter converter;

    public S3PutObjectRequestFactory(String bucketName, JsonConverter converter) {
        this(toRequestBuilder(bucketName), converter);
    }

    public S3PutObjectRequestFactory(PutObjectRequest.Builder builder, JsonConverter converter) {
        this.builder = builder;
        this.converter = converter;
    }

    public S3PutObjectRequestAdapter build(Payment payment) {
        String json = converter.toJson(payment);
        return S3PutObjectRequestAdapter.builder()
                .request(toRequest(json, payment.getId()))
                .body(toBody(json))
                .build();
    }

    private PutObjectRequest toRequest(String json, String id) {
        return builder.contentLength((long) json.length())
                .key(toKey(id))
                .build();
    }

    private static PutObjectRequest.Builder toRequestBuilder(String bucketName) {
        log.info("building put object request factory with bucket name {}", bucketName);
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(APPLICATION_JSON_VALUE);
    }

    private static AsyncRequestBody toBody(String json) {
        return AsyncRequestBody.fromBytes(json.getBytes(StandardCharsets.UTF_8));
    }

    private static String toKey(String id) {
        return String.format("payment-%s", id);
    }

}
