package uk.co.mruoc.demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import uk.org.webcompere.systemstubs.ThrowingRunnable;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.webcompere.systemstubs.resource.Resources.with;

@Testcontainers
class ApplicationIntegrationTest {

    @Container
    public static final LocalEnvironment LOCAL_ENVIRONMENT = new LocalEnvironment();

    private static int appPort;

    @BeforeAll
    public static void setUp() throws Exception {
        appPort = SocketUtils.findAvailableTcpPort();
        startApplication(buildSystemProperties(appPort));
    }

    @Test
    void applicationShouldBeUp() {
        RestTemplate template = new RestTemplate();
        String uri = String.format("http://localhost:%d/camunda/actuator/health", appPort);

        ResponseEntity<Void> response = template.exchange(uri, HttpMethod.GET, new HttpEntity<>(Void.class), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private static void startApplication(SystemProperties systemProperties) throws Exception {
        with(systemProperties).execute((ThrowingRunnable) Application::main);
    }

    private static SystemProperties buildSystemProperties(int port) {
        return new SystemProperties()
                .set("server.port", Integer.toString(port))
                .set("auth.server.http", LOCAL_ENVIRONMENT.getKeycloakHttpAuthUri())
                .set("auth.server.https", LOCAL_ENVIRONMENT.getKeycloakHttpsAuthUri())
                .set("auth.realm", "demo-local")
                .set("auth.client.id", "demo-client-id")
                .set("auth.client.secret", "demo-client-secret")
                .set("auth.admin.group", "camunda-admin")
                .set("aws.accessKeyId", "abc")
                .set("aws.secretAccessKey", "123")
                .set("aws.region", "eu-west-2")
                .set("aws.s3.endpoint.override", LOCAL_ENVIRONMENT.getAwsEndpointUri())
                .set("aws.s3.payment.bucket.name", "demo-payment")
                .set("quote.host", LOCAL_ENVIRONMENT.getWiremockEndpointUri());
    }

}
