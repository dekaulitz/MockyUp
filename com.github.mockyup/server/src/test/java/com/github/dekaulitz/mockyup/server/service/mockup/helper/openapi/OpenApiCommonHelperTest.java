package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import static com.github.dekaulitz.mockyup.server.model.constants.Role.MOCKS_READ;
import static com.github.dekaulitz.mockyup.server.model.constants.Role.MOCKS_READ_WRITE;
import static com.github.dekaulitz.mockyup.server.model.constants.Role.PROJECT_READ;
import static com.github.dekaulitz.mockyup.server.model.constants.Role.PROJECT_READ_WRITE;
import static com.github.dekaulitz.mockyup.server.model.constants.Role.USERS_READ;
import static com.github.dekaulitz.mockyup.server.model.constants.Role.USERS_READ_WRITE;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.db.entities.features.constants.DevStockEnum;
import com.github.dekaulitz.mockyup.server.errors.UnauthorizedException;
import com.github.dekaulitz.mockyup.server.model.constants.Role;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

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
  void something2() {
    Set<Role> accessRole = new HashSet<>(Arrays
        .asList(MOCKS_READ, MOCKS_READ_WRITE, USERS_READ, USERS_READ_WRITE, PROJECT_READ,
            PROJECT_READ_WRITE));
    List<String> roles = accessRole.stream().map(role -> role.toString())
        .collect(Collectors.toList());
    List<GrantedAuthority> grantedAuthorities = AuthorityUtils
        .commaSeparatedStringToAuthorityList(String.join(",", roles));

    System.out.println(grantedAuthorities.toString());
  }

  @Test
  void something() {
    Algorithm algorithm = Algorithm.HMAC256("something");
    String jti = UUID.randomUUID().toString();
    String token = JWT.create()
        .withJWTId(jti)
        .withIssuedAt(new Date()).sign(algorithm);
    DecodedJWT decodedJWT = decodeToken(token, "something");
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
