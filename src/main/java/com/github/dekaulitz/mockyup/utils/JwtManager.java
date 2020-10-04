package com.github.dekaulitz.mockyup.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * jwt manager for handling jwt
 */
public class JwtManager {

  public static final String CAN_DO_REFRESH = "canRefresh";
  public static final String EXPIRED_AT = "expiredAt";
  public static final long ONE_MINUTE_IN_MILLIS = 600000;//millisecs
  public static String SECCRET = "qwERTyuIODfghjK@#$%^7888ghjkbnhjkxzxzxAJSDHJASDHJHJS";

  public static String generateToken(String id) throws UnsupportedEncodingException {
    Algorithm algorithm = Algorithm.HMAC256(SECCRET);
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    return JWT.create()
        .withClaim("id", id)
        .withClaim(CAN_DO_REFRESH, new Date(t + (60 * ONE_MINUTE_IN_MILLIS)))
        .withClaim(EXPIRED_AT, new Date(t + (30 + ONE_MINUTE_IN_MILLIS)))
        .withIssuedAt(new Date()).sign(algorithm);
  }

  public static AuthenticationProfileModel validateToken(String token) {
    try {
      DecodedJWT jwt = decodeToken(token);
      Date canRefresh = jwt.getClaim(CAN_DO_REFRESH).asDate();
      Date expiredAt = jwt.getClaim(EXPIRED_AT).asDate();
      long currentTime = System.currentTimeMillis();
      if (expiredAt.getTime() < currentTime) {
        if (canRefresh.getTime() >= currentTime) {
          throw new UnathorizedAccess(ResponseCode.REFRESH_TOKEN_REQUIRED);
        } else {
          throw new UnathorizedAccess(ResponseCode.TOKEN_EXPIRED);
        }
      }
      AuthenticationProfileModel jwtUserModel = new AuthenticationProfileModel();
      jwtUserModel.set_id(jwt.getClaim("id").asString());
      return jwtUserModel;
    } catch (JWTVerificationException | UnsupportedEncodingException e) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
  }


  public static Optional<String> getUserIdFromToken(String token)
      throws UnsupportedEncodingException {
    DecodedJWT jwt = decodeToken(token);
    Date canRefresh = jwt.getClaim(CAN_DO_REFRESH).asDate();
    Date expiredAt = jwt.getClaim(EXPIRED_AT).asDate();
    long currentTime = System.currentTimeMillis();
    if ((expiredAt.getTime() < currentTime) && (canRefresh.getTime() <= currentTime)) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_EXPIRED);
    }
    return Optional.ofNullable(jwt.getClaim("id").asString());
  }

  static DecodedJWT decodeToken(String token) throws UnsupportedEncodingException {
    try {
      Algorithm algorithm = Algorithm.HMAC256(SECCRET);
      JWTVerifier verifier = JWT.require(algorithm).build();
      return verifier.verify(token);
    } catch (SignatureVerificationException signatureVerificationException) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID, signatureVerificationException);
    }

  }

  public static String getAuthorizationHeader(String AuthHeader) {
    if (AuthHeader == null || !AuthHeader.startsWith("Bearer")) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
    return AuthHeader.substring(7);
  }
}
