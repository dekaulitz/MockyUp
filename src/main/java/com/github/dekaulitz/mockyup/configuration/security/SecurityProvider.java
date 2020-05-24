package com.github.dekaulitz.mockyup.configuration.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityProvider extends AbstractUserDetailsAuthenticationProvider {
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        SecurityUsernameAuthenticationToken authenticationToken = (SecurityUsernameAuthenticationToken) usernamePasswordAuthenticationToken;
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN");
        return new AuthenticationProfileModel("5ec41562b5a0ae5108a31c1d", "fahmi", grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SecurityUsernameAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
