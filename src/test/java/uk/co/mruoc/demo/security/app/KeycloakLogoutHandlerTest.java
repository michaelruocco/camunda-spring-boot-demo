package uk.co.mruoc.demo.security.app;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KeycloakLogoutHandlerTest {

    private static final String LOGOUT_URI = "http://localhost:8080/auth/realms/test-realm/protocol/openid-connect/logout";
    private static final String CAMUNDA_URI = "http://localhost:8083/camunda";

    private final RedirectStrategy redirectStrategy = mock(RedirectStrategy.class);

    private final LogoutSuccessHandler handler = new KeycloakLogoutHandler(redirectStrategy, LOGOUT_URI);

    @Test
    void shouldRedirectToLogoutUri() throws ServletException, IOException {
        HttpServletRequest request = givenRequestWithUrl(String.format("%s/app", CAMUNDA_URI));
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        handler.onLogoutSuccess(request, response, authentication);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(redirectStrategy).sendRedirect(eq(request), eq(response), captor.capture());
        assertThat(captor.getValue()).isEqualTo(String.format("%s?redirect_uri=%s", LOGOUT_URI, CAMUNDA_URI));
    }

    private HttpServletRequest givenRequestWithUrl(String uri) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer(uri));
        return request;
    }

}
