package uk.co.mruoc.demo.adapter.s3;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

import java.util.Optional;

@Builder
@Data
@Slf4j
public class S3Config {

    private final String region;
    private final String paymentBucketName;
    private final String endpointOverride;
    private final AwsCredentialsProvider credentialsProvider;

    public Optional<String> getEndpointOverride() {
        if (StringUtils.isEmpty(endpointOverride)) {
            return Optional.empty();
        }
        return Optional.of(endpointOverride);
    }

}
