package uk.co.mruoc.demo.security.rest;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AudienceValidatorTest {

    private static final String AUDIENCE = "test-audience";

    private final AudienceValidator validator = new AudienceValidator(AUDIENCE);

    @Test
    void shouldReturnResultWithNoErrorsIfTokenAudienceMatches() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getAudience()).thenReturn(Collections.singletonList(AUDIENCE));

        OAuth2TokenValidatorResult result = validator.validate(jwt);

        assertThat(result.getErrors()).isEmpty();
    }

    @Test
    void shouldReturnResultWithErrorsIfTokenAudienceDoesNotMatch() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getAudience()).thenReturn(Collections.emptyList());

        OAuth2TokenValidatorResult result = validator.validate(jwt);

        OAuth2Error expectedError = new OAuth2Error("invalid_token", "required audience test-audience missing", null);
        assertThat(result.getErrors())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(expectedError);
    }

}
