package com.github.dekaulitz.mockyup.server.controllers;

import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.request.IssuerRequestModel;
import javax.servlet.ServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

  @ModelAttribute
  public IssuerRequestModel getIssuerRequestModel(ServletRequest servletRequest) {
    return (IssuerRequestModel) servletRequest.getServletContext().getAttribute("issuer");
  }

  @ModelAttribute
  protected AuthProfileModel getAuthenticationProfileModel() {
    return (AuthProfileModel) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
  }
}
