package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.model.request.user.CreateUserRequest;
import com.github.dekaulitz.mockyup.server.model.request.user.UpdateUserRequest;
import com.github.dekaulitz.mockyup.server.service.auth.WithAuthService;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UsersFacade extends WithAuthService {

  @Autowired
  @Qualifier(value = "userService")
  @Getter
  private UserService userService;

  public UserEntity createUser(@Valid @NotNull CreateUserRequest createUserRequest)
      throws ServiceException {
    AuthProfileModel authProfile = this.getAuthProfile();
    return userService.createUser(createUserRequest, authProfile);
  }

  public UserEntity getUserDetail(String userId)
      throws ServiceException {
    UserEntity userEntity = userService.getById(userId, UserEntity.class);
    if (userEntity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND);
    }
    return userEntity;
  }

  public UserEntity updateUser(String id, @Valid @NotNull UpdateUserRequest updateUserRequest)
      throws ServiceException {
    return userService.updateUser(id, updateUserRequest, getAuthProfile());
  }

  public List<UserEntity> allUsers(@Valid GetUserParam getUserParam)
      throws ServiceException {
    return userService.getAll(getUserParam);
  }

  public long getCount(@Valid GetUserParam getUserParam)
      throws ServiceException {
    return userService.getCount(getUserParam);
  }
}
