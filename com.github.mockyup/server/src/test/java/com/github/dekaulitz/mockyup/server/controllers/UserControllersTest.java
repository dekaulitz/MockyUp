package com.github.dekaulitz.mockyup.server.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isNull;
import static org.springframework.util.Assert.isTrue;

import com.github.dekaulitz.mockyup.server.db.entities.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.domain.users.vmodels.RegistrationResponseVmodel;
import com.github.dekaulitz.mockyup.server.domain.users.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.server.domain.users.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.server.errors.vmodels.ResponseVmodel;
import com.github.dekaulitz.mockyup.server.helperTest.Helper;
import com.github.dekaulitz.mockyup.server.utils.ResponseCode;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllersTest extends BaseTest {

  @BeforeEach
  void setUp() {
    super.setup();
  }

  @Test
  void adduser() throws UnsupportedEncodingException {
    when(this.userRepository.save(any())).thenReturn(Helper.getUserEntities());
    when(this.userRepository.existsByUsername(any())).thenReturn(false);

    RegistrationVmodel registrationVmodel = Helper.getreRegistrationVmodel();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<RegistrationVmodel> headersHttpEntity = new HttpEntity<>(registrationVmodel,
        headers);
    ResponseEntity<RegistrationResponseVmodel> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/addUser", HttpMethod.POST, headersHttpEntity,
            RegistrationResponseVmodel.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, HTTPCODE_NOT_EXPECTED);

    //user do request but user alrady exist on database
    UserEntities userEntities = Helper.getUserEntities();
    userEntities.setUsername("DUPLICATEENTRY");
    when(this.userRepository.existsByUsername("DUPLICATEENTRY")).thenReturn(true);
    registrationVmodel.setUsername("DUPLICATEENTRY");

    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<RegistrationVmodel> request2 = new HttpEntity<>(registrationVmodel, headers);
    ResponseEntity<ResponseVmodel> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/addUser", HttpMethod.POST, request2, ResponseVmodel.class);
    isTrue(response2.getStatusCodeValue() == ResponseCode.USER_ALREADY_EXIST.getHttpCode().value()
        , HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseCode()
        .equals(ResponseCode.USER_ALREADY_EXIST.getErrorCode()), RESPONSE_BODY_NOT_EXPECTED);
  }

  @Test
  void deleteUser() throws UnsupportedEncodingException {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
    ResponseEntity<Object> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/users/x/delete", HttpMethod.DELETE, headersHttpEntity,
            Object.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, HTTPCODE_NOT_EXPECTED);

    //when user do request but user not found
    when(this.userRepository.findById("y")).thenReturn(java.util.Optional.ofNullable(null));
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    headersHttpEntity = new HttpEntity<>(headers);
    ResponseEntity<ResponseVmodel> responseEntity2 = this.restTemplate
        .exchange(baseUrl + "/mocks/users/y/delete", HttpMethod.DELETE, headersHttpEntity,
            ResponseVmodel.class);
    isTrue(
        responseEntity2.getStatusCodeValue() == ResponseCode.USER_NOT_FOUND.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(responseEntity2.getBody().getResponseCode()
        .equals(ResponseCode.USER_NOT_FOUND.getErrorCode()), RESPONSE_BODY_NOT_EXPECTED);


  }

  @Test
  void getUsersPagination() throws UnsupportedEncodingException {
    when(this.userRepository.paging(any(), any())).thenReturn(Helper.getUserEntitiesPage());
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
    ResponseEntity<Object> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/users", HttpMethod.GET, headersHttpEntity, Object.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, HTTPCODE_NOT_EXPECTED);

    //when user do request but something bad happen
    when(this.userRepository.paging(any(), any()))
        .thenThrow(new RuntimeException("something bad happen"));
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    headersHttpEntity = new HttpEntity<>(headers);
    ResponseEntity<ResponseVmodel> response = this.restTemplate
        .exchange(baseUrl + "/mocks/users", HttpMethod.GET, headersHttpEntity,
            ResponseVmodel.class);
    isTrue(response.getStatusCodeValue() == HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(response.getBody().getResponseCode()
        .equals(ResponseCode.GLOBAL_ERROR_MESSAGE.getErrorCode()), RESPONSE_BODY_NOT_EXPECTED);
    isTrue(response.getBody().getResponseMessage().equals("something bad happen"),
        RESPONSE_BODY_NOT_EXPECTED);
  }

  @Test
  void getUserList() throws UnsupportedEncodingException {
    when(this.userRepository.getUserListByUserName(any(), any()))
        .thenReturn(Helper.getUserEntitiesList());
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
    ResponseEntity<Object> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/users/list?username=test", HttpMethod.GET, headersHttpEntity,
            Object.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, HTTPCODE_NOT_EXPECTED);

    //when user do request but something happen
    when(this.userRepository.getUserListByUserName(any(), any()))
        .thenThrow(new RuntimeException("something bad"));
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    headersHttpEntity = new HttpEntity<>(headers);
    ResponseEntity<ResponseVmodel> response = this.restTemplate
        .exchange(baseUrl + "/mocks/users/list?username=test", HttpMethod.GET, headersHttpEntity,
            ResponseVmodel.class);
    isTrue(response.getStatusCodeValue() == HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(response.getBody().getResponseMessage().equals("something bad"),
        RESPONSE_BODY_NOT_EXPECTED);
    isTrue(response.getBody().getResponseCode()
        .equals(ResponseCode.GLOBAL_ERROR_MESSAGE.getErrorCode()));
  }

  @Test
  void updateUsers() throws UnsupportedEncodingException {
    when(this.userRepository.findById("y"))
        .thenReturn(java.util.Optional.ofNullable(Helper.getUserEntities()));
    when(this.userRepository.save(any())).thenReturn(Helper.getUserEntities());
    UpdateUserVmodel updateUserVmodel = Helper.getUpdateUserVmodel();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<UpdateUserVmodel> headersHttpEntity = new HttpEntity<>(updateUserVmodel, headers);
    ResponseEntity<UserEntities> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/users/y/update", HttpMethod.PUT, headersHttpEntity,
            UserEntities.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, HTTPCODE_NOT_EXPECTED);
    isNull(responseEntity.getBody().getPassword(), RESPONSE_BODY_NOT_EXPECTED);

    UserEntities userEntities = Helper.getUserEntities();
    userEntities.setUsername("DUPLICATE");
    userEntities.setId("ID");
    updateUserVmodel.setUsername("DUPLICATE");
    //when user do request but username is alrady exist
    when(this.userRepository.findFirstByUsername("DUPLICATE")).thenReturn(userEntities);

    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<UpdateUserVmodel> request = new HttpEntity<>(updateUserVmodel, headers);
    ResponseEntity<ResponseVmodel> response = this.restTemplate
        .exchange(baseUrl + "/mocks/users/x/update", HttpMethod.PUT, request, ResponseVmodel.class);
    isTrue(response.getStatusCodeValue() == ResponseCode.USER_ALREADY_EXIST.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(
        response.getBody().getResponseCode().equals(ResponseCode.USER_ALREADY_EXIST.getErrorCode()),
        RESPONSE_BODY_NOT_EXPECTED);

  }

  @Test
  void getUserById() throws UnsupportedEncodingException {
    when(this.userRepository.findById("x"))
        .thenReturn(java.util.Optional.ofNullable(Helper.getUserEntities()));
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
    ResponseEntity<Object> responseEntity = this.restTemplate
        .exchange(baseUrl + "mocks/users/x/detail", HttpMethod.GET, headersHttpEntity,
            Object.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, HTTPCODE_NOT_EXPECTED);

    //when user do request but data not found
    when(this.userRepository.findById("y")).thenReturn(java.util.Optional.ofNullable(null));

    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);
    ResponseEntity<ResponseVmodel> response = this.restTemplate
        .exchange(baseUrl + "mocks/users/y/detail", HttpMethod.GET, headersHttpEntity,
            ResponseVmodel.class);
    isTrue(response.getStatusCodeValue() == ResponseCode.USER_NOT_FOUND.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(response.getBody().getResponseCode().equals(ResponseCode.USER_NOT_FOUND.getErrorCode()),
        RESPONSE_BODY_NOT_EXPECTED);


  }
}
