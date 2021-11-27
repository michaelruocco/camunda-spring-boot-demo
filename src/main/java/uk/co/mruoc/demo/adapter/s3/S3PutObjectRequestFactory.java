package uk.co.mruoc.demo.adapter.s3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import uk.co.mruoc.demo.domain.entity.Payment;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class S3PutObjectRequestFactory {

    private final PutObjectRequest.Builder builder;
    private final ObjectMapper mapper;

    public S3PutObjectRequestFactory(String bucketName) {
        this(bucketName, new ObjectMapper());
    }

    public S3PutObjectRequestFactory(String bucketName, ObjectMapper mapper) {
        this(toRequestBuilder(bucketName), mapper);
    }

    public S3PutObjectRequestAdapter build(Payment payment) {
        String json = toJson(payment);
        return S3PutObjectRequestAdapter.builder()
                .request(toRequest(json, payment.getId()))
                .body(toBody(json))
                .build();
    }

    private String toJson(Payment payment) {
        try {
            return mapper.writeValueAsString(payment);
        } catch (JsonProcessingException e) {
            throw new S3PersistenceException(e);
        }
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
