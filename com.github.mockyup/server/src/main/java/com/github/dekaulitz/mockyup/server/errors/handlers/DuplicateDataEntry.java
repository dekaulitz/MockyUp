package com.github.dekaulitz.mockyup.server.errors.handlers;

import com.github.dekaulitz.mockyup.server.errors.vmodels.ErrorVmodel;
import lombok.Getter;

public class DuplicateDataEntry extends Exception {

  @Getter
  private final ErrorVmodel errorVmodel;

  public DuplicateDataEntry(ErrorVmodel errorVmodel) {
    super(errorVmodel.getErrorMessage());
    this.errorVmodel = errorVmodel;
  }

  public DuplicateDataEntry(ErrorVmodel errorVmodel, Exception e) {
    super(e.getMessage().isEmpty() ? errorVmodel.getErrorMessage() : e.getMessage());
    errorVmodel.setErrorMessage(this.getMessage());
    this.errorVmodel = errorVmodel;
  }
}
