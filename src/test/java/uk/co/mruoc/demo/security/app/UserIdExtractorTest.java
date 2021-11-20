package uk.co.mruoc.demo.security.app;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserIdExtractorTest {

    @Test
    void shouldReturnEmptyIfAuthenticationIsNotOAuth2AuthenticationToken() {
        Authentication authentication = mock(Authentication.class);

        Optional<String> userId = UserIdExtractor.extractUserId(authentication);

        assertThat(userId).isEmpty();
    }

    @Test
    void shouldReturnEmptyIfAuthenticationPrincipalIsNotOidcUser() {
        OAuth2User user = mock(OAuth2User.class);
        Authentication authentication = givenOAuth2AuthenticationTokenWithPrincipal(user);

        Optional<String> userId = UserIdExtractor.extractUserId(authentication);

        assertThat(userId).isEmpty();
    }

    @Test
    void shouldReturnEmptyIfAuthenticationOidcUserNameIsNull() {
        OidcUser principal = givenUserWithName(null);
        Authentication authentication = givenOAuth2AuthenticationTokenWithPrincipal(principal);

        Optional<String> userId = UserIdExtractor.extractUserId(authentication);

        assertThat(userId).isEmpty();
    }

    @Test
    void shouldReturnEmptyIfAuthenticationOidcUserNameIsEmpty() {
        OidcUser principal = givenUserWithName("");
        Authentication authentication = givenOAuth2AuthenticationTokenWithPrincipal(principal);

        Optional<String> userId = UserIdExtractor.extractUserId(authentication);

        assertThat(userId).isEmpty();
    }

    @Test
    void shouldReturnUserNameFromAuthenticationOidcUser() {
        String expectedUserName = "my-user-name";
        OidcUser principal = givenUserWithName(expectedUserName);
        Authentication authentication = givenOAuth2AuthenticationTokenWithPrincipal(principal);

        Optional<String> userId = UserIdExtractor.extractUserId(authentication);

        assertThat(userId).contains(expectedUserName);
    }

    private Authentication givenOAuth2AuthenticationTokenWithPrincipal(Object principal) {
        Authentication authentication = mock(OAuth2AuthenticationToken.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        return authentication;
    }

    private OidcUser givenUserWithName(String name) {
        OidcUser user = mock(OidcUser.class);
        when(user.getName()).thenReturn(name);
        return user;
    }

}
