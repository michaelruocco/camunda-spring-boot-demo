package uk.co.mruoc.demo.security.app;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class KeycloakAuthenticationProvider extends ContainerBasedAuthenticationProvider {

    @Override
    public AuthenticationResult extractAuthenticatedUser(HttpServletRequest request, ProcessEngine engine) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2AuthenticationToken) || !(authentication.getPrincipal() instanceof OidcUser)) {
            return AuthenticationResult.unsuccessful();
        }
        String userId = ((OidcUser) authentication.getPrincipal()).getName();
        if (!StringUtils.hasLength(userId)) {
            return AuthenticationResult.unsuccessful();
        }
        AuthenticationResult authenticationResult = new AuthenticationResult(userId, true);
        authenticationResult.setGroups(getUserGroups(userId, engine));
        return authenticationResult;
    }

    private List<String> getUserGroups(String userId, ProcessEngine engine) {
        List<String> groupIds = new ArrayList<>();
        engine.getIdentityService().createGroupQuery().groupMember(userId).list().forEach(g -> groupIds.add(g.getId()));
        return groupIds;
    }

}
