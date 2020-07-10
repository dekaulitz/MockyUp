package com.github.dekaulitz.mockyup.infrastructure.configuration.security;

import com.github.dekaulitz.mockyup.infrastructure.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.infrastructure.db.repositories.UserRepository;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.utils.JwtManager;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Component
public class SecurityProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private final UserRepository userRepository;

    public SecurityProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        SecurityUsernameAuthenticationToken authenticationToken = (SecurityUsernameAuthenticationToken) usernamePasswordAuthenticationToken;
        try {
            AuthenticationProfileModel authenticationProfileModel = JwtManager.validateToken(authenticationToken.getToken());
            Optional<UserEntities> userEntities = this.userRepository.findById(authenticationProfileModel.get_id());
            if (!userEntities.isPresent()) throw new UnathorizedAccess(ResponseCode.USER_NOT_FOUND);
            authenticationProfileModel.setUsername(userEntities.get().getUsername());
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", userEntities.get().getAccessList()));
            authenticationProfileModel.setGrantedAuthorities(grantedAuthorities);
            return authenticationProfileModel;
        } catch (UnsupportedEncodingException e) {
            throw new UnathorizedAccess(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SecurityUsernameAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
