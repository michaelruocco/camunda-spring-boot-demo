package uk.co.mruoc.demo.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.camunda.bpm.extension.keycloak.plugin.KeycloakIdentityProviderPlugin;

@Component
@ConfigurationProperties(prefix="plugin.identity.keycloak")
@Profile("secure")
public class KeycloakIdentityProvider extends KeycloakIdentityProviderPlugin {

    // intentionally blank

}
