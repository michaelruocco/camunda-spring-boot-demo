package uk.co.mruoc.demo.security.app;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationProvider;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import uk.co.mruoc.demo.security.GroupQueryMother;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KeycloakAuthenticationProviderTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final IdentityService identityService = mock(IdentityService.class);

    private final AuthenticationProvider provider = new KeycloakAuthenticationProvider();

    @Test
    void shouldNotBeAuthenticatedIfUserIdIsNull() {
        SecurityContext context = SecurityContextHolder.getContext();
        try {
            Authentication authentication = mock(Authentication.class);
            context.setAuthentication(authentication);
            ProcessEngine engine = givenProcessEngineWithIdentityService();

            AuthenticationResult result = provider.extractAuthenticatedUser(request, engine);

            assertThat(result.isAuthenticated()).isFalse();
            assertThat(result.getAuthenticatedUser()).isNull();
            assertThat(result.getGroups()).isNull();
        } finally {
            context.setAuthentication(null);
        }
    }

    @Test
    void shouldPopulateGroupIdsWhenAuthenticated() {
        SecurityContext context = SecurityContextHolder.getContext();
        try {
            String username = "my-username";
            OAuth2User principal = givenPrincipalWithUsername(username);
            Authentication authentication = givenOAuth2AuthenticationTokenWithPrincipal(principal);
            context.setAuthentication(authentication);
            ProcessEngine engine = givenProcessEngineWithIdentityService();
            List<String> groupIds = givenGroupIdsForUser(username);

            AuthenticationResult result = provider.extractAuthenticatedUser(request, engine);

            assertThat(result.isAuthenticated()).isTrue();
            assertThat(result.getAuthenticatedUser()).isEqualTo(username);
            assertThat(result.getGroups()).containsExactlyElementsOf(groupIds);
        } finally {
            context.setAuthentication(null);
        }
    }

    private ProcessEngine givenProcessEngineWithIdentityService() {
        ProcessEngine engine = mock(ProcessEngine.class);
        when(engine.getIdentityService()).thenReturn(identityService);
        return engine;
    }

    private OAuth2User givenPrincipalWithUsername(String username) {
        OAuth2User principal = mock(OidcUser.class);
        when(principal.getName()).thenReturn(username);
        return principal;
    }

    private Authentication givenOAuth2AuthenticationTokenWithPrincipal(OAuth2User principal) {
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        return authentication;
    }

    private List<String> givenGroupIdsForUser(String username) {
        List<String> groupIds = Arrays.asList("group-id-1", "group-id-2");
        GroupQuery query = GroupQueryMother.groupQueryWithIdsForUser(username, groupIds);
        when(identityService.createGroupQuery()).thenReturn(query);
        return groupIds;
    }

}
