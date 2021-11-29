package uk.co.mruoc.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import uk.co.mruoc.demo.adapter.s3.S3AsyncPaymentPersistor;
import uk.co.mruoc.demo.adapter.s3.S3ClientConfigurer;
import uk.co.mruoc.demo.adapter.s3.S3Config;
import uk.co.mruoc.demo.adapter.s3.S3PutObjectRequestFactory;
import uk.co.mruoc.demo.domain.service.PaymentPersistor;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;

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
    public JsonConverter jsonConverter(ObjectMapper mapper) {
        return new JacksonJsonConverter(mapper);
    }

    @Bean
    public S3PutObjectRequestFactory requestFactory(S3Config config, JsonConverter converter) {
        return new S3PutObjectRequestFactory(config.getPaymentBucketName(), converter);
    }

    @Bean
    public S3AsyncClient asyncClient(S3Config config) {
        S3ClientConfigurer<S3AsyncClientBuilder, S3AsyncClient> configurer = new S3ClientConfigurer<>(config);
        return configurer.configure(S3AsyncClient.builder()).build();
    }

    @Bean
    public PaymentPersistor paymentPersistor(S3PutObjectRequestFactory requestFactory, S3AsyncClient client) {
        return new S3AsyncPaymentPersistor(requestFactory, client);
    }

}
