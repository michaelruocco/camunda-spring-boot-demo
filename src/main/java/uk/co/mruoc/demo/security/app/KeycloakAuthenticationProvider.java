package uk.co.mruoc.demo.security.app;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.co.mruoc.demo.security.UserGroupIdFinder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class KeycloakAuthenticationProvider extends ContainerBasedAuthenticationProvider {

    @Override
    public AuthenticationResult extractAuthenticatedUser(HttpServletRequest request, ProcessEngine engine) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<String> userId = UserIdExtractor.extractUserId(authentication);
        if (userId.isEmpty()) {
            return AuthenticationResult.unsuccessful();
        }
        return toResult(userId.get(), engine);
    }

    private AuthenticationResult toResult(String userId, ProcessEngine engine) {
        AuthenticationResult result = new AuthenticationResult(userId, true);
        result.setGroups(UserGroupIdFinder.findUserGroupIds(userId, engine.getIdentityService()));
        return result;
    }

}
