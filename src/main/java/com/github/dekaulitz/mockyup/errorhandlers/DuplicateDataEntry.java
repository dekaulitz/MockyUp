package com.github.dekaulitz.mockyup.errorhandlers;

import com.github.dekaulitz.mockyup.vmodels.ErrorVmodel;
import lombok.Getter;

public class DuplicateDataEntry extends Exception {
    @Getter
    private ErrorVmodel errorVmodel;

    public DuplicateDataEntry() {
    }

    public DuplicateDataEntry(String message) {
        super(message);
    }

    public DuplicateDataEntry(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDataEntry(Throwable cause) {
        super(cause);
    }

    public DuplicateDataEntry(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DuplicateDataEntry(ErrorVmodel errorVmodel) {
        super(errorVmodel.getErrorMessage());
        this.errorVmodel = errorVmodel;
    }

    public DuplicateDataEntry(ErrorVmodel errorVmodel, Exception e) {
        super(e.getMessage().isEmpty() ? errorVmodel.getErrorMessage() : e.getMessage());
        errorVmodel.setErrorMessage(this.getMessage());
        this.errorVmodel = errorVmodel;
    }
}
