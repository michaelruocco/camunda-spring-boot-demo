package uk.co.mruoc.demo.security.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthenticationFilter implements Filter {

    private final IdentityService identityService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = extractUsername(authentication);
            List<String> groups = getUserGroups(username);
            log.info("found groups {} for username {}", groups, username);
            identityService.setAuthentication(username, groups);
            chain.doFilter(request, response);
        } finally {
            identityService.clearAuthentication();
        }
    }

    private String extractUsername(Authentication authentication) {
        return Optional.of(authentication)
                .filter(JwtAuthenticationToken.class::isInstance)
                .map(JwtAuthenticationToken.class::cast)
                .map(AbstractOAuth2TokenAuthenticationToken::getToken)
                .map(token -> token.getClaimAsString("preferred_username"))
                .filter(username -> !StringUtils.isEmpty(username))
                .orElseThrow(() -> new InvalidAuthTokenException(authentication.getName()));
    }

    private List<String> getUserGroups(String userEmail) {
        return identityService.createGroupQuery()
                .groupMember(userEmail)
                .list()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());
    }

}
