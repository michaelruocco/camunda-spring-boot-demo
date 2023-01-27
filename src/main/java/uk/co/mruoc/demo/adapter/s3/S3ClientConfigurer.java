package uk.co.mruoc.demo.adapter.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3BaseClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class S3ClientConfigurer<B extends S3BaseClientBuilder<B, C>, C> {

    private final S3Config config;

    public B configure(S3BaseClientBuilder<B, C> builder) {
        B configured = configureRegionAndCredentialsProvider(builder);
        Optional<String> endpointOverride = config.getEndpointOverride();
        if (endpointOverride.isPresent()) {
            log.info("configuring overridden s3 endpoint with {}", endpointOverride);
            return configured.endpointOverride(toUri(endpointOverride.get()));
        }
        return configured;
    }

    private B configureRegionAndCredentialsProvider(S3BaseClientBuilder<B, C> builder) {
        String region = config.getRegion();
        AwsCredentialsProvider credentialsProvider = config.getCredentialsProvider();
        log.info("configuring s3 region {} and credentials provider {}", region, credentialsProvider);
        return builder.region(Region.of(region)).credentialsProvider(credentialsProvider).forcePathStyle(true);
    }

    private static URI toUri(String endpointOverride) {
        try {
            return new URI(endpointOverride);
        } catch (URISyntaxException e) {
            throw new InvalidUriException(e);
        }
    }

}
