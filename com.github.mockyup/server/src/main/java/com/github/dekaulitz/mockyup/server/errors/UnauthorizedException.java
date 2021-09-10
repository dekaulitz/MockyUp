package com.github.dekaulitz.mockyup.server.errors;

import com.github.dekaulitz.mockyup.server.model.response.ErrorResponse;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

  @Getter
  private ErrorResponse serviceMessageError = new ErrorResponse();

  public UnauthorizedException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public UnauthorizedException(String msg) {
    super(msg);
  }

  public UnauthorizedException(ResponseCode responseCode) {
    super(responseCode.getValue().getDescription());
    ErrorResponse errorModel = new ErrorResponse();
    errorModel.setDescription(responseCode.getValue().getDescription());
    errorModel.setHttpCode(responseCode.getValue().getHttpCode());
    errorModel.setStatusCode(responseCode.getValue().getStatusCode());
    errorModel.setMessages(responseCode.getValue().getMessages());
    errorModel.getMessages().forEach(translationsMessage -> {
      translationsMessage.setMessage(translationsMessage.getMessage());
    });
    this.serviceMessageError = errorModel;
  }
}
