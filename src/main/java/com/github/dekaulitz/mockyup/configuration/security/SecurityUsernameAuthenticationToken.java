package com.github.dekaulitz.mockyup.configuration.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SecurityUsernameAuthenticationToken extends UsernamePasswordAuthenticationToken {

    @Getter
    private String token;

    public SecurityUsernameAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }

    public SecurityUsernameAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public SecurityUsernameAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
