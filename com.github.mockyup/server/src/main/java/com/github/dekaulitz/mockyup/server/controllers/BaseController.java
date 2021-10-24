package com.github.dekaulitz.mockyup.server.controllers;

import com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import javax.servlet.ServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
public class BaseController {

  @ModelAttribute
  public MandatoryModel getMandatory(ServletRequest servletRequest) {
    return (MandatoryModel) servletRequest.getServletContext().getAttribute(
        ApplicationConstants.MANDATORY);
  }

}
