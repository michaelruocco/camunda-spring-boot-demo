package uk.co.mruoc.demo.security.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserIdExtractor {

    public static Optional<String> extractUserId(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            log.info("invalid authentication type {}", authentication.getClass());
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof OidcUser)) {
            log.info("invalid principal type {}", principal.getClass());
            return Optional.empty();
        }

        String userId = ((OidcUser) authentication.getPrincipal()).getName();
        if (StringUtils.isEmpty(userId)) {
            log.info("empty user id found");
            return Optional.empty();
        }

        return Optional.of(userId);
    }

}
