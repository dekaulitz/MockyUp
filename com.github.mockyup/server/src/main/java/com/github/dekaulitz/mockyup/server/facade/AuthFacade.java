package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.request.UserLoginRequest;
import com.github.dekaulitz.mockyup.server.service.auth.api.JwtService;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
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
  private ModelMapper modelMapper;

  public AuthProfileModel login(@Valid UserLoginRequest userLoginRequest) throws ServiceException {
    try {
      UserEntities userEntities = userService
          .getUserByUsernameOrEmail(userLoginRequest.getUsernameOrEmail());
      boolean passwordIsValid = HashingHelper
          .verifyHash(userLoginRequest.getPassword(), userEntities.getPassword());
      if (!passwordIsValid) {
        throw new ServiceException(ResponseCode.INVALID_USER_LOGIN);
      }
      return jwtService.generateToken(userEntities, userLoginRequest.isRememberMe());

    } catch (ServiceException e) {
      e.printStackTrace();
      throw new ServiceException(ResponseCode.INVALID_USER_LOGIN);
    }

  }
}
