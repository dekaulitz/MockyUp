package com.github.dekaulitz.mockyup.server.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isTrue;

import com.github.dekaulitz.mockyup.server.db.entities.v1.MockEntities;
import com.github.dekaulitz.mockyup.server.db.entities.v1.MockHistoryEntities;
import com.github.dekaulitz.mockyup.server.db.repositories.MockHistoryRepository;
import com.github.dekaulitz.mockyup.server.db.repositories.MockRepository;
import com.github.dekaulitz.mockyup.server.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.server.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.server.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.server.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.server.errors.vmodels.ResponseVmodel;
import com.github.dekaulitz.mockyup.server.helperTest.Helper;
import com.github.dekaulitz.mockyup.server.model.constants.Role;
import com.github.dekaulitz.mockyup.server.utils.ResponseCode;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MockControllersTest extends BaseTest {

  @MockBean
  private MockRepository mockRepository;

  @MockBean
  private MockHistoryRepository mockHistoryRepository;


  @BeforeEach
  void setUp() throws IOException {
    super.setup();
  }

  @Test
  void greeting() throws UnsupportedEncodingException {
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/docs", HttpMethod.GET, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);
  }


  @Test
  void mockingPath() throws IOException {
    //when user do mock request with invalid path but using correct mockId
    //mock id was exist but the path contract was wrong
    this.getMockingPathNotFound();

    //when user do mock request wiht valid path and correct mockId
    //path mock contract path=/books with query method get
    this.getMocksMockingIdPathBooksQuery();

    //when user do mock request with valid path and correct mockid
    //path mock contract path=/mocks/mocking/id?path=/books method post
    this.getMocksMockingIdPathStoreBooks();

    //when user do mock request with valid path and correct mockid
    //path mock contract path=/mocks/mocking/id?path=/books/{ID) method post
    this.getMocksMockingIdBookDetail();

    //when user do mock request with valid path and correct mockid
    //path mock contract path=/mocks/mocking/id?path=/books/x/update method put
    this.getMocksMockingDetailUpdate();

    this.getMocksCategoriesCatIdBooksBookId();

    //when user do refresh with valid token
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(null));
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    ResponseEntity<ResponseVmodel> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/x?path=asdasd", HttpMethod.GET, request1,
            ResponseVmodel.class);
    isTrue(response1.getStatusCode().value() == ResponseCode.MOCKUP_NOT_FOUND.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(
        response1.getBody().getResponseCode().equals(ResponseCode.MOCKUP_NOT_FOUND.getErrorCode()),
        RESPONSE_BODY_NOT_EXPECTED);


  }

  @Test
  void getUserMocks() throws UnsupportedEncodingException {
    //given
    List<DtoMockUserLookupVmodel> given = Helper.getDtoMockUserLookupVmodelList();
    when(this.mockRepository.getUsersMock(any())).thenReturn(given);
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/users", HttpMethod.GET, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);
    List<DtoMockUserLookupVmodel> response = (List<DtoMockUserLookupVmodel>) response1.getBody();
    isTrue(response.size() == 2, RESPONSE_BODY_NOT_EXPECTED);
  }

  @Test
  void getCurrentAccess() throws IOException {
    //given
    List<DtoMockupDetailVmodel> given = Helper.getDtoMockupDetailVmodels();
    when(this.mockRepository.getMockDetailWithCurrentAccess(any(), any())).thenReturn(given);
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/detailWithAccess", HttpMethod.GET, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);
    when(this.mockRepository.getMockDetailWithCurrentAccess(any(), any()))
        .thenReturn(new ArrayList<>());

    //when user do request with not data found
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    ResponseEntity<ResponseVmodel> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/detailWithAccess", HttpMethod.GET, request1,
            ResponseVmodel.class);
    isTrue(response2.getStatusCode() == ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseMessage()
            .equals(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorMessage()),
        RESPONSE_BODY_NOT_EXPECTED);
  }

  @Test
  void getMockHistories() throws IOException {
    //give
    List<MockHistoryEntities> given = Helper.getMockHistoryEntitiesList();
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/histories", HttpMethod.GET, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);
  }

  @Test
  void addUserAccess() throws IOException {
    //given
    MockEntities mockEntities = Helper.getMockEntities();
    mockEntities.getUsers().forEach(userMocksEntities -> {
      userMocksEntities.setUserId(givenId);
      userMocksEntities.setAccess(Role.MOCKS_READ_WRITE.name());
    });
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));
    when(this.mockRepository.registeringUserToMock(any(), any(), any()))
        .thenReturn(Helper.getMockEntities());
    AddUserAccessVmodel addUserAccessVmodel = Helper.getAddUserAccessVmodel();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<AddUserAccessVmodel> request1 = new HttpEntity<>(addUserAccessVmodel, headers);
    ResponseEntity<MockEntities> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/addUser", HttpMethod.PUT, request1, MockEntities.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);
    isTrue(response1.getBody().getId().equals(mockEntities.getId()), RESPONSE_BODY_NOT_EXPECTED);

    //when user do request but mock not found
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(null));
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(addUserAccessVmodel, headers);
    ResponseEntity<ResponseVmodel> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/addUser", HttpMethod.PUT, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.MOCKUP_NOT_FOUND.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(
        response2.getBody().getResponseCode().equals(ResponseCode.MOCKUP_NOT_FOUND.getErrorCode()),
        HTTPCODE_NOT_EXPECTED);

    //when user do request but mock not found
    mockEntities.getUsers().forEach(userMocksEntities -> {
      userMocksEntities.setUserId(givenId);
      userMocksEntities.setAccess(Role.MOCKS_READ.name());
    });

    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(addUserAccessVmodel, headers);
    response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/addUser", HttpMethod.PUT, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseCode()
        .equals(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorCode()), HTTPCODE_NOT_EXPECTED);

  }

  @Test
  void removeUserAccessOnMock() throws IOException {
    //given
    MockEntities mockEntities = Helper.getMockEntities();
    mockEntities.getUsers().forEach(userMocksEntities -> {
      userMocksEntities.setUserId(givenId);
      userMocksEntities.setAccess(Role.MOCKS_READ_WRITE.name());
    });
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/remove/x", HttpMethod.DELETE, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);

    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(null));
    //when user do request but mock not found
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    ResponseEntity<ResponseVmodel> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/remove/x", HttpMethod.DELETE, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.MOCKUP_NOT_FOUND.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(
        response2.getBody().getResponseCode().equals(ResponseCode.MOCKUP_NOT_FOUND.getErrorCode()),
        RESPONSE_BODY_NOT_EXPECTED);

    mockEntities.getUsers().forEach(userMocksEntities -> {
      userMocksEntities.setAccess(Role.MOCKS_READ.name());
    });
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/remove/x", HttpMethod.DELETE, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseCode()
        .equals(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorCode()), RESPONSE_BODY_NOT_EXPECTED);


  }

  @Test
  void getSpecHistory() throws IOException {
    //given
    when(this.mockRepository.checkMockUserAccessPermission(any(), any()))
        .thenReturn(Helper.getMockEntities());
    when(this.mockHistoryRepository.getMockHistoryByIdAndMockId(any(), any()))
        .thenReturn(Helper.getDtoMockupHistoryVmodel());

    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/histories/x", HttpMethod.GET, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);

    //given
    when(this.mockRepository.checkMockUserAccessPermission(any(), any())).thenReturn(null);
    //when user do request but there no spec history base on user access
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    ResponseEntity<ResponseVmodel> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/histories/x", HttpMethod.GET, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseCode()
        .equals(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorCode()), HTTPCODE_NOT_EXPECTED);

    //given
    when(this.mockRepository.checkMockUserAccessPermission(any(), any()))
        .thenReturn(Helper.getMockEntities());
    when(this.mockHistoryRepository.getMockHistoryByIdAndMockId(any(), any())).thenReturn(null);
    //when user do request but there no spec history base on user access
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/histories/x", HttpMethod.GET, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.MOCKUP_NOT_FOUND.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(
        response2.getBody().getResponseCode().equals(ResponseCode.MOCKUP_NOT_FOUND.getErrorCode()),
        HTTPCODE_NOT_EXPECTED);


  }

  @Test
  void storeMocksEntity() throws IOException {
    //given /mocks/store
    MockVmodel mockVmodel = Helper.getMockVmodel();
    when(this.mockRepository.save(any())).thenReturn(Helper.getMockEntities());

    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<MockVmodel> request1 = new HttpEntity<>(mockVmodel, headers);
    ResponseEntity<MockVmodel> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/store", HttpMethod.POST, request1, MockVmodel.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);
    isTrue(response1.getBody().getTitle().equals(Helper.MOCK_TITLE), RESPONSE_BODY_NOT_EXPECTED);

    //when user do request and return exception
    when(this.mockRepository.save(any())).thenThrow(new RuntimeException("something"));
    ResponseEntity<ResponseVmodel> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/store", HttpMethod.POST, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.INVALID_MOCKUP_STRUCTURE.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseCode()
        .equals(ResponseCode.INVALID_MOCKUP_STRUCTURE.getErrorCode()), HTTPCODE_NOT_EXPECTED);
  }

  @Test
  void healthCheck() {
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<String> response1 = this.restTemplate
        .exchange(baseUrl + "/health", HttpMethod.GET, request1, String.class);
    isTrue(response1.getStatusCode().is2xxSuccessful(), HTTPCODE_NOT_EXPECTED);

  }

  @Test
  void deleteByMockId() throws IOException {
    //given
    when(this.mockRepository.findById(any()))
        .thenReturn(java.util.Optional.of(Helper.getMockEntities()));
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/delete", HttpMethod.DELETE, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);

    //given
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(null));

    //when user do request and mockId not found
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/delete", HttpMethod.DELETE, request1, Object.class);
    isTrue(response1.getStatusCode().value() == ResponseCode.MOCKUP_NOT_FOUND.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);

  }

  @Test
  void getMockSpecById() throws IOException {
    //given
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(Helper.getMockEntities()));
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/spec", HttpMethod.GET, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);

    //given
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(null));
    //when user do request and mockup not found
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    ResponseEntity<ResponseVmodel> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/spec", HttpMethod.GET, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.MOCKUP_NOT_FOUND.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(
        response2.getBody().getResponseCode().equals(ResponseCode.MOCKUP_NOT_FOUND.getErrorCode()),
        RESPONSE_BODY_NOT_EXPECTED);

    //given
    MockEntities mockEntities = Helper.getMockEntities();
    mockEntities.getUsers().forEach(userMocksEntities -> {
      userMocksEntities.setUserId("not valid user id");
    });

    //when user do request and mockup not found
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));

    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/spec", HttpMethod.GET, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseCode()
        .equals(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorCode()), RESPONSE_BODY_NOT_EXPECTED);

    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));
    //when user do request and mockup not found
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/spec", HttpMethod.GET, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseCode()
        .equals(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorCode()), RESPONSE_BODY_NOT_EXPECTED);

  }

  @Test
  void updateMockById() throws IOException {
    //given
    MockVmodel mockVmodel = Helper.getMockVmodel();
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(Helper.getMockEntities()));
    when(this.mockHistoryRepository.save(any())).thenReturn(Helper.getMockHistoryEntities());
    when(this.mockRepository.save(any())).thenReturn(Helper.getMockEntities());
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<MockVmodel> request1 = new HttpEntity<>(mockVmodel, headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/update", HttpMethod.PUT, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);

    //when user do request and mockid was not found
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(null));
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(mockVmodel, headers);
    ResponseEntity<ResponseVmodel> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/update", HttpMethod.PUT, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.MOCKUP_NOT_FOUND.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(
        response2.getBody().getResponseCode().equals(ResponseCode.MOCKUP_NOT_FOUND.getErrorCode()),
        HTTPCODE_NOT_EXPECTED);

    //when user do request and not have access
    MockEntities mockEntities1 = Helper.getMockEntities();
    mockEntities1.getUsers().forEach(userMocksEntities -> {
      userMocksEntities.setAccess(Role.MOCKS_READ.name());
    });
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities1));

    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(mockVmodel, headers);
    response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/x/update", HttpMethod.PUT, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseCode()
        .equals(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorCode()), RESPONSE_BODY_NOT_EXPECTED);

  }

  @Test
  void getMocksPagination() throws IOException {
    //given
    when(this.mockRepository.paging(any(), any(), any())).thenReturn(Helper.getMockEntitiesPage());
    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<HttpHeaders> request1 = new HttpEntity<>(headers);
    ResponseEntity<Object> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/page", HttpMethod.GET, request1, Object.class);
    isTrue(response1.getStatusCode().value() == 200, HTTPCODE_NOT_EXPECTED);

    //given
    when(this.mockRepository.paging(any(), any(), any()))
        .thenThrow(new RuntimeException("something bad"));
    //when user do request
    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    request1 = new HttpEntity<>(headers);
    ResponseEntity<ResponseVmodel> response2 = this.restTemplate
        .exchange(baseUrl + "/mocks/page", HttpMethod.GET, request1, ResponseVmodel.class);
    isTrue(response2.getStatusCode().value() == ResponseCode.GLOBAL_ERROR_MESSAGE.getHttpCode()
        .value(), HTTPCODE_NOT_EXPECTED);
    isTrue(!response2.getBody().getRequestId().isEmpty(), RESPONSE_BODY_NOT_EXPECTED);
    isTrue(response2.getBody().getResponseCode()
        .equals(ResponseCode.GLOBAL_ERROR_MESSAGE.getErrorCode()), RESPONSE_BODY_NOT_EXPECTED);

  }

  void getMockingPathNotFound() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any()))
        .thenReturn(java.util.Optional.ofNullable(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    headers.set("accept", "");
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);

    //test mock response from query=title:empty
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/boosks?query=title:empty&sort=published:desc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 404, "http code is not expected");

  }

  void getMocksCategoriesCatIdBooksBookId() throws IOException {
    //
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any()))
        .thenReturn(java.util.Optional.ofNullable(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

    //test mock response from query but some header was missing
    ResponseEntity<String> response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/1/books/x", HttpMethod.GET, request,
            String.class);
    Assert.isTrue(response.getStatusCodeValue() == 201, "http code is not expected");

    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/any/books/1", HttpMethod.GET,
            request, String.class);
    Assert.isTrue(response.getStatusCodeValue() == 403, "http code is not expected");

    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/0/books/11", HttpMethod.GET,
            request, String.class);
    Assert.isTrue(response.getStatusCodeValue() == 200, "http code is not expected");

  }

  void getMocksMockingIdPathBooksQuery() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any()))
        .thenReturn(java.util.Optional.ofNullable(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    headers.set("client-id", "");
    headers.set("accept", "application/json");
    HttpEntity<HttpHeaders> headersHttpEntity2 = new HttpEntity<>(headers);

    //test mock response from query but some header was missing
    ResponseEntity<String> responseEntity3 = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=123", HttpMethod.GET,
            headersHttpEntity2, String.class);
    isTrue(responseEntity3.getStatusCodeValue() == 422, "http code is not expected");
    if (responseEntity3.getBody() != null) {
      isTrue(responseEntity3.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any()))
        .thenReturn(java.util.Optional.ofNullable(mockEntities));
    headers = new HttpHeaders();
    headers.set("client-id", "asd");
    headers.set("accept", "application/json");
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);

    //test mock response from query=
    ResponseEntity<String> responseEntity2 = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity2.getStatusCodeValue() == 422, "http code is not expected");
    if (responseEntity2.getBody() != null) {
      isTrue(responseEntity2.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from query was not initiate
    responseEntity2 = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?sort=published:sdesc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity2.getStatusCodeValue() == 422, "http code is not expected");
    if (responseEntity2.getBody() != null) {
      isTrue(responseEntity2.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from query=title:empty
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:empty&sort=published:desc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from sort=published:asc
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:aone&sort=published:asc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from sort=published:asc because its defined the first then query=title:one
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:one&sort=published:asc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response default
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=one&sort=asc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mocking response from header with client-id=default
    headers.set("client-id", "default");
    headersHttpEntity = new HttpEntity<>(headers);

    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=one&sort=asc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 400, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mocking response from header with client-id=one
    headers.set("client-id", "one");
    headersHttpEntity = new HttpEntity<>(headers);

    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books&query=one&sort=asc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mocking response from header with client-id=one
    headers.set("client-id", "error_400");
    headersHttpEntity = new HttpEntity<>(headers);

    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books&query=one&sort=asc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 400, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }
  }

  void getMocksMockingIdPathStoreBooks() throws IOException {
    String payload = "{\n" +
        "  \"title\": \"x\"\n" +
        "}";
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(java.util.Optional.of(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    headers.set("accept", "application/json");
    HttpEntity<String> headersHttpEntity = new HttpEntity<>(payload, headers);
    //test mock response default
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.POST, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock from payload title:noReff
    payload = "{\n" +
        "  \"title\": \"noRef\"\n" +
        "}";
    headersHttpEntity = new HttpEntity<>(payload, headers);
    //test mock response default
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.POST, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    payload = "{\n" +
        "  \"title\": \"withRef\"\n" +
        "}";
    headersHttpEntity = new HttpEntity<>(payload, headers);
    //test mock response default
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.POST, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }
  }


  void getMocksMockingIdBookDetail() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(java.util.Optional.of(mockEntities));

    HttpHeaders headers = new HttpHeaders();
    headers.set("accept", "application/json");
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
    //test mock response from BOOK_ID=10
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/10", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from BOOK_ID=99
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/99", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from BOOK_ID=99
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/99", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mocking response from header with client-id=one
    headers.set("client-id", "empty");
    headersHttpEntity = new HttpEntity<>(headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/919", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }
    isTrue(responseEntity.getHeaders().get(Helper.DEFAULT_HEADER_NAME).get(0)
        .equals(Helper.DEFAULT_HEADER_VALUE), "header not expected");

    //test mocking response from header with client-id=one
    headers.set("client-id", "one");
    headersHttpEntity = new HttpEntity<>(headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/919", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 403, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }
    isTrue(responseEntity.getHeaders().get(Helper.DEFAULT_HEADER_NAME).get(0)
        .equals(Helper.DEFAULT_HEADER_VALUE), "header not expected");

    //test mocking response default
    headers.set("client-id", "x");
    headersHttpEntity = new HttpEntity<>(headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/919", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 401, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }
    isTrue(responseEntity.getHeaders().get(Helper.DEFAULT_HEADER_NAME).get(0)
        .equals(Helper.DEFAULT_HEADER_VALUE), "header not expected");
  }


  void getMocksMockingDetailUpdate() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any(String.class)))
        .thenReturn(java.util.Optional.ofNullable(mockEntities));

    String payload3 = "{}";
    HttpHeaders headers = new HttpHeaders();
    headers.set("accept", "application/json");
    HttpEntity<String> headersHttpEntity2 = new HttpEntity<>(payload3, headers);
    //test mock response from BOOK_ID=10
    ResponseEntity<String> responseEntity2 = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update", HttpMethod.PUT,
            headersHttpEntity2, String.class);
    isTrue(responseEntity2.getStatusCodeValue() == 422, "http code is not expected");
    if (responseEntity2.getBody() != null) {
      isTrue(responseEntity2.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    String payload1 = "{\n" +
        "\t\"title\":null\n" +
        "}";

    HttpEntity<String> headersHttpEntity = new HttpEntity<>(payload1, headers);
    //test mock response from BOOK_ID=10
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 422, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }
    String payload = "{}";
    //test mock response from query query=title:empty
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update?query=title:empty",
            HttpMethod.PUT, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 422, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from query query=title:empty
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update?sort=published:asc",
            HttpMethod.PUT, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from query query=title:one
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update?query=title:one",
            HttpMethod.PUT, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from path BOOK_ID=1
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/1/update?query=title:onxe",
            HttpMethod.PUT, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from path BOOK_ID=1
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/5/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 403, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from path BOOK_ID=1
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/5/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 403, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from payload tittle:noreff
    payload = "{\n" +
        "  \"title\": \"noRef\"\n" +
        "}";
    headersHttpEntity = new HttpEntity<>(payload, headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/51/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }

    //test mock response from payload tittle:withRef
    payload = "{\n" +
        "  \"title\": \"withRef\"\n" +
        "}";
    headersHttpEntity = new HttpEntity<>(payload, headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/51/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 201, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
          "response is not expected");
    }
  }
}
