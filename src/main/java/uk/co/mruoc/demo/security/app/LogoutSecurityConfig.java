package uk.co.mruoc.demo.security.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@Slf4j
@Profile("secure")
public class LogoutSecurityConfig {

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(@Value("${spring.security.oauth2.client.provider.keycloak.logout-uri}") String logoutUri) {
        log.info("configuring logout handler with uri {}", logoutUri);
        return new KeycloakLogoutHandler(logoutUri);
    }

}
