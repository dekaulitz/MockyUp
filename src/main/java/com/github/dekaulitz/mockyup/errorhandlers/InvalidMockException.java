package com.github.dekaulitz.mockyup.errorhandlers;

import lombok.Getter;

public class InvalidMockException extends Exception {
    @Getter
    private ErrorModel errorModel;

    public InvalidMockException() {
    }

    public InvalidMockException(String message) {
        super(message);
    }

    public InvalidMockException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMockException(Throwable cause) {
        super(cause);
    }

    public InvalidMockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidMockException(ErrorModel errorModel) {
        super(errorModel.getErrorMessage());
        this.errorModel = errorModel;
    }
}
