package uk.co.mruoc.demo.adapter.s3;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class S3ClientConfigurerTest {

    private static final String VALID_REGION = "eu-west-1";

    private final AwsCredentialsProvider credentialsProvider = mock(AwsCredentialsProvider.class);
    private final S3AsyncClientBuilder clientBuilder = mock(S3AsyncClientBuilder.class);

    private final S3Config.S3ConfigBuilder configBuilder = S3Config.builder()
            .region(VALID_REGION)
            .credentialsProvider(credentialsProvider);

    @Test
    void shouldConfigureRegionAndCredentialsProviderIfEndpointOverrideNotSet() {
        S3AsyncClientBuilder builderWithRegion = givenConfiguredWithRegion(clientBuilder);
        S3AsyncClientBuilder builderWithCredentials = givenConfiguredWithCredentials(builderWithRegion);
        S3Config config = configBuilder.build();
        S3ClientConfigurer<S3AsyncClientBuilder, S3AsyncClient> configurer = new S3ClientConfigurer<>(config);

        S3AsyncClientBuilder configuredClientBuilder = configurer.configure(clientBuilder);


        assertThat(configuredClientBuilder).isEqualTo(builderWithCredentials);
    }

    @Test
    void shouldConfigureRegionAndCredentialsAndEndpointOverride() throws URISyntaxException {
        URI endpointOverride = new URI("http://localhost:1234");
        S3AsyncClientBuilder builderWithRegion = givenConfiguredWithRegion(clientBuilder);
        S3AsyncClientBuilder builderWithCredentials = givenConfiguredWithCredentials(builderWithRegion);
        S3AsyncClientBuilder builderWithEndpointOverride = givenConfiguredWithEndpointOverride(builderWithCredentials, endpointOverride);
        S3Config config = configBuilder.endpointOverride(endpointOverride.toString()).build();
        S3ClientConfigurer<S3AsyncClientBuilder, S3AsyncClient> configurer = new S3ClientConfigurer<>(config);

        S3AsyncClientBuilder configuredClientBuilder = configurer.configure(clientBuilder);

        assertThat(configuredClientBuilder).isEqualTo(builderWithEndpointOverride);
    }

    @Test
    void shouldThrowExceptionIfRegionIsEmpty() {
        S3Config config = configBuilder.region("").build();
        S3ClientConfigurer<S3AsyncClientBuilder, S3AsyncClient> configurer = new S3ClientConfigurer<>(config);

        Throwable error = catchThrowable(() -> configurer.configure(clientBuilder));

        assertThat(error)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("region must not be blank or empty.");
    }

    @Test
    void shouldThrowExceptionIfEndpointOverrideIsNotValidUri() {
        String endpointOverride = ":invalid-uri";
        S3AsyncClientBuilder builderWithRegion = givenConfiguredWithRegion(clientBuilder);
        givenConfiguredWithCredentials(builderWithRegion);
        S3Config config = configBuilder.endpointOverride(endpointOverride).build();
        S3ClientConfigurer<S3AsyncClientBuilder, S3AsyncClient> configurer = new S3ClientConfigurer<>(config);

        Throwable error = catchThrowable(() -> configurer.configure(clientBuilder));

        assertThat(error)
                .isInstanceOf(InvalidUriException.class)
                .hasCauseInstanceOf(URISyntaxException.class)
                .hasMessage("java.net.URISyntaxException: Expected scheme name at index 0: :invalid-uri");
    }

    private S3AsyncClientBuilder givenConfiguredWithEndpointOverride(S3AsyncClientBuilder builder, URI endpointOverride) {
        S3AsyncClientBuilder builderWithOverride = mock(S3AsyncClientBuilder.class);
        when(builder.endpointOverride(endpointOverride)).thenReturn(builderWithOverride);
        return builderWithOverride;
    }

    private S3AsyncClientBuilder givenConfiguredWithRegion(S3AsyncClientBuilder builder) {
        S3AsyncClientBuilder builderWithRegion = mock(S3AsyncClientBuilder.class);
        when(builder.region(Region.of(VALID_REGION))).thenReturn(builderWithRegion);
        return builderWithRegion;
    }

    private S3AsyncClientBuilder givenConfiguredWithCredentials(S3AsyncClientBuilder builder) {
        S3AsyncClientBuilder builderWithCredentials = mock(S3AsyncClientBuilder.class);
        when(builder.credentialsProvider(credentialsProvider)).thenReturn(builderWithCredentials);
        return builderWithCredentials;
    }

}
