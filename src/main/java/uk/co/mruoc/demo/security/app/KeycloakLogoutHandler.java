package uk.co.mruoc.demo.security.app;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Component
public class KeycloakLogoutHandler implements LogoutSuccessHandler {

    private final RedirectStrategy redirectStrategy;
    private final String logoutUri;

    public KeycloakLogoutHandler(@Value("${spring.security.oauth2.client.provider.keycloak.authorization-uri}") String authUri) {
        this(new DefaultRedirectStrategy(), toLogoutUri(authUri).orElse(null));
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Optional<String> uri = Optional.ofNullable(logoutUri);
        if (uri.isEmpty()) {
            log.info("attempting to logout uri is empty so returning");
            return;
        }
        String redirectUri = toRedirectUri(uri.get(), request);
        log.debug("redirecting to logout uri {} from {}", redirectUri, logoutUri);
        redirectStrategy.sendRedirect(request, response, redirectUri);
    }

    private static Optional<String> toLogoutUri(String authUri) {
        return Optional.ofNullable(authUri).map(uri -> uri.replace("openid-connect/auth", "openid-connect/logout"));
    }

    private static String toRedirectUri(String uri, HttpServletRequest request) {
        String requestUri = request.getRequestURL().toString();
        String redirectUri = requestUri.substring(0, requestUri.indexOf("/app"));
        return String.format("%s?redirect_uri=%s", uri, redirectUri);
    }

}
