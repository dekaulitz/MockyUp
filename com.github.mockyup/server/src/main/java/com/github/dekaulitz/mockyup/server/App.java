package com.github.dekaulitz.mockyup.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dekaulitz.mockyup.server.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.server.db.query.UserQuery;
import com.github.dekaulitz.mockyup.server.model.embeddable.Message;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
@Slf4j
public class App implements CommandLineRunner {

  @Autowired
  private MongoTemplate mongoTemplate;


  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  //injecting user for the first time
  public void run(String... args) throws Exception {
    log.info("checking root user exists");
    UserQuery userQuery = new UserQuery();
    userQuery.username("root");
    UserEntities userEntities = mongoTemplate.findOne(userQuery.getQuery(), UserEntities.class);
    if (userEntities == null) {
      log.info("user root doesn't exists will create root users");
      userEntities = UserEntities.builder()
          .username("root")
          .password(HashingHelper.hashing("root"))
          .email("root@root.com")
          .build();
      mongoTemplate.save(userEntities);
      log.info("user root created");
    }
    log.info("user root exists");

    // load user mesages
    Resource resource = new ClassPathResource("messages.json");
    InputStreamReader isReader = null;
    try {
      isReader = new InputStreamReader(resource.getInputStream());
      BufferedReader reader = new BufferedReader(isReader);
      StringBuffer sb = new StringBuffer();
      String str;
      while ((str = reader.readLine()) != null) {
        sb.append(str);
      }
      ObjectMapper objectMapper = new ObjectMapper();
      HashMap<String, Message> messageMap = objectMapper
          .readValue(sb.toString(), new TypeReference<HashMap<String, Message>>() {
          });
      for (ResponseCode responseCode : ResponseCode.values()) {
        String type = responseCode.toString();
        if (messageMap.containsKey(type)) {
          ResponseCode et = ResponseCode.valueOf(type);
          et.setValue(messageMap.get(type));
        } else {
          throw new RuntimeException("responseCode: " + type + " not found");
        }
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
