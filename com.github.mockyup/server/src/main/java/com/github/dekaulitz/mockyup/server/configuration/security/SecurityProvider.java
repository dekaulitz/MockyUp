package com.github.dekaulitz.mockyup.server.configuration.security;

import com.github.dekaulitz.mockyup.server.configuration.jwt.JwtManager;
import com.github.dekaulitz.mockyup.server.configuration.jwt.JwtProfileModel;
import com.github.dekaulitz.mockyup.server.db.entities.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.db.repositories.UserRepository;
import com.github.dekaulitz.mockyup.server.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.server.utils.ResponseCode;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityProvider extends AbstractUserDetailsAuthenticationProvider {

  @Autowired
  private final UserRepository userRepository;
  @Autowired
  private final JwtManager jwtManager;

  public SecurityProvider(UserRepository userRepository,
      JwtManager jwtManager) {
    this.userRepository = userRepository;
    this.jwtManager = jwtManager;
  }

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {

  }

  @Override
  protected UserDetails retrieveUser(String s,
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {
    SecurityUsernameAuthenticationToken authenticationToken = (SecurityUsernameAuthenticationToken) usernamePasswordAuthenticationToken;
    JwtProfileModel jwtProfileModel = jwtManager
        .validateToken(authenticationToken.getToken());
    Optional<UserEntities> userEntities = this.userRepository
        .findById(jwtProfileModel.getId());
    if (!userEntities.isPresent()) {
      throw new UnathorizedAccess(ResponseCode.USER_NOT_FOUND);
    }
    jwtProfileModel.setUsername(userEntities.get().getUsername());
    List<GrantedAuthority> grantedAuthorities = AuthorityUtils
        .commaSeparatedStringToAuthorityList(String.join(",", userEntities.get().getAccessList()));
    jwtProfileModel.setGrantedAuthorities(grantedAuthorities);
    return jwtProfileModel;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return SecurityUsernameAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
