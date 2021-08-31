package com.github.dekaulitz.mockyup.server.errors;

import com.github.dekaulitz.mockyup.server.model.embeddable.Message;
import lombok.Getter;

public class ServiceException extends Exception {

  @Getter
  private Message serviceMessageError;

  public ServiceException() {
  }

  public ServiceException(Message message) {
    super(message.getDescription());
    this.serviceMessageError = message;
  }

  public ServiceException(Message message, String detailMessage) {
    super(message.getDescription());
    message.getMessages().forEach(translationsMessage -> {
      translationsMessage.setMessage(translationsMessage.getMessage() + " : " + detailMessage);
    });
    this.serviceMessageError = message;
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
