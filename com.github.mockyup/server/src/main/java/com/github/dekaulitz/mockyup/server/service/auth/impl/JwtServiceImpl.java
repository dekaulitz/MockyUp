package com.github.dekaulitz.mockyup.server.service.auth.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.UnauthorizedException;
import com.github.dekaulitz.mockyup.server.model.constants.CacheConstants;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.service.auth.api.JwtService;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang3.time.DateUtils;
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
  @Value("${mock.auth.refresh.time}")
  private Long refreshTime;
  @Value("${mock.auth.expired.time}")
  private Long expired;

  @Autowired
  private CacheService cacheService;

  @Override
  public AuthProfileModel generateToken(UserEntity userEntity, Boolean rememberMe) {
    if (!userEntity.isAccountNonLocked() || !userEntity.isEnabled()) {
      throw new UnauthorizedException(ResponseCode.USER_DISABLED);
    }
    String jti = UUID.randomUUID().toString();
    Long expiredTime = expired;
    if (rememberMe) {
      expiredTime = expiredTime * 100;
    }
    Date canDoRefreshTime = DateUtils.addMilliseconds(new Date(), Math.toIntExact(refreshTime));
    Date tokenExpireTime = DateUtils.addDays(new Date(), Math.toIntExact(expiredTime));
    //@TODO i think it should more than this that we can keep user data on token
    String token = buildToken(jti, canDoRefreshTime, tokenExpireTime);
    AuthProfileModel authProfileModel = new AuthProfileModel();
    authProfileModel.setJti(jti);
    authProfileModel.setToken(token);
    authProfileModel.setId(userEntity.getId());
    authProfileModel.setUsername(userEntity.getUsername());
    authProfileModel.setAccess(userEntity.getAccess());
    authProfileModel.setAccountNonLocked(userEntity.isAccountNonLocked());
    authProfileModel.setEnabled(userEntity.isEnabled());
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
        String cacheKey = CacheConstants.AUTH_PREFIX + jwt.getId();
        cacheService.deleteCache(cacheKey);
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

  @Override
  public String invalidateToken(String token) {
    DecodedJWT jwt = decodeToken(token, secret);
    String cacheKey = CacheConstants.AUTH_PREFIX + jwt.getId();
    cacheService.deleteCache(cacheKey);
    return jwt.getId();
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
