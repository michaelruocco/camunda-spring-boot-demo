package uk.co.mruoc.demo.security.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import uk.co.mruoc.demo.security.UserGroupIdFinder;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final IdentityService identityService;

    public void setAuthentication(Authentication authentication) {
        String username = extractUsername(authentication);
        List<String> groups = UserGroupIdFinder.findUserGroupIds(username, identityService);
        log.info("found groups {} for username {}", groups, username);
        identityService.setAuthentication(username, groups);
    }

    public void clearAuthentication() {
        identityService.clearAuthentication();
    }

    private String extractUsername(Authentication authentication) {
        return Optional.of(authentication)
                .filter(JwtAuthenticationToken.class::isInstance)
                .map(JwtAuthenticationToken.class::cast)
                .map(AbstractOAuth2TokenAuthenticationToken::getToken)
                .map(token -> token.getClaimAsString("preferred_username"))
                .filter(username -> !StringUtils.isEmpty(username))
                .orElseThrow(() -> new InvalidAuthenticationException(authentication.getName()));
    }

}
