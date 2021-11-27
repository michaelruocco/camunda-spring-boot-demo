package uk.co.mruoc.demo.adapter.s3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidUriExceptionTest {

    @Test
    void shouldReturnMessage() {
        String uri = "invalid-uri";

        Throwable error = new InvalidUriException(uri);

        assertThat(error.getMessage()).isEqualTo("invalid endpoint override value %s", uri);
    }

}