package uk.co.mruoc.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import uk.co.mruoc.demo.adapter.s3.S3AsyncPaymentPersistor;
import uk.co.mruoc.demo.adapter.s3.S3Config;
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
                .credentialsProvider(SystemPropertyCredentialsProvider.create())
                .build();
    }

    @Bean
    public PaymentPersistor paymentPersistor(S3Config config) {
        return new S3AsyncPaymentPersistor(config);
    }

}
