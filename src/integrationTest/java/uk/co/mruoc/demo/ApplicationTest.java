package uk.co.mruoc.demo;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import uk.org.webcompere.systemstubs.ThrowingRunnable;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

import static org.assertj.core.api.Assertions.assertThatCode;
import static uk.org.webcompere.systemstubs.resource.Resources.with;

@Testcontainers
class ApplicationTest {

    @Container
    public static final LocalKeycloak KEYCLOAK = new LocalKeycloak();

    @Test
    void applicationShouldStart() {
        assertThatCode(this::startApplication).doesNotThrowAnyException();
    }

    private void startApplication() throws Exception {
        with(systemProperties()).execute((ThrowingRunnable) Application::main);
    }

    private SystemProperties systemProperties() {
        return new SystemProperties()
                .set("server.port", "0")
                .set("auth.server.http", KEYCLOAK.getHttpAuthUri())
                .set("auth.server.https", KEYCLOAK.getHttpsAuthUri())
                .set("auth.realm", "demo-local")
                .set("auth.client.id", "demo-client-id")
                .set("auth.client.secret", "demo-client-secret")
                .set("auth.admin.group", "camunda-admin");
    }

}
