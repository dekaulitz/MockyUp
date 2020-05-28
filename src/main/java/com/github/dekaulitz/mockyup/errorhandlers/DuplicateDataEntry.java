package com.github.dekaulitz.mockyup.errorhandlers;

import lombok.Getter;

public class DuplicateDataEntry extends Exception {
    @Getter
    private ErrorModel errorModel;

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

    public DuplicateDataEntry(ErrorModel errorModel) {
        super(errorModel.getErrorMessage());
        this.errorModel = errorModel;
    }
}
