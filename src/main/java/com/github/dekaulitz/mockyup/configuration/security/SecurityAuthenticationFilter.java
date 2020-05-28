package com.github.dekaulitz.mockyup.configuration.security;

import com.github.dekaulitz.mockyup.errorhandlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public SecurityAuthenticationFilter() {
        super("/mocks/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String authenticationHeader = request.getHeader("Authorization");
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer"))
            throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
        String token = authenticationHeader.substring(7);
        SecurityUsernameAuthenticationToken auth = new SecurityUsernameAuthenticationToken(token);
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw failed;
    }
}
