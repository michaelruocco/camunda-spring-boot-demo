package uk.co.mruoc.demo.security.rest;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class KeycloakAuthenticationFilterTest {

    private final AuthenticationService authenticationService = mock(AuthenticationService.class);
    private final ServletRequest request = mock(ServletRequest.class);
    private final ServletResponse response = mock(ServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final Filter filter = new KeycloakAuthenticationFilter(authenticationService);

    @Test
    void shouldSetAuthenticationCallFilterChainAndClearAuthentication() throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        try {
            Authentication authentication = mock(Authentication.class);
            context.setAuthentication(authentication);

            filter.doFilter(request, response, chain);

            InOrder inOrder = inOrder(authenticationService, chain);
            inOrder.verify(authenticationService).setAuthentication(authentication);
            inOrder.verify(chain).doFilter(request, response);
            inOrder.verify(authenticationService).clearAuthentication();
        } finally {
            context.setAuthentication(null);
        }
    }

    @Test
    void shouldClearAuthenticationIfExceptionIsThrown() {
        SecurityContext context = SecurityContextHolder.getContext();
        try {
            Authentication authentication = mock(Authentication.class);
            context.setAuthentication(authentication);
            doThrow(InvalidAuthenticationException.class).when(authenticationService).setAuthentication(authentication);

            Throwable error = catchThrowable(() -> filter.doFilter(request, response, chain));

            assertThat(error).isInstanceOf(InvalidAuthenticationException.class);
            verify(authenticationService).clearAuthentication();
            verifyNoInteractions(chain);
        } finally {
            context.setAuthentication(null);
        }
    }

}
