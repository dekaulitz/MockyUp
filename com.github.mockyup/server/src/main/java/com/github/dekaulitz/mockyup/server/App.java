package com.github.dekaulitz.mockyup.server;

import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.MockRepository;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.UserRepository;
import com.github.dekaulitz.mockyup.server.model.constants.Role;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import com.github.dekaulitz.mockyup.server.service.common.helper.MessageHelper;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
@Slf4j
@EnableAutoConfiguration(exclude = {MongoDataAutoConfiguration.class})
public class App implements CommandLineRunner {

  @Autowired
  private final UserRepository userRepository;
  @Autowired
  private final MockRepository mockRepository;

  public App(UserRepository userRepository, MockRepository mockRepository) {
    this.userRepository = userRepository;
    this.mockRepository = mockRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  //injecting user for the first time
  public void run(String... args) throws Exception {
    // injecting user root
    UserEntities userEntities = this.userRepository.findFirstByUsername("root");
    if (userEntities == null) {
      log.info("user not found will create new user root");
      UserEntities rootUser = new UserEntities();
      //default username
      rootUser.setUsername("root");
      //default password for root is root
      rootUser.setPassword(HashingHelper.hashing("root"));
      rootUser.setUpdatedDate(new Date());
      //grant all accesss
      rootUser
          .setAccessList(Arrays.asList(Role.MOCKS_READ_WRITE.name(), Role.USERS_READ_WRITE.name()));
      this.userRepository.save(rootUser);
      //will check if there is old swagger that available
      this.mockRepository.injectRootIntoAllMocks(rootUser);
    }
    log.info("user root already created will pass the process");

    // load user mesages
    MessageHelper.loadInstance();
  }


  @Bean
  public ModelMapper modelMapper() {
    final ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STRICT);
    return modelMapper;
  }

  @PostConstruct
  public void setDefaultTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
  }


}
