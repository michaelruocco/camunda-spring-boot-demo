package uk.co.mruoc.demo.security.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class LogoutSecurityConfig {

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(@Value("${spring.security.oauth2.client.provider.keycloak.logout-uri}") String logoutUri) {
        return new KeycloakLogoutHandler(logoutUri);
    }

}
