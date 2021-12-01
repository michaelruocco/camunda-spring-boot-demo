package uk.co.mruoc.demo.adapter.camunda;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.demo.domain.service.ExternalNotificationFailedException;

import static org.assertj.core.api.Assertions.assertThat;

class ExternalNotificationFailedExceptionTest {

    @Test
    void shouldReturnMessage() {
        String id = "my-id";

        Throwable error = new ExternalNotificationFailedException(id);

        assertThat(error.getMessage()).isEqualTo("failed to send external notification for %s", id);
    }

}
