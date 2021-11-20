package uk.co.mruoc.demo.security.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class KeycloakLogoutHandler implements LogoutSuccessHandler {

    private final RedirectStrategy redirectStrategy;
    private final String logoutUri;

    public KeycloakLogoutHandler(String logoutUri) {
        this(new DefaultRedirectStrategy(), logoutUri);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String redirectUri = toRedirectUri(request);
        log.debug("redirecting to logout uri {}", redirectUri);
        redirectStrategy.sendRedirect(request, response, redirectUri);
    }

    private String toRedirectUri(HttpServletRequest request) {
        String requestUri = request.getRequestURL().toString();
        String redirectUri = requestUri.substring(0, requestUri.indexOf("/app"));
        return String.format("%s?redirect_uri=%s", logoutUri, redirectUri);
    }

}
