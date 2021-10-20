package com.github.dekaulitz.mockyup.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.db.query.UserQuery;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.constants.Role;
import com.github.dekaulitz.mockyup.server.model.dto.ErrorMessageModel;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
@EnableMongoAuditing
@Slf4j
public class App {

  @Autowired
  private MongoTemplate mongoTemplate;


  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @PostConstruct
  public void initRootUser() {
    log.info("checking root user exists");
    UserQuery userQuery = new UserQuery();
    userQuery.username("root");
    UserEntity userEntity = mongoTemplate.findOne(userQuery.getQuery(), UserEntity.class);
    if (userEntity == null) {
      log.info("user root doesn't exists will create root users");
      Set<Role> roles = new HashSet<>(Arrays.asList(Role.values()));
      userEntity = UserEntity.builder()
          .username("root")
          .password(HashingHelper.hashing("root"))
          .email("root@root.com")
          .access(roles)
          .isEnabled(true)
          .isAccountNonLocked(true)
          .build();
      mongoTemplate.save(userEntity);
      log.info("user root created");
    }
    log.info("user root exists");
  }

  @PostConstruct
  public void injectMessages() {
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
      HashMap<String, ErrorMessageModel> messageMap = objectMapper
          .readValue(sb.toString(), new TypeReference<HashMap<String, ErrorMessageModel>>() {
          });
      for (ResponseCode responseCode : ResponseCode.values()) {
        String type = responseCode.toString();
        if (messageMap.containsKey(type)) {
          ResponseCode et = ResponseCode.valueOf(type);
          et.setErrorMessageModel(messageMap.get(type));
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
