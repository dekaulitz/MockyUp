package com.github.dekaulitz.mockyup.server.service.auth.api;

import com.github.dekaulitz.mockyup.server.model.JwtProfileModel;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public interface JwtService {

  String CAN_DO_REFRESH = "canRefresh";
  String EXPIRED_AT = "expiredAt";

  String getAuthorizationHeader(String AuthHeader);

  JwtProfileModel generateToken(String userId)
      throws UnsupportedEncodingException;

  JwtProfileModel validateToken(String token);

  Optional<String> getUserIdFromToken(String token)
          throws UnsupportedEncodingException;

  String buildToken(String id)
              throws UnsupportedEncodingException;
}
