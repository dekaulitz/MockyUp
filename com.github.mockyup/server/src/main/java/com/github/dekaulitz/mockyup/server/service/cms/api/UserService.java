package com.github.dekaulitz.mockyup.server.service.cms.api;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.service.BaseCrudService;


public interface UserService extends BaseCrudService<UserEntity, GetUserParam> {

  UserEntity getUserByUsernameOrEmail(String usernameOrEmail) throws ServiceException;
}
