package com.github.dekaulitz.mockyup.errorhandlers;

import com.github.dekaulitz.mockyup.vmodels.ErrorVmodel;
import lombok.Getter;

public class InvalidMockException extends Exception {
    @Getter
    private ErrorVmodel errorVmodel;

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

    public InvalidMockException(ErrorVmodel errorVmodel) {
        super(errorVmodel.getErrorMessage());
        this.errorVmodel = errorVmodel;
    }

    public InvalidMockException(ErrorVmodel errorVmodel, Exception e) {
        super(e.getMessage().isEmpty() ? errorVmodel.getErrorMessage() : e.getMessage());
        errorVmodel.setErrorMessage(this.getMessage());
        this.errorVmodel = errorVmodel;
    }
}
