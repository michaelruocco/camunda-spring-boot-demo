
package uk.co.mruoc.demo;

import org.junit.jupiter.api.Test;
import uk.org.webcompere.systemstubs.ThrowingRunnable;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

import static org.assertj.core.api.Assertions.assertThatCode;
import static uk.org.webcompere.systemstubs.resource.Resources.with;

class ApplicationStubbedStartupIntegrationTest {

    @Test
    void applicationShouldStartWithStubbedProfile() {
        assertThatCode(this::startApplication).doesNotThrowAnyException();
    }

    private void startApplication() throws Exception {
        with(systemProperties()).execute((ThrowingRunnable) Application::main);
    }

    private SystemProperties systemProperties() {
        return new SystemProperties()
                .set("spring.profiles.active", "stubbed")
                .set("server.port", "0")
                .set("spring.autoconfigure.exclude", "org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration")
                .set("camunda.bpm.generate-unique-process-engine-name", "true")
                .set("camunda.bpm.generate-unique-process-application-name", "true");
    }

}
