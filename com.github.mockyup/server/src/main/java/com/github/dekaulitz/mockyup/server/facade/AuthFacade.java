package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.db.entities.UserLogLoginEntity;
import com.github.dekaulitz.mockyup.server.db.query.UserQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.errors.UnauthorizedException;
import com.github.dekaulitz.mockyup.server.model.constants.CacheConstants;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import com.github.dekaulitz.mockyup.server.model.request.auth.UpdateProfileRequest;
import com.github.dekaulitz.mockyup.server.model.request.auth.UserLoginRequest;
import com.github.dekaulitz.mockyup.server.service.auth.WithAuthService;
import com.github.dekaulitz.mockyup.server.service.auth.api.JwtService;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserLogLoginService;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthFacade extends WithAuthService {

  @Autowired
  private UserService userService;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserLogLoginService userLogLoginService;
  @Autowired
  private CacheService cacheService;

  public AuthProfileModel login(@Valid UserLoginRequest userLoginRequest,
      MandatoryModel mandatoryModel) throws ServiceException {
    try {
      UserEntity userEntity = userService
          .getUserByUsernameOrEmail(userLoginRequest.getUsernameOrEmail());
      boolean passwordIsValid = HashingHelper
          .verifyHash(userLoginRequest.getPassword(), userEntity.getPassword());
      if (!passwordIsValid) {
        throw new ServiceException(ResponseCode.INVALID_USER_LOGIN);
      }
      UserLogLoginEntity userLogLog = userLogLoginService.deleteByJtiOrUserId(
          userEntity.getId());
      if (userLogLog != null) {
        cacheService.deleteCache(CacheConstants.AUTH_PREFIX + userLogLog.getJti());
      }
      AuthProfileModel authProfileModel = jwtService.generateToken(userEntity,
          userLoginRequest.isRememberMe());
      userLogLoginService.logLogin(authProfileModel, mandatoryModel);
      return authProfileModel;
    } catch (ServiceException e) {
      e.printStackTrace();
      throw new ServiceException(ResponseCode.INVALID_USER_LOGIN);
    }
  }

  public UserEntity getAuthDetail() throws ServiceException {
    AuthProfileModel authProfile = this.getAuthProfile();
    UserEntity userEntity = userService.getById(authProfile.getId(), UserEntity.class);
    if (userEntity == null) {
      throw new UnauthorizedException(ResponseCode.TOKEN_NOT_VALID);
    }
    return userEntity;
  }

  public void logout(@NotNull @NotBlank String authorization) {
    if (authorization == null || !authorization.startsWith("Bearer")) {
      throw new UnauthorizedException(ResponseCode.TOKEN_NOT_VALID);
    }
    if (authorization.length() <= 7) {
      throw new UnauthorizedException(ResponseCode.TOKEN_NOT_VALID);
    }
    String token = authorization.substring(7);
    String jti = jwtService.invalidateToken(token);
    userLogLoginService.deleteByJtiOrUserId(jti);
  }

  public AuthProfileModel updateAuthProfile(UpdateProfileRequest updateProfileRequest,
      MandatoryModel mandatoryModel) throws ServiceException {
    AuthProfileModel authProfile = this.getAuthProfile();
    UserQuery userQuery = new UserQuery();
    userQuery.usernameOrEmail(updateProfileRequest.getUsername(), updateProfileRequest.getEmail());
    List<UserEntity> userExists = this.userService.getAll(userQuery.getQuery(), UserEntity.class);
    if (CollectionUtils.isNotEmpty(userExists)) {
      for (UserEntity userExist : userExists) {
        if (!userExist.getId().equals(authProfile.getId())) {
          throw new ServiceException(ResponseCode.DUPLICATE_DATA_ENTRY);
        }
      }
    }
    UserEntity userEntity = userService.getById(authProfile.getId(), UserEntity.class);
    if (userEntity == null) {
      throw new UnauthorizedException(ResponseCode.TOKEN_NOT_VALID);
    }
    if (StringUtils.isNotBlank(updateProfileRequest.getPassword())) {
      userEntity.setPassword(HashingHelper.hashing(userEntity.getPassword()));
    }
    userEntity.setUsername(updateProfileRequest.getUsername());
    userEntity.setEmail(updateProfileRequest.getEmail());
    userEntity.setUpdatedDate(new Date());
    userEntity.setUpdatedByUserId(authProfile.getId());
    userService.save(userEntity);
    UserLogLoginEntity userLogLog = userLogLoginService.deleteByJtiOrUserId(
        userEntity.getId());
    if (userLogLog != null) {
      cacheService.deleteCache(CacheConstants.AUTH_PREFIX + userLogLog.getJti());
    }
    AuthProfileModel authProfileModel = jwtService.generateToken(userEntity,
       true);
    userLogLoginService.logLogin(authProfileModel, mandatoryModel);
    return authProfileModel;
  }
}
