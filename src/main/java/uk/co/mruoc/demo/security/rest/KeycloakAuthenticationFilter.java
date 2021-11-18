package uk.co.mruoc.demo.security.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthenticationFilter implements Filter {

    private final AuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authenticationService.setAuthentication(authentication);
            chain.doFilter(request, response);
        } finally {
            authenticationService.clearAuthentication();
        }
    }

}
