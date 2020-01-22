package com.github.dekaulitz.mockyup.errorHandlers;

public class NotFounException extends RuntimeException {
    public NotFounException() {
    }

    public NotFounException(String message) {
        super(message);
    }

    public NotFounException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFounException(Throwable cause) {
        super(cause);
    }

    public NotFounException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
