package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.UserRepository;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.helperTest.Helper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class UserRepositorySupportImplTest {

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
  }

  @Test
  void paging() {
    UserEntities firstData = Helper.getUserEntities();
    firstData.setId(null);
    firstData.setUsername("firstData");
    userRepository.save(firstData);

    UserEntities secondData = Helper.getUserEntities();
    secondData.setId(null);
    secondData.setUsername("secondData");
    userRepository.save(secondData);

    PageRequest pageRequest = PageRequest.of(0, 10);
    UserEntitiesPage userEntitiesPage = userRepository.paging(pageRequest, "username:data");
    Assert.isTrue(userEntitiesPage.getRows().size() == 2, "size is not expected");

    pageRequest = PageRequest.of(0, 1);
    userEntitiesPage = userRepository.paging(pageRequest, "username:data");
    Assert.isTrue(userEntitiesPage.getRows().size() == 1, "size is not expected");

    pageRequest = PageRequest.of(0, 10);
    userEntitiesPage = userRepository.paging(pageRequest, "username:secondData");
    Assert.isTrue(userEntitiesPage.getRows().size() == 1, "size is not expected");

  }

  @Test
  void getUserListByUserName() {
    UserEntities firstData = Helper.getUserEntities();
    firstData.setId(null);
    firstData.setUsername("firstData");
    userRepository.save(firstData);

    UserEntities secondData = Helper.getUserEntities();
    secondData.setId(null);
    secondData.setUsername("secondData");
    userRepository.save(secondData);

    List<UserEntities> userEntities = userRepository
        .getUserListByUserName("data", secondData.getId());
    Assert.isTrue(userEntities.size() == 1, "size is not expected");

    UserEntities thirdData = Helper.getUserEntities();
    thirdData.setId(null);
    thirdData.setUsername("thirdData");
    userRepository.save(thirdData);

    userEntities = userRepository.getUserListByUserName("data", secondData.getId());
    Assert.isTrue(userEntities.size() == 2, "size is not expected");
  }
}
