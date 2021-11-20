package uk.co.mruoc.demo.security.rest;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import uk.co.mruoc.demo.security.GroupQueryMother;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    private static final String AUTHENTICATION_NAME = "my-authentication-name";

    private final IdentityService identityService = mock(IdentityService.class);

    private final AuthenticationService authenticationService = new AuthenticationService(identityService);

    @Test
    void shouldThrowExceptionIfAuthenticationIsNotJwtAuthenticationToken() {
        Authentication authentication = givenAuthentication();

        Throwable error = catchThrowable(() -> authenticationService.setAuthentication(authentication));

        assertThat(error)
                .isInstanceOf(InvalidAuthenticationException.class)
                .hasMessage(AUTHENTICATION_NAME);
    }

    @Test
    void shouldThrowExceptionIfJwtClaimPreferredUsernameIsNull() {
        Jwt token = givenJwtTokenWithPreferredUsername(null);
        Authentication authentication = givenAuthenticationWithJwtToken(token);

        Throwable error = catchThrowable(() -> authenticationService.setAuthentication(authentication));

        assertThat(error)
                .isInstanceOf(InvalidAuthenticationException.class)
                .hasMessage(AUTHENTICATION_NAME);
    }

    @Test
    void shouldThrowExceptionIfJwtClaimPreferredUsernameIsEmpty() {
        Jwt token = givenJwtTokenWithPreferredUsername("");
        Authentication authentication = givenAuthenticationWithJwtToken(token);

        Throwable error = catchThrowable(() -> authenticationService.setAuthentication(authentication));

        assertThat(error)
                .isInstanceOf(InvalidAuthenticationException.class)
                .hasMessage(AUTHENTICATION_NAME);
    }

    @Test
    void shouldSetAuthenticationUsernameAndGroupIdsOnIdentityService() {
        String username = "my-username";
        Jwt token = givenJwtTokenWithPreferredUsername(username);
        Authentication authentication = givenAuthenticationWithJwtToken(token);
        List<String> groupIds = givenGroupIdsForUser(username);

        authenticationService.setAuthentication(authentication);

        verify(identityService).setAuthentication(username, groupIds);
    }

    @Test
    void shouldClearAuthentication() {
        authenticationService.clearAuthentication();

        verify(identityService).clearAuthentication();
    }

    private Authentication givenAuthentication() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(AUTHENTICATION_NAME);
        return authentication;
    }

    private Authentication givenAuthenticationWithJwtToken(Jwt token) {
        JwtAuthenticationToken authentication = mock(JwtAuthenticationToken.class);
        when(authentication.getName()).thenReturn(AUTHENTICATION_NAME);
        when(authentication.getToken()).thenReturn(token);
        return authentication;
    }

    private Jwt givenJwtTokenWithPreferredUsername(String preferredUsername) {
        Jwt token = mock(Jwt.class);
        when(token.getClaimAsString("preferred_username")).thenReturn(preferredUsername);
        return token;
    }

    private List<String> givenGroupIdsForUser(String username) {
        List<String> groupIds = Arrays.asList("group-id-1", "group-id-2");
        GroupQuery query = GroupQueryMother.groupQueryWithIdsForUser(username, groupIds);
        when(identityService.createGroupQuery()).thenReturn(query);
        return groupIds;
    }

}
