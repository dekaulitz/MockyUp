package com.github.dekaulitz.mockyup.server.service.cms.api;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.model.request.user.CreateUserRequest;
import com.github.dekaulitz.mockyup.server.model.request.user.UpdateUserRequest;
import com.github.dekaulitz.mockyup.server.service.common.api.BaseCrudService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends BaseCrudService<UserEntity> {

  UserEntity getUserByUsernameOrEmail(String usernameOrEmail) throws ServiceException;

  UserEntity updateUser(String id, UpdateUserRequest updateUserRequest,
      AuthProfileModel authProfileModel)
      throws ServiceException;

  List<UserEntity> getAll(GetUserParam getUserParam);

  long getCount(GetUserParam getUserParam);

  UserEntity createUser(CreateUserRequest createUserRequest,
      AuthProfileModel authProfileModel) throws ServiceException;
}
