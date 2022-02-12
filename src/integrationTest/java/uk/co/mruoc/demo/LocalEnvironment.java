package uk.co.mruoc.demo;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import java.io.File;
import java.time.Duration;

@Slf4j
public class LocalEnvironment extends DockerComposeContainer<LocalEnvironment> {

    private static final String AWS_SERVICE_NAME = "aws-services";
    private static final int AWS_PORT = 4566;

    private static final String KEYCLOAK_SERVICE_NAME = "keycloak";
    private static final int KEYCLOAK_HTTP_PORT = 8091;
    private static final int KEYCLOAK_HTTPS_PORT = 8092;
    private static final WaitStrategy KEYCLOAK_WAIT = buildKeycloakWait();

    private static final String WIREMOCK_SERVICE_NAME = "wiremock";
    private static final int WIREMOCK_PORT = 8080;

    public LocalEnvironment() {
        super(new File("src/integrationTest/resources/integration-test-docker-compose.yml"));
        withServices(KEYCLOAK_SERVICE_NAME, AWS_SERVICE_NAME, WIREMOCK_SERVICE_NAME);
        withLogConsumer(KEYCLOAK_SERVICE_NAME, new LogConsumer(KEYCLOAK_SERVICE_NAME));
        withLogConsumer(AWS_SERVICE_NAME, new LogConsumer(AWS_SERVICE_NAME));
        withLogConsumer(WIREMOCK_SERVICE_NAME, new LogConsumer(WIREMOCK_SERVICE_NAME));

        withExposedService(KEYCLOAK_SERVICE_NAME, KEYCLOAK_HTTP_PORT);
        withExposedService(KEYCLOAK_SERVICE_NAME, KEYCLOAK_HTTPS_PORT);
        withExposedService(AWS_SERVICE_NAME, AWS_PORT);
        withExposedService(WIREMOCK_SERVICE_NAME, WIREMOCK_PORT);
    }

    public String getAwsEndpointUri() {
        return toLocalEndpoint(AWS_SERVICE_NAME, AWS_PORT);
    }

    public String getKeycloakHttpAuthUri() {
        return toLocalEndpoint(KEYCLOAK_SERVICE_NAME, KEYCLOAK_HTTP_PORT);
    }

    public String getKeycloakHttpsAuthUri() {
        return toLocalEndpoint(KEYCLOAK_SERVICE_NAME, KEYCLOAK_HTTPS_PORT);
    }

    public String getWiremockEndpointUri() {
        return toLocalEndpoint(WIREMOCK_SERVICE_NAME, WIREMOCK_PORT);
    }

    public String toLocalEndpoint(String serviceName, int port) {
        String host = getServiceHost(serviceName, port);
        int servicePort = getServicePort(serviceName, port);
        return String.format("http://%s:%d", host, servicePort);
    }

    private static WaitStrategy buildKeycloakWait() {
        return Wait.forHttp("/auth/realms/demo-local/.well-known/openid-configuration")
                .withStartupTimeout(Duration.ofMinutes(4));
    }

}
