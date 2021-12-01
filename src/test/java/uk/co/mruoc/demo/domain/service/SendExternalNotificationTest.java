
package uk.co.mruoc.demo.domain.service;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

class SendExternalNotificationTest {

    private final SendExternalNotification sendNotification = new SendExternalNotification();

    @Test
    void shouldDoNothingIfIdDoesNotEndInNine() {
        String paymentId = "111";

        ThrowingCallable call = () -> sendNotification.send(paymentId);

        assertThatCode(call).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionIfIdEndsWithNine() {
        String paymentId = "119";

        Throwable error = catchThrowable(() -> sendNotification.send(paymentId));

        assertThat(error)
                .isInstanceOf(ExternalNotificationFailedException.class)
                .hasMessageEndingWith("119");
    }

}
