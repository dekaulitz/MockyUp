package com.github.dekaulitz.mockyup.server.domain.auth.models;

import com.github.dekaulitz.mockyup.server.configuration.jwt.JwtManager;
import com.github.dekaulitz.mockyup.server.db.entities.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.db.repositories.UserRepository;
import com.github.dekaulitz.mockyup.server.domain.auth.base.AuthInterface;
import com.github.dekaulitz.mockyup.server.domain.auth.vmodels.DtoAuthProfileVmodel;
import com.github.dekaulitz.mockyup.server.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import com.github.dekaulitz.mockyup.server.utils.ResponseCode;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthModel implements AuthInterface {

  @Autowired
  private final UserRepository userRepository;
  @Autowired
  private final JwtManager jwtManager;

  public AuthModel(UserRepository userRepository,
      JwtManager jwtManager) {
    this.userRepository = userRepository;
    this.jwtManager = jwtManager;
  }

  /**
   * @param username username of user
   * @param password password of user
   * @return DtoAuthProfileVmodel   auth profile data
   * @throws UnathorizedAccess            when username or password was not valid or
   * @throws UnsupportedEncodingException when something bad happen when generate token
   */
  @Override
  public DtoAuthProfileVmodel generateAccessToken(String username, String password)
      throws UnathorizedAccess, UnsupportedEncodingException {
    UserEntities userExist = this.userRepository.findFirstByUsername(username);
    if (userExist == null) {
      throw new UnathorizedAccess(ResponseCode.INVALID_USERNAME_OR_PASSWORD);
    }
    boolean isAuthenticated = HashingHelper.verifyHash(password, userExist.getPassword());
    if (!isAuthenticated) {
      throw new UnathorizedAccess(ResponseCode.INVALID_USERNAME_OR_PASSWORD);
    }
    return this.renderingAccessToken(userExist);
  }

  /**
   * @param authHeader current access token that expired
   * @return DtoAuthProfileVmodel  auth profile data
   * @throws UnathorizedAccess            when user id or not found or token not valid
   * @throws UnsupportedEncodingException when something bad happen when generate token
   */
  @Override
  public DtoAuthProfileVmodel refreshingToken(String authHeader)
      throws UnathorizedAccess, UnsupportedEncodingException {
    String token = jwtManager.getAuthorizationHeader(authHeader);
    Optional<String> userId = jwtManager.getUserIdFromToken(token);
    if (!userId.isPresent()) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
    Optional<UserEntities> userEntities = this.userRepository.findById(userId.get());
    if (!userEntities.isPresent()) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
    return this.renderingAccessToken(userEntities.get());
  }

  private DtoAuthProfileVmodel renderingAccessToken(UserEntities userEntities)
      throws UnsupportedEncodingException {
    DtoAuthProfileVmodel auth = new DtoAuthProfileVmodel();
    auth.setId(userEntities.getId());
    auth.setAccessMenus(userEntities.getAccessList());
    auth.setUsername(userEntities.getUsername());
    auth.setToken(jwtManager.generateToken(userEntities.getId()).getAccessToken());
    return auth;
  }

}
