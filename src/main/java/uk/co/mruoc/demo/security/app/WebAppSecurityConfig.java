package uk.co.mruoc.demo.security.app;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.webapp.impl.security.auth.ContainerBasedAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.ForwardedHeaderFilter;

import javax.inject.Inject;
import java.util.Collections;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
@Slf4j
@Profile("secure")
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private LogoutSuccessHandler logoutHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .ignoringAntMatchers("/api/**", "/engine-rest/**")
                .and()
                .requestMatchers()
                .antMatchers("/**").and()
                .authorizeRequests(
                        authorizeRequests ->
                                authorizeRequests
                                        .antMatchers("/app/**", "/api/**", "/lib/**")
                                        .authenticated()
                                        .anyRequest()
                                        .permitAll()
                )
                .oauth2Login()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/app/**/logout"))
                .logoutSuccessHandler(logoutHandler);
    }

    @Bean
    public FilterRegistrationBean<ContainerBasedAuthenticationFilter> containerBasedAuthenticationFilter() {
        FilterRegistrationBean<ContainerBasedAuthenticationFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ContainerBasedAuthenticationFilter());
        bean.setInitParameters(Collections.singletonMap("authentication-provider", KeycloakAuthenticationProvider.class.getName()));
        bean.setOrder(101); // make sure the filter is registered after the Spring Security Filter Chain
        bean.addUrlPatterns("/app/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ForwardedHeaderFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    @Order(0)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
