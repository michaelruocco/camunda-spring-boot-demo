package uk.co.mruoc.demo.adapter.s3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import uk.co.mruoc.demo.LocalAwsServices;
import uk.co.mruoc.demo.adapter.camunda.s3.S3PaymentBucketCallable;
import uk.co.mruoc.demo.adapter.camunda.s3.S3PaymentGetterCallable;
import uk.co.mruoc.demo.domain.entity.Payment;
import uk.co.mruoc.demo.domain.entity.PaymentMother;
import uk.co.mruoc.demo.domain.service.PaymentPersistor;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

import java.time.Duration;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static uk.org.webcompere.systemstubs.resource.Resources.with;

@Testcontainers
class S3PersistenceIntegrationTest {

    @Container
    public static final LocalAwsServices AWS_SERVICES = new LocalAwsServices();

    private static final JsonConverter CONVERTER = new JacksonJsonConverter(new ObjectMapper());

    @BeforeEach
    void setUp() throws Exception {
        with(systemProperties()).execute(this::waitForBucket);
    }

    @Test
    void shouldPersistPaymentToS3() throws Exception {
        with(systemProperties()).execute(this::runS3UploadTest);
    }

    private void waitForBucket() {
        S3PaymentBucketCallable bucketExists = new S3PaymentBucketCallable(buildConfig());
        Awaitility.await().atMost(Duration.ofSeconds(30))
                .pollInterval(Duration.ofSeconds(1))
                .until(bucketExists);
    }

    private void runS3UploadTest() {
        S3Config config = buildConfig();
        PaymentPersistor persistor = new S3AsyncPaymentPersistor(CONVERTER, config);
        Payment payment = PaymentMother.build();

        persistor.persist(payment);

        S3PaymentGetterCallable paymentFound = new S3PaymentGetterCallable(config, payment.getId());
        Awaitility.await().atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(1))
                .until(paymentFound);
        assertThatJson(paymentFound.getContent()).isEqualTo(CONVERTER.toJson(payment));
    }

    private SystemProperties systemProperties() {
        return new SystemProperties()
                .set("aws.accessKeyId", AWS_SERVICES.getAccessKeyId())
                .set("aws.secretAccessKey", AWS_SERVICES.getSecretAccessKey());
    }

    private static S3Config buildConfig() {
        return S3Config.builder()
                .endpointOverride(AWS_SERVICES.getEndpointUri())
                .region(AWS_SERVICES.getRegion())
                .credentialsProvider(SystemPropertyCredentialsProvider.create())
                .paymentBucketName("demo-payment")
                .build();
    }

}
