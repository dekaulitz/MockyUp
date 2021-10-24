package com.github.dekaulitz.mockyup.server.configuration.security;

import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PreAuthenticationChecker implements UserDetailsChecker {

  /**
   * Examines the User
   *
   * @param toCheck the UserDetails instance whose status should be checked.
   */
  @Override
  public void check(UserDetails toCheck) {
    AuthProfileModel authProfileModel = (AuthProfileModel) toCheck;
    // @TODO this for pre authentication request need to do some enhancement relate security issues
  }
}
