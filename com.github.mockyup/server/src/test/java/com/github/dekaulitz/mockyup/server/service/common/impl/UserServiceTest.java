package com.github.dekaulitz.mockyup.server.service.common.impl;

import static org.mockito.Mockito.times;

import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import com.github.dekaulitz.mockyup.server.service.cms.impl.UserServiceImpl;
import com.github.dekaulitz.mockyup.server.service.common.api.BaseCrudService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

  @Mock
  private MongoTemplate mongoTemplate;
  @Autowired
  private UserService userService;

  @Test
  public void update() throws ServiceException {
    UserEntity userEntity = new UserEntity();
//    Mockito.when(mongoTemplate.save(Mockito.any()))
//        .thenThrow(new OptimisticLockingFailureException("something"))
//        .thenThrow(new OptimisticLockingFailureException("something"))
//        .thenReturn(userEntity);
    userService.save(userEntity);
//    Mockito.verify(mongoTemplate.save(userEntity), times(2));
  }
}
