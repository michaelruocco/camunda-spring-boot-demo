package uk.co.mruoc.demo.adapter.s3;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import uk.co.mruoc.demo.config.InvalidUriException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Builder
@Data
@Slf4j
public class S3Config {

    private final String region;
    private final String paymentBucketName;
    private final String endpointOverride;
    private final AwsCredentialsProvider credentialsProvider;

    public S3AsyncClientBuilder configure(S3AsyncClientBuilder builder) {
        if (StringUtils.isNotEmpty(endpointOverride)) {
            log.info("configuring overridden s3 endpoint with {}", endpointOverride);
            builder.endpointOverride(toUri(endpointOverride));
        }
        log.info("configuring s3 region {} and credentials provider {}", region, credentialsProvider);
        return builder.region(Region.of(region)).credentialsProvider(credentialsProvider);
    }

    public Optional<URI> getEndpointOverride() {
        if (StringUtils.isEmpty(endpointOverride)) {
            return Optional.empty();
        }
        return Optional.of(toUri(endpointOverride));
    }

    private static URI toUri(String endpointOverride) {
        try {
            return new URI(endpointOverride);
        } catch (URISyntaxException e) {
            throw new InvalidUriException(endpointOverride);
        }
    }

}
