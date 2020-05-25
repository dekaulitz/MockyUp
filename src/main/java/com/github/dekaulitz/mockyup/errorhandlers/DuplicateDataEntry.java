package com.github.dekaulitz.mockyup.errorhandlers;

public class DuplicateDataEntry extends Exception {
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
}
