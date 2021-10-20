package com.github.dekaulitz.mockyup.server.configuration.security;

import com.github.dekaulitz.mockyup.server.errors.UnauthorizedException;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


public class SecurityAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public SecurityAuthenticationFilter() {
    super("/mocks/**");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse)
      throws AuthenticationException, IOException, ServletException {
    String authenticationHeader = httpServletRequest.getHeader("Authorization");
    if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer")) {
      throw new UnauthorizedException(ResponseCode.TOKEN_NOT_VALID);
    }
    if (authenticationHeader.length() <= 7) {
      throw new UnauthorizedException(ResponseCode.TOKEN_NOT_VALID);
    }
    String token = authenticationHeader.substring(7);
    SecurityAuthenticationToken auth = new SecurityAuthenticationToken(token);
    return getAuthenticationManager().authenticate(auth);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    throw failed;
  }
}
