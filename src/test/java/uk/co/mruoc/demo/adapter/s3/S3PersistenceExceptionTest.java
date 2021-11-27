package uk.co.mruoc.demo.adapter.s3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class S3PersistenceExceptionTest {

    @Test
    void shouldReturnMessage() {
        String message = "error-message";

        Throwable error = new S3PersistenceException(message);

        assertThat(error.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldReturnCause() {
        Throwable cause = new Exception();

        Throwable error = new S3PersistenceException(cause);

        assertThat(error.getCause()).isEqualTo(cause);
    }

}