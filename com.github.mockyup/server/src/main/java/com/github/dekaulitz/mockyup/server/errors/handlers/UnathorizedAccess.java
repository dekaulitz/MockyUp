package com.github.dekaulitz.mockyup.server.errors.handlers;

import com.github.dekaulitz.mockyup.server.errors.vmodels.ErrorVmodel;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class UnathorizedAccess extends AuthenticationException {

  @Getter
  private final ErrorVmodel errorVmodel;


  public UnathorizedAccess(ErrorVmodel errorVmodel) {
    super(errorVmodel.getErrorMessage());
    this.errorVmodel = errorVmodel;
  }

  public UnathorizedAccess(ErrorVmodel errorVmodel, Exception e) {
    super(e.getMessage().isEmpty() ? errorVmodel.getErrorMessage() : e.getMessage());
    errorVmodel.setErrorMessage(this.getMessage());
    this.errorVmodel = errorVmodel;
  }
}
