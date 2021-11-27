package uk.co.mruoc.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import uk.co.mruoc.demo.adapter.s3.S3AsyncPaymentPersistor;
import uk.co.mruoc.demo.adapter.s3.S3Config;
import uk.co.mruoc.demo.adapter.s3.S3PersistenceResponseHandler;
import uk.co.mruoc.demo.adapter.s3.S3PutObjectRequestFactory;
import uk.co.mruoc.demo.domain.service.PaymentPersistor;

@Configuration
@Slf4j
public class ApplicationS3Config {

    @Bean
    public S3Config config(@Value("${aws.region}") String region,
            @Value("${aws.s3.endpoint.override:}") String endpointOverride,
            @Value("${aws.s3.payment.bucket.name}") String paymentBucketName) {
        return S3Config.builder()
                .endpointOverride(endpointOverride)
                .region(region)
                .paymentBucketName(paymentBucketName)
                .credentialsProvider(buildCredentialsProvider())
                .build();
    }

    @Bean
    public PaymentPersistor paymentPersistor(S3Config config) {
        return S3AsyncPaymentPersistor.builder()
                .requestFactory(new S3PutObjectRequestFactory(config.getPaymentBucketName()))
                .client(toClient(config))
                .responseHandler(new S3PersistenceResponseHandler())
                .build();
    }

    private static AwsCredentialsProvider buildCredentialsProvider() {
        return AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(SystemPropertyCredentialsProvider.create())
                .build();
    }

    private static S3AsyncClient toClient(S3Config config) {
        log.info("building s3 async client with region {}", config.getRegion());
        return config.configure(S3AsyncClient.builder()).build();
    }

}
