package com.github.dekaulitz.mockyup.server.errors;

import com.github.dekaulitz.mockyup.server.model.embeddable.Message;
import com.github.dekaulitz.mockyup.server.model.response.ErrorResponse;
import lombok.Getter;

//@TODO need do some enhancement relate on response message
public class ServiceException extends Exception {

  @Getter
  private ErrorResponse serviceMessageError = new ErrorResponse();

  public ServiceException() {
  }

  public ServiceException(Message message) {
    super(message.getDescription());
    ErrorResponse errorModel = new ErrorResponse();
    errorModel.setDescription(message.getDescription());
    errorModel.setHttpCode(message.getHttpCode());
    errorModel.setStatusCode(message.getStatusCode());
    errorModel.setMessages(message.getMessages());
    errorModel.getMessages().forEach(translationsMessage -> {
      translationsMessage.setMessage(translationsMessage.getMessage());
    });
    this.serviceMessageError = errorModel;
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

  public ServiceException(Message message, Throwable cause) {
    super(message.getDescription(), cause);
    ErrorResponse errorModel = new ErrorResponse();
    errorModel.setDescription(message.getDescription());
    errorModel.setHttpCode(message.getHttpCode());
    errorModel.setStatusCode(message.getStatusCode());
    errorModel.setMessages(message.getMessages());
    errorModel.getMessages().forEach(translationsMessage -> {
      translationsMessage.setMessage(translationsMessage.getMessage());
    });
    this.serviceMessageError = errorModel;
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }

  public ServiceException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
