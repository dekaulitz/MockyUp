package com.github.dekaulitz.mockyup.errorhandlers;

import org.springframework.security.core.AuthenticationException;

public class UnathorizedAccess extends AuthenticationException {

    public UnathorizedAccess(String msg, Throwable t) {
        super(msg, t);
    }

    public UnathorizedAccess(String msg) {
        super(msg);
    }
}
