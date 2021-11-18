package uk.co.mruoc.demo.security.rest;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER - 20)
@ConditionalOnProperty(name = "rest.security.enabled", havingValue = "true", matchIfMissing = true)
public class RestApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private RestApiSecurityConfigProperties properties;

    @Inject
    private ApplicationContext applicationContext;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .ignoringAntMatchers("/payments/**", "/engine-rest/**")
                .and()
                .requestMatchers()
                .antMatchers("/payments/**", "/engine-rest/**")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwkSetUri(loadJwkSetUri());
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String issuerUri = loadIssuerUri();
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(properties.getRequiredAudience());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }

    @Bean
    public AuthenticationService authenticationService(IdentityService identityService) {
        return new AuthenticationService(identityService);
    }

    @Bean
    public FilterRegistrationBean<KeycloakAuthenticationFilter> keycloakAuthenticationFilter(AuthenticationService authenticationService) {
        FilterRegistrationBean<KeycloakAuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new KeycloakAuthenticationFilter(authenticationService));
        filterRegistration.setOrder(102); // make sure the filter is registered after the Spring Security Filter Chain
        filterRegistration.addUrlPatterns("/payments/*");
        return filterRegistration;
    }

    private String loadJwkSetUri() {
        return loadOAuth2Property("jwk-set-uri");
    }

    private String loadIssuerUri() {
        return loadOAuth2Property("issuer-uri");
    }

    private String loadOAuth2Property(String suffix) {
        String key = String.format("spring.security.oauth2.client.provider.%s.%s", properties.getProvider(), suffix);
        return applicationContext.getEnvironment().getRequiredProperty(key);
    }

}
