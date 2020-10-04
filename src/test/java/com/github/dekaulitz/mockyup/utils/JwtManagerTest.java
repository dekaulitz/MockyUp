package com.github.dekaulitz.mockyup.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;


class JwtManagerTest {

  @Test
  void generateToken() throws UnsupportedEncodingException {
    String id = "0123213";
    String token = JwtManager.generateToken(id);
    Assert.isTrue(!token.isEmpty(), "token is empty");
    AuthenticationProfileModel authProfile = JwtManager.validateToken(token);
    Assert.isTrue(authProfile.get_id().equals(id), "id is not equal");
  }

  @Test
  void validateFakeToken() {
    try {
      String fakeToken = generateFakeToken();
      JwtManager.validateToken(fakeToken);
    } catch (Exception e) {
      Assert.isInstanceOf(UnathorizedAccess.class, e, "exception is not expected");
      if (e instanceof UnathorizedAccess) {
        Assert.isTrue(((UnathorizedAccess) e).getErrorVmodel().getErrorMessage()
            .equals(ResponseCode.TOKEN_INVALID.getErrorMessage()), "exception message is not same");
      }
    }
  }

  @Test
  void validateTokenWhenTokenExpired() {
    try {
      String expiredToken = generateTokenExpired();
      JwtManager.validateToken(expiredToken);
    } catch (Exception e) {
      Assert.isInstanceOf(UnathorizedAccess.class, e, "exception is not expected");
      if (e instanceof UnathorizedAccess) {
        Assert.isTrue(((UnathorizedAccess) e).getErrorVmodel().getErrorMessage()
            .equals(ResponseCode.TOKEN_EXPIRED.getErrorMessage()), "exception message is not same");
      }
    }
  }

  @Test
  void validateTokenWhenTokenExpiredAndCanRefresh() {
    try {
      String expiredToken = generateTokenExpiredCanDoRefrsh();
      JwtManager.validateToken(expiredToken);
    } catch (Exception e) {
      Assert.isInstanceOf(UnathorizedAccess.class, e, "exception is not expected");
      if (e instanceof UnathorizedAccess) {
        Assert.isTrue(((UnathorizedAccess) e).getErrorVmodel().getErrorMessage()
                .equals(ResponseCode.REFRESH_TOKEN_REQUIRED.getErrorMessage()),
            "exception message is not same");
      }
    }
  }

  String generateTokenExpiredCanDoRefrsh() throws UnsupportedEncodingException {
    Algorithm algorithm = Algorithm.HMAC256(JwtManager.SECCRET);
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    return JWT.create()
        .withClaim("id", "fake")
        .withClaim(JwtManager.CAN_DO_REFRESH, new Date(t + (60 * JwtManager.ONE_MINUTE_IN_MILLIS)))
        .withClaim(JwtManager.EXPIRED_AT, new Date(t))
        .withIssuedAt(new Date()).sign(algorithm);
  }

  String generateTokenExpired() throws UnsupportedEncodingException {
    Algorithm algorithm = Algorithm.HMAC256(JwtManager.SECCRET);
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    return JWT.create()
        .withClaim("id", "fake")
        .withClaim(JwtManager.CAN_DO_REFRESH, new Date(t))
        .withClaim(JwtManager.EXPIRED_AT, new Date(t))
        .withIssuedAt(new Date()).sign(algorithm);
  }

  String generateFakeToken() throws UnsupportedEncodingException {
    Algorithm algorithm = Algorithm.HMAC256("fake");
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    return JWT.create()
        .withClaim("id", "fake")
        .withClaim(JwtManager.CAN_DO_REFRESH, new Date(t + (60 * JwtManager.ONE_MINUTE_IN_MILLIS)))
        .withClaim(JwtManager.EXPIRED_AT, new Date(t + (30 + JwtManager.ONE_MINUTE_IN_MILLIS)))
        .withIssuedAt(new Date()).sign(algorithm);
  }
}
