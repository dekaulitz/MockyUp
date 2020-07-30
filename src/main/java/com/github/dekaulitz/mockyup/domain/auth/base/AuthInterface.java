package com.github.dekaulitz.mockyup.domain.auth.base;

import com.github.dekaulitz.mockyup.domain.auth.vmodels.DtoAuthProfileVmodel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;

import java.io.UnsupportedEncodingException;

public interface AuthInterface {
    /**
     * @param username username of user
     * @param password password of user
     * @return DtoAuthProfileVmodel   auth profile data
     * @throws UnathorizedAccess            when username or password was not valid or
     * @throws UnsupportedEncodingException when something bad happen when generate token
     */
    DtoAuthProfileVmodel generateAccessToken(String username, String password) throws UnathorizedAccess, UnsupportedEncodingException;

    /**
     * @param token current access token that expired
     * @return DtoAuthProfileVmodel  auth profile data
     * @throws UnathorizedAccess            when user id or not found or token not valid
     * @throws UnsupportedEncodingException when something bad happen when generate token
     */
    DtoAuthProfileVmodel refreshingToken(String token) throws UnathorizedAccess, UnsupportedEncodingException;
}
