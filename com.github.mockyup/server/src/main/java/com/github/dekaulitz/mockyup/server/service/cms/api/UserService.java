package com.github.dekaulitz.mockyup.server.service.cms.api;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.service.BaseCrudService;
import org.springframework.stereotype.Service;


public interface UserService extends BaseCrudService<UserEntities, GetUserParam> {

  UserEntities getUserByUsernameOrEmail(String usernameOrEmail) throws ServiceException;
}
