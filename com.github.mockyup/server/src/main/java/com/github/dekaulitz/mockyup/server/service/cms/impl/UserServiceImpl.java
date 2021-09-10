package com.github.dekaulitz.mockyup.server.service.cms.impl;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.server.db.query.UserQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public UserEntities getUserByUsernameOrEmail(String usernameOrEmail) throws ServiceException {
    UserQuery userQuery = new UserQuery();
    userQuery.usernameOrEmail(usernameOrEmail);
    UserEntities userEntities = mongoTemplate.findOne(userQuery.getQuery(), UserEntities.class);
    if (userEntities == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND);
    }
    return userEntities;
  }

  @Override
  public UserEntities getById(String id) throws ServiceException {
    return null;
  }

  @Override
  public void delete(UserEntities userEntities) throws ServiceException {

  }

  @Override
  public List<UserEntities> getAll(GetUserParam getUserParam) {
    return null;
  }

  @Override
  public UserEntities update(UserEntities userEntities) throws ServiceException {
    return null;
  }

  @Override
  public UserEntities save(UserEntities userEntities) throws ServiceException {
    return mongoTemplate.save(userEntities);
  }
}
