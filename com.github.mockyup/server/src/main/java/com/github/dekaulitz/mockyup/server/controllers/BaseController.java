package com.github.dekaulitz.mockyup.server.controllers;

import com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants;
import com.github.dekaulitz.mockyup.server.model.dto.Mandatory;
import javax.servlet.ServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

  @ModelAttribute
  public Mandatory getIssuerRequestModel(ServletRequest servletRequest) {
    return (Mandatory) servletRequest.getServletContext().getAttribute(
        ApplicationConstants.MANDATORY);
  }

}
