package com.github.dekaulitz.mockyup.server.service.cms.impl;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.db.query.UserQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.model.request.user.UpdateUserRequest;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.service.common.impl.BaseCrudServiceImpl;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends
    BaseCrudServiceImpl<UserEntity> implements UserService {

  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private ModelMapper modelMapper;

  @Override
  public UserEntity getUserByUsernameOrEmail(String usernameOrEmail) throws ServiceException {
    UserQuery userQuery = new UserQuery();
    userQuery.usernameOrEmail(usernameOrEmail);
    UserEntity userEntity = mongoTemplate.findOne(userQuery.getQuery(), UserEntity.class);
    if (userEntity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND);
    }
    return userEntity;
  }

  @Override
  @Retryable(value = {
      OptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000))
  public UserEntity updateUser(String id, UpdateUserRequest updateUserRequest,
      AuthProfileModel authProfileModel)
      throws ServiceException {
    UserQuery userQuery = new UserQuery();
    userQuery.usernameOrEmail(updateUserRequest.getUsername(), updateUserRequest.getEmail());
    List<UserEntity> userExists = this.getAll(userQuery.getQuery(), UserEntity.class);
    if (CollectionUtils.isNotEmpty(userExists)) {
      for (UserEntity userExist : userExists) {
        if (!userExist.getId().equals(id)) {
          throw new ServiceException(ResponseCode.DUPLICATE_DATA_ENTRY);
        }
      }
    }
    UserEntity userEntity = this.getById(id, UserEntity.class);
    userEntity.setUsername(updateUserRequest.getUsername());
    userEntity.setEmail(updateUserRequest.getEmail());
    userEntity.setAccess(updateUserRequest.getAccess());
    userEntity.setEnabled(updateUserRequest.isEnabled());
    userEntity.setAccountNonLocked(updateUserRequest.isAccountNonLocked());
    userEntity.setAccessProjects(updateUserRequest.getAccessProjects());
    userEntity.setUpdatedByUserId(authProfileModel.getId());
    return this.update(userEntity);
  }

  @Override
  public List<UserEntity> getAll(GetUserParam getUserParam) {
    UserQuery userQuery = new UserQuery();
    userQuery.buildQuery(getUserParam);
    return this.getAll(userQuery.getQueryWithPaging(), UserEntity.class);
  }

  @Override
  public UserEntity save(UserEntity userEntity) throws ServiceException {
    UserQuery userQuery = new UserQuery();
    userQuery.usernameOrEmail(userEntity.getUsername(), userEntity.getEmail());
    List<UserEntity> userExists = this.getAll(userQuery.getQuery(), UserEntity.class);
    if (CollectionUtils.isNotEmpty(userExists)) {
      throw new ServiceException(ResponseCode.DUPLICATE_DATA_ENTRY);
    }
    return super.save(userEntity);
  }
}
