package com.github.dekaulitz.mockyup.server.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.dekaulitz.mockyup.server.tmp.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.server.utils.ResponseCode;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <pre>this is jwt manager for generate token and validate token</pre>
 */
@Service
public class JwtManager {

  public static final String CAN_DO_REFRESH = "canRefresh";
  public static final String EXPIRED_AT = "expiredAt";

  @Value("${com.github.dekaulitz.mockup.auth.secret}")
  private String secret;
  @Value("${com.github.dekaulitz.mockup.auth.refresh}")
  private Long refreshTime;
  @Value("${com.github.dekaulitz.mockup.auth.expired}")
  private Long expiredTime;


  public String getAuthorizationHeader(String AuthHeader) {
    if (AuthHeader == null || !AuthHeader.startsWith("Bearer")) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
    return AuthHeader.substring(7);
  }

  public JwtProfileModel generateToken(String userId)
      throws UnsupportedEncodingException {
    JwtProfileModel jwtProfileModel = new JwtProfileModel();
    jwtProfileModel.setId(userId);
    jwtProfileModel.setAccessToken(buildToken(userId));
    return jwtProfileModel;
  }

  public JwtProfileModel validateToken(String token) {
    try {
      DecodedJWT jwt = decodeToken(token, secret);
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
      JwtProfileModel jwtUserModel = new JwtProfileModel();
      jwtUserModel.setId(jwt.getClaim("id").asString());
      return jwtUserModel;
    } catch (JWTVerificationException | UnsupportedEncodingException e) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_INVALID);
    }
  }

  public Optional<String> getUserIdFromToken(String token)
      throws UnsupportedEncodingException {
    DecodedJWT jwt = decodeToken(token, secret);
    Date canRefresh = jwt.getClaim(CAN_DO_REFRESH).asDate();
    Date expiredAt = jwt.getClaim(EXPIRED_AT).asDate();
    long currentTime = System.currentTimeMillis();
    if ((expiredAt.getTime() < currentTime) && (canRefresh.getTime() <= currentTime)) {
      throw new UnathorizedAccess(ResponseCode.TOKEN_EXPIRED);
    }
    return Optional.ofNullable(jwt.getClaim("id").asString());
  }

  public String buildToken(String id)
      throws UnsupportedEncodingException {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    return JWT.create()
        .withClaim("id", id)
        .withClaim(CAN_DO_REFRESH, new Date(t + 60 * refreshTime))
        .withClaim(EXPIRED_AT, new Date(t + 60 * expiredTime))
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
