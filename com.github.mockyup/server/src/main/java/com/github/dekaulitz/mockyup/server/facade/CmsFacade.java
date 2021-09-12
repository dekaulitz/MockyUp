package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.model.request.user.CreateUserRequest;
import com.github.dekaulitz.mockyup.server.model.request.user.UpdateUserRequest;
import com.github.dekaulitz.mockyup.server.service.auth.WithAuthService;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsFacade extends WithAuthService {

  @Autowired
  private UserService userService;
  @Autowired
  private ModelMapper modelMapper;

  public UserEntity createUser(@Valid CreateUserRequest createUserRequest)
      throws ServiceException {
    AuthProfileModel authProfile = this.getAuthProfile();
    UserEntity userEntity = modelMapper.map(createUserRequest, UserEntity.class);
    userEntity.setPassword(HashingHelper.hashing(userEntity.getPassword()));
    userEntity.setCreatedByUserId(authProfile.getId());
    userEntity.setUpdatedByUserId(authProfile.getId());
    return userService.save(userEntity);
  }

  public UserEntity updateUser(String id, @Valid UpdateUserRequest updateUserRequest)
      throws ServiceException {
    return userService.updateUser(id, updateUserRequest, getAuthProfile());
  }

  public List<UserEntity> allUsers(@Valid GetUserParam getUserParam)
      throws ServiceException {
    return userService.getAll(getUserParam);
  }
}
