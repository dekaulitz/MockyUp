package com.github.dekaulitz.mockyup.server.errors;

import com.github.dekaulitz.mockyup.server.model.dto.ErrorMessageModel;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

  @Getter
  private ErrorMessageModel errorMessagResponseModelError;

  public UnauthorizedException(String msg, Throwable cause) {
    super(msg, cause);
    this.errorMessagResponseModelError = ResponseCode.UNAUTHORIZED_ACCESS.getErrorMessageModel();
    this.errorMessagResponseModelError.setDescription(msg);
    this.errorMessagResponseModelError.setDetailMessage(cause.toString());
  }

  public UnauthorizedException(String msg) {
    super(msg);
    this.errorMessagResponseModelError = ResponseCode.UNAUTHORIZED_ACCESS.getErrorMessageModel();
    this.errorMessagResponseModelError.setDescription(msg);
  }

  public UnauthorizedException(ResponseCode responseCode) {
    super(responseCode.getErrorMessageModel().getDescription());
    this.errorMessagResponseModelError = responseCode.getErrorMessageModel();
  }
}
