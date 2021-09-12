package com.github.dekaulitz.mockyup.server.service.common.impl;

import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private MongoTemplate mongoTemplate;
  @Autowired
  private UserService userService;

  @Test
  public void update() throws ServiceException {
    String test = "berak/123123123";
    Assertions.assertThat(test.contains("sss")).isEqualTo(true);
  }
}
