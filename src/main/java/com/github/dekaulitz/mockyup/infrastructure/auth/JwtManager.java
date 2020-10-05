package com.github.dekaulitz.mockyup.infrastructure.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.infrastructure.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>this is jwt manager for generate token and validate token</pre>
 */
@Service
public class JwtManager {

  public static final String CAN_DO_REFRESH = "canRefresh";
  public static final String EXPIRED_AT = "expiredAt";


  @Autowired
  private final JwtProperties config;

  public JwtManager(JwtProperties config) {
    this.config = config;
  }


  public String getAuthorizationHeader(String AuthHeader) {
    if (AuthHeader == null || !AuthHeader.startsWith("Bearer")) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
    return AuthHeader.substring(7);
  }

  public AuthenticationProfileModel generateToken(String userId)
      throws UnsupportedEncodingException {
    AuthenticationProfileModel authenticationProfileModel = new AuthenticationProfileModel();
    authenticationProfileModel.setId(userId);
    authenticationProfileModel.setAccessToken(generateToken(userId, this.config));
    return authenticationProfileModel;
  }

  public AuthenticationProfileModel validateToken(String token) {
    try {
      DecodedJWT jwt = decodeToken(token, this.config.getSecret());
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
      jwtUserModel.setId(jwt.getClaim("id").asString());
      return jwtUserModel;
    } catch (JWTVerificationException | UnsupportedEncodingException e) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
  }

  public Optional<String> getUserIdFromToken(String token)
      throws UnsupportedEncodingException {
    DecodedJWT jwt = decodeToken(token, this.config.getSecret());
    Date canRefresh = jwt.getClaim(CAN_DO_REFRESH).asDate();
    Date expiredAt = jwt.getClaim(EXPIRED_AT).asDate();
    long currentTime = System.currentTimeMillis();
    if ((expiredAt.getTime() < currentTime) && (canRefresh.getTime() <= currentTime)) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_EXPIRED);
    }
    return Optional.ofNullable(jwt.getClaim("id").asString());
  }

  private String generateToken(String id, JwtProperties config)
      throws UnsupportedEncodingException {
    Algorithm algorithm = Algorithm.HMAC256(config.getSecret());
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    return JWT.create()
        .withClaim("id", id)
        .withClaim(CAN_DO_REFRESH, new Date(t + 60 * config.getRefreshTime()))
        .withClaim(EXPIRED_AT, new Date(t + 60 * config.getExpiredTime()))
        .withIssuedAt(new Date()).sign(algorithm);
  }

  private DecodedJWT decodeToken(String token, String secret)
      throws UnsupportedEncodingException {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm).build();
      return verifier.verify(token);
    } catch (SignatureVerificationException signatureVerificationException) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID, signatureVerificationException);
    }
  }


}
