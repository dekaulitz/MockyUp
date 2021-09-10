package com.github.dekaulitz.mockyup.server.service.auth.api;

import com.github.dekaulitz.mockyup.server.model.request.UserLoginRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

  Object doLogin(UserLoginRequest userLoginRequest) throws Exception;

  void validateUserSession(UserLoginRequest userLoginRequest);

  void updateUserProfile();
}
