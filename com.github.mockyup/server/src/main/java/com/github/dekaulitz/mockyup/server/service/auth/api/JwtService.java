package com.github.dekaulitz.mockyup.server.service.auth.api;


import com.github.dekaulitz.mockyup.server.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;

public interface JwtService {

  String CAN_DO_REFRESH = "canRefresh";

  AuthProfileModel generateToken(UserEntities userEntities, Boolean rememberMe);

  AuthProfileModel validateToken(String token);

}
