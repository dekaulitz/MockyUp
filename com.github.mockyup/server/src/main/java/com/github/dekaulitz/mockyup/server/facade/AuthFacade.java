package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.errors.UnauthorizedException;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.dto.Mandatory;
import com.github.dekaulitz.mockyup.server.model.param.GetUserLogLoginParam;
import com.github.dekaulitz.mockyup.server.model.request.UserLoginRequest;
import com.github.dekaulitz.mockyup.server.service.auth.api.JwtService;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserLogLoginService;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthFacade {

  @Autowired
  private UserService userService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserLogLoginService userLogLoginService;

  public AuthProfileModel login(@Valid UserLoginRequest userLoginRequest,
      Mandatory mandatory) throws ServiceException {
    try {
      UserEntity userEntity = userService
          .getUserByUsernameOrEmail(userLoginRequest.getUsernameOrEmail());
      boolean passwordIsValid = HashingHelper
          .verifyHash(userLoginRequest.getPassword(), userEntity.getPassword());
      if (!passwordIsValid) {
        throw new ServiceException(ResponseCode.INVALID_USER_LOGIN);
      }
      userLogLoginService.deleteByParameter(GetUserLogLoginParam.builder()
          .userId(userEntity.getId())
          .build());
      AuthProfileModel authProfileModel = jwtService.generateToken(userEntity,
          userLoginRequest.isRememberMe());
      userLogLoginService.logLogin(authProfileModel, mandatory);
      return authProfileModel;
    } catch (ServiceException e) {
      e.printStackTrace();
      throw new ServiceException(ResponseCode.INVALID_USER_LOGIN);
    }
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
    userLogLoginService.deleteByParameter(GetUserLogLoginParam.builder().jti(jti).build());
  }
}
