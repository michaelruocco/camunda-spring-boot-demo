package uk.co.mruoc.demo.adapter.s3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidUriExceptionTest {

    @Test
    void shouldReturnCause() {
        Throwable cause = new Exception();

        Throwable error = new InvalidUriException(cause);

        assertThat(error.getCause()).isEqualTo(cause);
    }

}
