package com.github.dekaulitz.mockyup.domain.auth.base;

import com.github.dekaulitz.mockyup.domain.auth.vmodels.DtoAuthProfileVmodel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;

import java.io.UnsupportedEncodingException;

public interface AuthInterface {
    DtoAuthProfileVmodel generateAccessToken(String username, String password) throws UnathorizedAccess, UnsupportedEncodingException;

    DtoAuthProfileVmodel refreshingToken(String token) throws UnathorizedAccess, UnsupportedEncodingException;
}
