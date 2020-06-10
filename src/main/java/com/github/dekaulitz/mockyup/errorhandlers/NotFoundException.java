package com.github.dekaulitz.mockyup.errorhandlers;

import com.github.dekaulitz.mockyup.vmodels.ErrorVmodel;
import lombok.Getter;

public class NotFoundException extends Exception {
    @Getter
    private ErrorVmodel errorVmodel;

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

    public NotFoundException(ErrorVmodel errorVmodel) {
        super(errorVmodel.getErrorMessage());
        this.errorVmodel = errorVmodel;
    }

    public NotFoundException(ErrorVmodel errorVmodel, Exception e) {
        super(e.getMessage().isEmpty() ? errorVmodel.getErrorMessage() : e.getMessage());
        errorVmodel.setErrorMessage(this.getMessage());
        this.errorVmodel = errorVmodel;
    }
}
