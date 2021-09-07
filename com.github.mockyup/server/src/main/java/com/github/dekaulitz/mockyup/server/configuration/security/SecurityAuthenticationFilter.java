package com.github.dekaulitz.mockyup.server.configuration.security;

import com.github.dekaulitz.mockyup.server.tmp.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.server.utils.ResponseCode;
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
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    String authenticationHeader = request.getHeader("Authorization");
    if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer")) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
    if (authenticationHeader.length() <= 7) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
    String token = authenticationHeader.substring(7);
    SecurityUsernameAuthenticationToken auth = new SecurityUsernameAuthenticationToken(token);
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
