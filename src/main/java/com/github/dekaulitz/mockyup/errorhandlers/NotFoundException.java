package com.github.dekaulitz.mockyup.errorhandlers;

import lombok.Getter;

public class NotFoundException extends Exception {
    @Getter
    private ErrorModel errorModel;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotFoundException(ErrorModel errorModel) {
        super(errorModel.getErrorMessage());
        this.errorModel = errorModel;
    }
}
