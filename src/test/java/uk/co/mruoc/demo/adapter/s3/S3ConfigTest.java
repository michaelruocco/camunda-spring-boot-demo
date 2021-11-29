package uk.co.mruoc.demo.adapter.s3;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class S3ConfigTest {

    private final S3Config.S3ConfigBuilder configBuilder = S3Config.builder();

    @Test
    void shouldReturnEmptyEndpointOverrideWithValueIsNull() {
        S3Config config = configBuilder.endpointOverride(null).build();

        Optional<String> endpointOverride = config.getEndpointOverride();

        assertThat(endpointOverride).isEmpty();
    }

    @Test
    void shouldReturnEmptyEndpointOverrideWithValueIsEmpty() {
        S3Config config = configBuilder.endpointOverride("").build();

        Optional<String> endpointOverride = config.getEndpointOverride();

        assertThat(endpointOverride).isEmpty();
    }

    @Test
    void shouldReturnEmptyEndpointOverrideWithValueIsSet() {
        String expectedEndpointOverride = "endpoint-override";
        S3Config config = configBuilder.endpointOverride(expectedEndpointOverride).build();

        Optional<String> endpointOverride = config.getEndpointOverride();

        assertThat(endpointOverride).contains(expectedEndpointOverride);
    }

}
