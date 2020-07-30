package com.github.dekaulitz.mockyup.domain.auth.models;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.UserRepository;
import com.github.dekaulitz.mockyup.domain.auth.base.AuthInterface;
import com.github.dekaulitz.mockyup.domain.auth.vmodels.DtoAuthProfileVmodel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.utils.Hash;
import com.github.dekaulitz.mockyup.utils.JwtManager;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class AuthModel implements AuthInterface {
    @Autowired
    private final UserRepository userRepository;

    public AuthModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param username username of user
     * @param password password of user
     * @return DtoAuthProfileVmodel   auth profile data
     * @throws UnathorizedAccess            when username or password was not valid or
     * @throws UnsupportedEncodingException when something bad happen when generate token
     */
    @Override
    public DtoAuthProfileVmodel generateAccessToken(String username, String password) throws UnathorizedAccess, UnsupportedEncodingException {
        UserEntities userExist = this.userRepository.findFirstByUsername(username);
        if (userExist == null) {
            throw new UnathorizedAccess(ResponseCode.INVALID_USERNAME_OR_PASSWORD);
        }
        boolean isAuthenticated = Hash.verifyHash(password, userExist.getPassword());
        if (!isAuthenticated) throw new UnathorizedAccess(ResponseCode.INVALID_USERNAME_OR_PASSWORD);
        return this.renderingAccessToken(userExist);
    }

    /**
     * @param token current access token that expired
     * @return DtoAuthProfileVmodel  auth profile data
     * @throws UnathorizedAccess            when user id or not found or token not valid
     * @throws UnsupportedEncodingException when something bad happen when generate token
     */
    @Override
    public DtoAuthProfileVmodel refreshingToken(String token) throws UnathorizedAccess, UnsupportedEncodingException {
        Optional<String> userId = JwtManager.getUserIdFromToken(token);
        if (!userId.isPresent()) throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
        Optional<UserEntities> userEntities = this.userRepository.findById(userId.get());
        if (!userEntities.isPresent())
            throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
        return this.renderingAccessToken(userEntities.get());
    }

    private DtoAuthProfileVmodel renderingAccessToken(UserEntities userEntities) throws UnsupportedEncodingException {
        DtoAuthProfileVmodel auth = new DtoAuthProfileVmodel();
        auth.setId(userEntities.getId());
        auth.setAccessMenus(userEntities.getAccessList());
        auth.setUsername(userEntities.getUsername());
        auth.setToken(JwtManager.generateToken(userEntities.getId()));
        return auth;
    }

}
