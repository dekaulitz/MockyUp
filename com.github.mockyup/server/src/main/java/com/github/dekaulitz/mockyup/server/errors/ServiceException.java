package com.github.dekaulitz.mockyup.server.errors;

import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.ErrorMessageModel;
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

  public ServiceException(ResponseCode responseCode, String detailMessage) {
    super(responseCode.getErrorMessageModel().getDescription());
    this.errorMessagResponseModelError = responseCode.getErrorMessageModel();
    this.errorMessagResponseModelError.setDetailMessage(detailMessage);
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

  public ServiceException(ResponseCode responseCode, Throwable cause) {
    super(responseCode.getErrorMessageModel().getDescription(), cause);
    this.errorMessagResponseModelError = responseCode.getErrorMessageModel();
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
