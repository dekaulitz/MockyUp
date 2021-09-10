package com.github.dekaulitz.mockyup.server.service.auth.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.github.dekaulitz.mockyup.server.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.server.errors.UnauthorizedException;
import com.github.dekaulitz.mockyup.server.model.constants.CacheConstants;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.service.auth.api.JwtService;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <pre>this is jwt manager for generate token and validate token</pre>
 */
@Service
public class JwtServiceImpl implements JwtService {

  @Value("${com.github.dekaulitz.mockup.auth.secret}")
  private String secret;
  @Value("${com.github.dekaulitz.mockup.auth.refresh}")
  private Long refreshTime;
  @Value("${com.github.dekaulitz.mockup.auth.expired}")
  private Long expiredTime;

  @Autowired
  private CacheService cacheService;

  @Override
  public AuthProfileModel generateToken(UserEntities userEntities, Boolean rememberMe) {
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    if (rememberMe) {
      expiredTime = expiredTime * 1000;
    }
    Date canDoRefreshTime = new Date(t + 60 * refreshTime);
    Date tokenExpireTime = new Date(t + 60 * expiredTime);
    //@TODO i think it should more than this that we can keep user data on token
    String jti = UUID.randomUUID().toString();
    String token = buildToken(jti, canDoRefreshTime, tokenExpireTime);
    AuthProfileModel authProfileModel = new AuthProfileModel();
    authProfileModel.setToken(token);
    authProfileModel.setId(userEntities.getId());
    authProfileModel.setUsername(userEntities.getUsername());
    String cacheKey = CacheConstants.AUTH_PREFIX + jti;
    return (AuthProfileModel) cacheService
        .createCache(cacheKey, authProfileModel, canDoRefreshTime.getTime());
  }

  @Override
  public AuthProfileModel validateToken(String token) throws UnauthorizedException {
    DecodedJWT jwt = decodeToken(token, secret);
    Date canRefresh = jwt.getClaim(CAN_DO_REFRESH).asDate();
    Date expiredAt = jwt.getExpiresAt();
    long currentTime = System.currentTimeMillis();
    if (expiredAt.getTime() < currentTime) {
      if (canRefresh.getTime() >= currentTime) {
        throw new UnauthorizedException(ResponseCode.REFRESH_TOKEN_REQUIRED);
      } else {
        throw new UnauthorizedException(ResponseCode.TOKEN_NOT_VALID);
      }
    }
    String cacheKey = CacheConstants.AUTH_PREFIX + jwt.getId();
    AuthProfileModel authProfile = cacheService.findCacheByKey(cacheKey, AuthProfileModel.class);
    if (authProfile == null) {
      throw new UnauthorizedException(ResponseCode.TOKEN_NOT_VALID);
    }
    return authProfile;
  }


  private DecodedJWT decodeToken(String token, String secret)
      throws UnauthorizedException {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    JWTVerifier verifier = JWT.require(algorithm).build();
    try {
      return verifier.verify(token);
    } catch (JWTVerificationException jwtVerificationException) {
      throw new UnauthorizedException(ResponseCode.TOKEN_NOT_VALID);
    }
  }

  private String buildToken(String jti, Date refreshTime, Date expiredTime) {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    return JWT.create()
        .withJWTId(jti)
        .withClaim(CAN_DO_REFRESH, refreshTime)
        .withExpiresAt(expiredTime)
        .withIssuedAt(new Date()).sign(algorithm);
  }
}
