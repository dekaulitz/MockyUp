package com.github.dekaulitz.mockyup.server.configuration.security;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;


public class SecurityAuthenticationToken extends UsernamePasswordAuthenticationToken {

  @Getter
  private String token;

  public SecurityAuthenticationToken(String token) {
    super(null, null);
    this.token = token;
  }

  public SecurityAuthenticationToken(Object principal, Object credentials) {
    super(principal, credentials);
  }

  public SecurityAuthenticationToken(Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }
}
