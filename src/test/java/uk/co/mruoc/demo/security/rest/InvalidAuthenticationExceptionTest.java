package uk.co.mruoc.demo.security.rest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidAuthenticationExceptionTest {

    @Test
    void shouldReturnMessage() {
        String message = "my-message";

        Throwable error = new InvalidAuthenticationException(message);

        assertThat(error.getMessage()).isEqualTo(message);
    }

}
