package com.github.dekaulitz.mockyup.server.errors;

import com.github.dekaulitz.mockyup.server.model.dto.ErrorMessageModel;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import lombok.Getter;

//@TODO need do some enhancement relate on response message
public class ServiceException extends Exception {

  @Getter
  private ErrorMessageModel errorMessagResponseModelError;

  public ServiceException() {
  }

  public ServiceException(ResponseCode responseCode) {
    super(responseCode.getErrorMessageModel().getDescription());
    this.errorMessagResponseModelError = responseCode.getErrorMessageModel();
  }

  public ServiceException(ErrorMessageModel errorMessagResponseModelError) {
    super(errorMessagResponseModelError.getDescription());
    this.errorMessagResponseModelError = errorMessagResponseModelError;
  }

  public ServiceException(ErrorMessageModel errorMessagResponseModelError, String detailMessage) {
    super(errorMessagResponseModelError.getDescription());
    this.errorMessagResponseModelError = errorMessagResponseModelError;
    this.errorMessagResponseModelError.setDetailMessage(detailMessage);
  }

  public ServiceException(String messageError) {
    super(messageError);
  }

  public ServiceException(ErrorMessageModel errorMessagResponseModelError, Throwable cause) {
    super(errorMessagResponseModelError.getDescription(), cause);
    this.errorMessagResponseModelError = errorMessagResponseModelError;
    this.errorMessagResponseModelError.setDetailMessage(cause.toString());
  }

  public ServiceException(Throwable cause) {
    super(cause);
    this.errorMessagResponseModelError = ResponseCode.INTERNAL_SERVER_ERROR.getErrorMessageModel();
    this.errorMessagResponseModelError.setDetailMessage(cause.toString());
  }

  public ServiceException(String messageError, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(messageError, cause, enableSuppression, writableStackTrace);
    this.errorMessagResponseModelError = ResponseCode.INTERNAL_SERVER_ERROR.getErrorMessageModel();
    this.errorMessagResponseModelError.setDescription(messageError);
    this.errorMessagResponseModelError.setDetailMessage(cause.toString());
  }
}
