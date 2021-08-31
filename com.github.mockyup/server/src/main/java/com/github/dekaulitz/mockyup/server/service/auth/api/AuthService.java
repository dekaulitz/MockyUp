package com.github.dekaulitz.mockyup.server.service.auth.api;

import com.github.dekaulitz.mockyup.server.model.request.UserLoginRequest;
import com.github.dekaulitz.mockyup.server.model.response.UserLoggedResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

  UserLoggedResponse doLogin(UserLoginRequest userLoginRequest) throws Exception;

  void validateUserSession(UserLoginRequest userLoginRequest);

  void updateUserProfile();
}
