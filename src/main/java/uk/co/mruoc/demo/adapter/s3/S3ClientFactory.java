package uk.co.mruoc.demo.adapter.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@RequiredArgsConstructor
@Slf4j
public class S3ClientFactory {

    private final S3ClientConfigurer<S3ClientBuilder, S3Client> clientConfigurer;
    private final S3ClientConfigurer<S3AsyncClientBuilder, S3AsyncClient> asyncClientConfigurer;

    public S3ClientFactory(S3Config config) {
        this(new S3ClientConfigurer<>(config), new S3ClientConfigurer<>(config));
    }

    public S3Client buildClient() {
        return clientConfigurer.configure(S3Client.builder()).build();
    }

    public S3AsyncClient buildAsyncClient() {
        return asyncClientConfigurer.configure(S3AsyncClient.builder()).build();
    }

}
