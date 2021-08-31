package com.github.dekaulitz.mockyup.server.service.auth.impl;

import com.github.dekaulitz.mockyup.server.configuration.jwt.JwtManager;
import com.github.dekaulitz.mockyup.server.db.entities.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.db.query.UserQuery;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.model.request.UserLoginRequest;
import com.github.dekaulitz.mockyup.server.model.response.UserLoggedResponse;
import com.github.dekaulitz.mockyup.server.service.auth.api.AuthService;
import com.github.dekaulitz.mockyup.server.service.auth.helper.HashingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Value("${mock.auth.secret}")
  private String secret;
  @Value("${mock.auth.refresh}")
  private Long refreshTime;
  @Value("${mock.auth.expired}")
  private Long expiredTime;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private JwtManager jwtManager;

  @Override
  public UserLoggedResponse doLogin(UserLoginRequest userLoginRequest)
      throws Exception {
    GetUserParam getUserParam = GetUserParam.builder()
        .email(userLoginRequest.getEmail())
        .build();
    UserQuery userQuery = new UserQuery();
    userQuery.buildQuery(getUserParam);
    UserEntities userEntities = mongoTemplate.findOne(userQuery.getQuery(), UserEntities.class);
    if (userEntities == null) {
      throw new Exception("User not found");
    }
    String password = userEntities.getPassword();
    if (Boolean.FALSE == HashingHelper.verifyHash(userLoginRequest.getPassword(), password)) {
      throw new Exception("User not found");
    }
    String token = jwtManager.buildToken(userEntities.getId());
    return UserLoggedResponse.builder()
        .token(token)
        .id(userEntities.getId())
        .email(userEntities.getEmail())
        .build();
  }

  @Override
  public void validateUserSession(UserLoginRequest userLoginRequest) {

  }

  @Override
  public void updateUserProfile() {

  }
}
