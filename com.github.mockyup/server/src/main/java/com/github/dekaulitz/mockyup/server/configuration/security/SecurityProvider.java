package com.github.dekaulitz.mockyup.server.configuration.security;

import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.service.auth.api.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
public class SecurityProvider extends AbstractUserDetailsAuthenticationProvider {

  @Autowired
  private JwtService jwtService;

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {

  }

  @Override
  protected UserDetails retrieveUser(String s,
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {
    SecurityAuthenticationToken authenticationToken = (SecurityAuthenticationToken) usernamePasswordAuthenticationToken;
    AuthProfileModel authProfileModel = jwtService.validateToken(authenticationToken.getToken());
    return authProfileModel;
  }
}
