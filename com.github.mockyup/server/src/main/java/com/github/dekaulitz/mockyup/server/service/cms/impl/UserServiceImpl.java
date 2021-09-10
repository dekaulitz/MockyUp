package com.github.dekaulitz.mockyup.server.service.cms.impl;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.db.query.UserQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private MongoTemplate mongoTemplate;

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
  public UserEntity getById(String id) throws ServiceException {
    return null;
  }

  @Override
  public void delete(UserEntity userEntity) throws ServiceException {

  }

  @Override
  public List<UserEntity> getAll(GetUserParam getUserParam) {
    return null;
  }

  @Override
  public UserEntity update(@Valid UserEntity userEntity) throws ServiceException {
    return null;
  }

  @Override
  public UserEntity save(@Valid UserEntity userEntity) throws ServiceException {
    return mongoTemplate.save(userEntity);
  }
}
