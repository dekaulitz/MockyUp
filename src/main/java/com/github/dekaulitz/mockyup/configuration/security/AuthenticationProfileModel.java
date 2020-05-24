package com.github.dekaulitz.mockyup.configuration.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthenticationProfileModel implements UserDetails {

    private String _id;
    private final String username;
    private final List<GrantedAuthority> grantedAuthorities;

    public AuthenticationProfileModel(String userId, String username, List<GrantedAuthority> grantedAuthorities) {
        this.username = username;
        this.grantedAuthorities = grantedAuthorities;
        this._id = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
