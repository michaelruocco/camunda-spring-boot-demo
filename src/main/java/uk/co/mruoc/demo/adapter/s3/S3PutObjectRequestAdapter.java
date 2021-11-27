package uk.co.mruoc.demo.adapter.s3;

import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Builder
@Data
public class S3PutObjectRequestAdapter {

    private final PutObjectRequest request;
    private final AsyncRequestBody body;

}
