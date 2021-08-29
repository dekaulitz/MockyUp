package com.github.dekaulitz.mockyup.server.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isTrue;

import com.github.dekaulitz.mockyup.server.db.entities.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.domain.auth.vmodels.DoAuthVmodel;
import com.github.dekaulitz.mockyup.server.domain.auth.vmodels.DtoAuthProfileVmodel;
import com.github.dekaulitz.mockyup.server.errors.vmodels.ResponseVmodel;
import com.github.dekaulitz.mockyup.server.helperTest.Helper;
import com.github.dekaulitz.mockyup.server.model.constants.Role;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.HashingHelper;
import com.github.dekaulitz.mockyup.server.utils.ResponseCode;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest extends BaseTest {

  String givenId = "X";
  String givenUserName = "root";
  String givenPassword = "root";

  @Test
  void login() {
    List<String> givenAccesses = new ArrayList<>();
    givenAccesses.add(Role.USERS_READ_WRITE.name());
    givenAccesses.add(Role.MOCKS_READ_WRITE.name());
    when(this.userRepository.findFirstByUsername(any())).thenReturn(UserEntities.builder()
        .id(givenId)
        .username(givenUserName)
        .password(HashingHelper.hashing(givenPassword))
        .accessList(givenAccesses)
        .build());
    DoAuthVmodel authVmodel = new DoAuthVmodel();
    authVmodel.setUsername("root");
    authVmodel.setPassword("fahmi");

    //when user login with wrong username or password
    HttpEntity<DoAuthVmodel> request = new HttpEntity<>(authVmodel);
    ResponseEntity<ResponseVmodel> response1 = this.restTemplate
        .postForEntity(baseUrl + "/mocks/login", request, ResponseVmodel.class);
    isTrue(response1.getStatusCodeValue() == ResponseCode.INVALID_USERNAME_OR_PASSWORD.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(response1.getBody().getResponseCode()
            .equals(ResponseCode.INVALID_USERNAME_OR_PASSWORD.getErrorCode()),
        RESPONSE_BODY_NOT_EXPECTED);

    //when user login with correct credential
    authVmodel.setPassword(givenPassword);
    authVmodel.setUsername(givenUserName);
    request = new HttpEntity<>(authVmodel);
    ResponseEntity<DtoAuthProfileVmodel> response2 = this.restTemplate
        .postForEntity(baseUrl + "/mocks/login", request, DtoAuthProfileVmodel.class);
    isTrue(response2.getStatusCode().is2xxSuccessful(), HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getAccessMenus().equals(givenAccesses), RESPONSE_BODY_NOT_EXPECTED);
    isTrue(response2.getBody().getUsername().equals(givenUserName), RESPONSE_BODY_NOT_EXPECTED);
    isTrue(!response2.getBody().getToken().isEmpty(), RESPONSE_BODY_NOT_EXPECTED);

    //when user login with user not found
    when(this.userRepository.findFirstByUsername(any())).thenReturn(null);
    ResponseEntity<ResponseVmodel> response3 = this.restTemplate
        .postForEntity(baseUrl + "/mocks/login", request, ResponseVmodel.class);
    isTrue(response3.getStatusCodeValue() == ResponseCode.INVALID_USERNAME_OR_PASSWORD.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(response3.getBody().getResponseCode()
            .equals(ResponseCode.INVALID_USERNAME_OR_PASSWORD.getErrorCode()),
        RESPONSE_BODY_NOT_EXPECTED);


  }

  @Test
  void refreshToken() throws UnsupportedEncodingException {
    List<String> givenAccesses = new ArrayList<>();
    givenAccesses.add(Role.USERS_READ_WRITE.name());
    givenAccesses.add(Role.MOCKS_READ_WRITE.name());
    when(this.userRepository.findById(any()))
        .thenReturn(java.util.Optional.ofNullable(UserEntities.builder()
            .id(givenId)
            .username(givenUserName)
            .password(HashingHelper.hashing(givenPassword))
            .accessList(givenAccesses)
            .build()));
    String token = Helper.generateToken(givenId, 1000, 1000);

    //when user do refresh with valid token
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token);
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<DtoAuthProfileVmodel> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/auth/refresh", HttpMethod.GET, request1,
            DtoAuthProfileVmodel.class);
    isTrue(response1.getStatusCode().is2xxSuccessful(), HTTPCODE_NOT_EXPECTED);
    isTrue(response1.getBody().getAccessMenus().equals(givenAccesses), RESPONSE_BODY_NOT_EXPECTED);
    isTrue(response1.getBody().getUsername().equals(givenUserName), RESPONSE_BODY_NOT_EXPECTED);
    isTrue(!response1.getBody().getToken().isEmpty(), RESPONSE_BODY_NOT_EXPECTED);

    //when user do refresh without userId on token or invalid token
    HttpHeaders headers2 = new HttpHeaders();
    headers2.set("Authorization", "Bearer " + Helper.generateInvalidToken("xxx", 10, 3));
    HttpEntity<HttpHeaders> request2 = new HttpEntity<>(headers2);
    ResponseEntity<Object> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/auth/refresh", HttpMethod.GET, request2, Object.class);
    isTrue(response2.getStatusCode() == ResponseCode.TOKEN_INVALID.getHttpCode(),
        HTTPCODE_NOT_EXPECTED);
//        isTrue(response2.getBody().getResponseMessage().equals(ResponseCode.TOKEN_INVALID.getErrorMessage()), RESPONSE_BODY_NOT_EXPECTED);

    //when user do refresh with expired token
    HttpHeaders headers3 = new HttpHeaders();
    headers3.set("Authorization", "Bearer " + Helper.generateExpiredToken(givenId));
    HttpEntity<HttpHeaders> request3 = new HttpEntity<>(headers3);
    ResponseEntity<ResponseVmodel> response3 = this.restTemplate
        .exchange(baseUrl + "/mocks/auth/refresh", HttpMethod.GET, request3, ResponseVmodel.class);
    isTrue(response3.getStatusCode() == ResponseCode.TOKEN_EXPIRED.getHttpCode(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(response3.getBody().getResponseMessage()
        .equals(ResponseCode.TOKEN_EXPIRED.getErrorMessage()), RESPONSE_BODY_NOT_EXPECTED);

    //when user do refresh and user not found
    headers3 = new HttpHeaders();
    headers3.set("Authorization", "");
    request3 = new HttpEntity<>(headers3);
    response3 = this.restTemplate
        .exchange(baseUrl + "/mocks/auth/refresh", HttpMethod.GET, request3, ResponseVmodel.class);
    isTrue(response3.getStatusCode() == ResponseCode.TOKEN_INVALID.getHttpCode(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(response3.getBody().getResponseMessage()
        .equals(ResponseCode.TOKEN_INVALID.getErrorMessage()), RESPONSE_BODY_NOT_EXPECTED);

    when(this.userRepository.findById("notFound")).thenReturn(Optional.ofNullable(null));
    //when user do refresh and user not found
    HttpHeaders headers4 = new HttpHeaders();
    headers4.set("Authorization", "Bearer " + Helper.generateToken("notFound", 10, 10));
    HttpEntity<HttpHeaders> request4 = new HttpEntity<>(headers4);
    ResponseEntity<ResponseVmodel> response4 = this.restTemplate
        .exchange(baseUrl + "/mocks/auth/refresh", HttpMethod.GET, request4, ResponseVmodel.class);
    isTrue(response4.getStatusCode() == ResponseCode.TOKEN_INVALID.getHttpCode(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(response4.getBody().getResponseMessage()
        .equals(ResponseCode.TOKEN_INVALID.getErrorMessage()), RESPONSE_BODY_NOT_EXPECTED);
  }
}
