package com.github.dekaulitz.mockyup.server.tmp.errors.handlers;

import com.github.dekaulitz.mockyup.server.tmp.errors.vmodel.vmodels.ErrorVmodel;
import lombok.Getter;

public class InvalidMockException extends Exception {

  @Getter
  private final ErrorVmodel errorVmodel;

  public InvalidMockException(ErrorVmodel errorVmodel) {
    super(errorVmodel.getErrorMessage());
    this.errorVmodel = errorVmodel;
  }

  public InvalidMockException(ErrorVmodel errorVmodel, Exception e) {
    super(e.getMessage().isEmpty() ? errorVmodel.getErrorMessage() : e.getMessage());
    errorVmodel.setErrorMessage(this.getMessage());
    this.errorVmodel = errorVmodel;
  }
}
