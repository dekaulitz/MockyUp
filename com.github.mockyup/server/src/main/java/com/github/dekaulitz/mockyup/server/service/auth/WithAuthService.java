package com.github.dekaulitz.mockyup.server.service.auth;

import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WithAuthService {

  public AuthProfileModel getAuthProfile() {
    return (AuthProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
