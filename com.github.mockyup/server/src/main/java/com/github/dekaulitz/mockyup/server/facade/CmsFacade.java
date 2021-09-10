package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.request.CreateUserRequest;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsFacade {

  @Autowired
  private UserService userService;
  @Autowired
  private ModelMapper modelMapper;


  public UserEntity createUser(@Valid CreateUserRequest createUserRequest)
      throws ServiceException {
    UserEntity userEntity = modelMapper.map(createUserRequest, UserEntity.class);
    userEntity.setPassword(HashingHelper.hashing(userEntity.getPassword()));
    return userService.save(userEntity);
  }
}
