package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.features.constants.DevStockEnum;
import com.github.dekaulitz.mockyup.server.errors.UnauthorizedException;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import java.util.Date;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OpenApiCommonHelperTest {

  @Test
  void getContentType() {
    String contentType = "Application/json";
    OpenApiContentType contentTypeResult = OpenApiCommonHelper.getContentType(contentType);
    Assertions.assertThat(contentTypeResult)
        .isEqualByComparingTo(OpenApiContentType.APPLICATION_JSON);
    contentType = "Application/xml";
    contentTypeResult = OpenApiCommonHelper.getContentType(contentType);
    Assertions.assertThat(contentTypeResult)
        .isEqualByComparingTo(OpenApiContentType.APPLICATION_XML);
  }

  @Test
  void checkEnum() {
    Assertions.assertThat(DevStockEnum.get("x-mock-example"))
        .isEqualTo(DevStockEnum.MOCKING_REQUEST);
  }

  @Test
  void something() {
    Algorithm algorithm = Algorithm.HMAC256("something");
    String jti =UUID.randomUUID().toString();
    String token = JWT.create()
        .withJWTId(jti)
        .withIssuedAt(new Date()).sign(algorithm);
    DecodedJWT decodedJWT = decodeToken(token,"something");
    System.out.println(token);
    System.out.println(jti);
    System.out.println(decodedJWT.getId());
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
}
