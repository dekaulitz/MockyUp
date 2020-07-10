package com.github.dekaulitz.mockyup.infrastructure.errors.handlers;

import com.github.dekaulitz.mockyup.infrastructure.errors.vmodels.ErrorVmodel;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class UnathorizedAccess extends AuthenticationException {
    @Getter
    private ErrorVmodel errorVmodel;

    public UnathorizedAccess(String msg, Throwable t) {
        super(msg, t);
    }

    public UnathorizedAccess(String msg) {
        super(msg);
    }

    public UnathorizedAccess(ErrorVmodel errorVmodel) {
        super(errorVmodel.getErrorMessage());
        this.errorVmodel = errorVmodel;
    }

    public UnathorizedAccess(ErrorVmodel errorVmodel, Exception e) {
        super(e.getMessage().isEmpty() ? errorVmodel.getErrorMessage() : e.getMessage());
        errorVmodel.setErrorMessage(this.getMessage());
        this.errorVmodel = errorVmodel;
    }
}
