package com.github.dekaulitz.mockyup.server.service.auth;

import com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import javax.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WithAuthService {

  @Autowired
  private ServletRequest servletRequest;

  public AuthProfileModel getAuthProfile() {
    return (AuthProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public MandatoryModel getMandatory() {
    return (MandatoryModel) servletRequest.getServletContext().getAttribute(
        ApplicationConstants.MANDATORY);
  }
}
