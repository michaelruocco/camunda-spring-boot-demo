package uk.co.mruoc.demo.adapter.camunda;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SendExternalNotificationTest {

    private final VariableExtractor extractor = mock(VariableExtractor.class);

    private final SendExternalNotification sendNotification = new SendExternalNotification(extractor);

    @Test
    void shouldDoNothingIfIdDoesNotEndInNine() {
        DelegateExecution execution = givenExecutionWithId("111");

        ThrowingCallable call = () -> sendNotification.execute(execution);

        assertThatCode(call).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionIfIdEndsWithNine() {
        DelegateExecution execution = givenExecutionWithId("119");

        Throwable error = catchThrowable(() -> sendNotification.execute(execution));

        assertThat(error)
                .isInstanceOf(ExternalNotificationFailedException.class)
                .hasMessageEndingWith("119");
    }

    private DelegateExecution givenExecutionWithId(String id) {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(extractor.extractPaymentId(execution)).thenReturn(id);
        return execution;
    }

}
