package com.github.dekaulitz.mockyup.server.errors;

import com.github.dekaulitz.mockyup.server.model.embeddable.Message;
import com.github.dekaulitz.mockyup.server.model.response.ErrorResponse;
import lombok.Getter;

public class ServiceException extends Exception {

  @Getter
  private ErrorResponse serviceMessageError = new ErrorResponse();

  public ServiceException() {
  }

  public ServiceException(Message message) {
    super(message.getDescription());
  }

  public ServiceException(Message message, String detailMessage) {
    super(message.getDescription());
    ErrorResponse errorModel = new ErrorResponse();
    errorModel.setDescription(message.getDescription() + " : " + detailMessage);
    errorModel.setHttpCode(message.getHttpCode());
    errorModel.setStatusCode(message.getStatusCode());
    errorModel.setMessages(message.getMessages());
    errorModel.getMessages().forEach(translationsMessage -> {
      translationsMessage.setMessage(translationsMessage.getMessage());
    });
    this.serviceMessageError = errorModel;
  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }

  public ServiceException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
