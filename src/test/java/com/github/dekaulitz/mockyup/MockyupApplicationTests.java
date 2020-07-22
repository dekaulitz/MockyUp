package com.github.dekaulitz.mockyup;

import com.github.dekaulitz.mockyup.domain.auth.vmodels.DoAuthVmodel;
import com.github.dekaulitz.mockyup.domain.auth.vmodels.DtoAuthProfileVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.helperTest.MockTestHelper;
import com.github.dekaulitz.mockyup.infrastructure.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.infrastructure.db.repositories.MockRepository;
import com.github.dekaulitz.mockyup.infrastructure.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.infrastructure.errors.vmodels.ErrorVmodel;
import com.github.dekaulitz.mockyup.infrastructure.errors.vmodels.ResponseVmodel;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MockyupApplicationTests {
    private String baseUrl;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;

    @MockBean
    private MockRepository mockRepository;

    private MockTestHelper mockTestHelper;

    @BeforeEach
    void setUp() throws IOException {
        baseUrl = "http://localhost:" + port;
        mockTestHelper = new MockTestHelper(this.restTemplate, baseUrl);
    }

    @Test
    @Order(1)
    void doLoginWithInValidAuth() {
        ErrorVmodel errorVmodel = ResponseCode.INVALID_USERNAME_OR_PASSWORD;
        DoAuthVmodel authVmodel = new DoAuthVmodel();
        authVmodel.setUsername("root");
        authVmodel.setPassword("fahmi");
        HttpEntity<DoAuthVmodel> request = new HttpEntity<>(authVmodel);
        ResponseEntity<ResponseVmodel> responseEntity = this.restTemplate
                .postForEntity(baseUrl + "/mocks/login", request, ResponseVmodel.class);
        Assert.isTrue(responseEntity.getStatusCode().value() == errorVmodel.getHttpCode().value(), "http code unexpected");
        if (responseEntity.getBody() != null) {
            Assert.isTrue(responseEntity.getBody().getResponseCode().equals(errorVmodel.getErrorCode()), "response code is not expected");
            Assert.isTrue(responseEntity.getBody().getResponseMessage().equals(errorVmodel.getErrorMessage()), "error message was not expected");
        }
    }

    @Test
    @Order(2)
        //test refresh token with correct refresh token
    void doRefreshToken() {
        String accessToken = this.mockTestHelper.doRequestLogin();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
        ResponseEntity<DtoAuthProfileVmodel> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/auth/refresh", HttpMethod.GET, headersHttpEntity, DtoAuthProfileVmodel.class);
        Assert.isTrue(responseEntity.getStatusCode().value() == 200, "http code unexpected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(!responseEntity.getBody().getToken().isEmpty(), "token not exist");
    }

    @Test
        //test refresh token with expired token
    void doRefreshTokenWithExpiredToken() throws UnsupportedEncodingException {
        ErrorVmodel responseVmodel = ResponseCode.TOKEN_EXPIRED;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + this.mockTestHelper.generateTokenExpired());
        HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
        ResponseEntity<ResponseVmodel> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/auth/refresh", HttpMethod.GET, headersHttpEntity, ResponseVmodel.class);
        Assert.isTrue(responseEntity.getStatusCode().value() == responseVmodel.getHttpCode().value(), "http code unexpected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().getResponseCode().equals(responseVmodel.getErrorCode()), "token not equal");
    }


    @Test
    @Order(3)
        //test login with valid username and password
    void doLoginWithValidAuth() {
        DoAuthVmodel authVmodel = new DoAuthVmodel();
        authVmodel.setUsername("root");
        authVmodel.setPassword("root");
        HttpEntity<DoAuthVmodel> request = new HttpEntity<>(authVmodel);
        ResponseEntity<DtoAuthProfileVmodel> responseEntity = this.restTemplate
                .postForEntity(baseUrl + "/mocks/login", request, DtoAuthProfileVmodel.class);
        Assert.isTrue(responseEntity.getStatusCode().value() == 200, "http code unexpected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(!responseEntity.getBody().getToken().isEmpty(), "token not exist");
    }

    @Test
    @Order(4)
        //test getting all mock list
    void getallMocksList() {
        String accessToken = this.mockTestHelper.doRequestLogin();
        Assert.isTrue(!accessToken.isEmpty(), "access token is empty");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
        ResponseEntity<MockEntitiesPage> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/page", HttpMethod.GET, headersHttpEntity, MockEntitiesPage.class);
        Assert.isTrue(responseEntity.getStatusCode().value() == 200, "http code unexpected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().getRowCount() == 0, "row count of mocks is not valid");
    }

    @Test
    void testAddNewMockValidation() {
        String accessToken = this.mockTestHelper.doRequestLogin();
        ErrorVmodel errorVmodel = ResponseCode.VALIDATION_FAIL;
        MockVmodel mockVmodel = new MockVmodel();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<MockVmodel> headersHttpEntity = new HttpEntity<>(mockVmodel, headers);
        ResponseEntity<ResponseVmodel> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/store", HttpMethod.POST, headersHttpEntity, ResponseVmodel.class);
        Assert.isTrue(responseEntity.getStatusCode().value() == errorVmodel.getHttpCode().value(), "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().getResponseCode().equals(errorVmodel.getErrorCode()), "error code is not equal");
    }

    @Test
    void addNewMock() throws IOException {
        MockEntities mockEntities = this.mockTestHelper.generateMockEntities();
        Mockito.when(this.mockRepository.save(Mockito.any(MockEntities.class))).thenReturn(mockEntities);
        String accessToken = this.mockTestHelper.doRequestLogin();
        MockVmodel mockVmodel = new MockVmodel();
        mockVmodel.setTitle(mockTestHelper.MOCK_TITLE);
        mockVmodel.setSpec(JsonMapper.mapper().readTree(this.mockTestHelper.getDefaultJsonContract()));
        mockVmodel.setDescription(mockTestHelper.MOCK_DESC);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<MockVmodel> headersHttpEntity = new HttpEntity<>(mockVmodel, headers);
        ResponseEntity<MockVmodel> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/store", HttpMethod.POST, headersHttpEntity, MockVmodel.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null) {
            Assert.isTrue(responseEntity.getBody().getTitle().equals(mockTestHelper.MOCK_TITLE), "title is not expected");
            Assert.isTrue(responseEntity.getBody().getDescription().equals(mockTestHelper.MOCK_DESC), "description is not expected");
            Assert.isTrue(responseEntity.getBody().getId().equals(mockTestHelper.DEFAULT_MOCK_ID), "mock id is not expected");
        }
    }

    @Test
    void getMockMockingWithDefault() throws IOException {
        MockEntities mockEntities = this.mockTestHelper.generateMockEntities();
        Mockito.when(this.mockRepository.findById(Mockito.any(String.class))).thenReturn(java.util.Optional.ofNullable(mockEntities));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");
    }

    @Test
        //contract path /books
    void getMockingForBooksPath() throws IOException {
        MockEntities mockEntities = this.mockTestHelper.generateMockEntities();
        Mockito.when(this.mockRepository.findById(Mockito.any(String.class))).thenReturn(java.util.Optional.ofNullable(mockEntities));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);

        //test mock response from query=title:empty
        ResponseEntity<String> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:empty&sort=published:desc", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

        //test mock response from sort=published:asc
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:aone&sort=published:asc", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mock response from sort=published:asc because its defined the first then query=title:one
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:one&sort=published:asc", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mock response default
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=one&sort=asc", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mocking response from header with client-id=default
        headers.set("client-id", "default");
        headersHttpEntity = new HttpEntity<>(headers);

        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=one&sort=asc", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 400, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

        //test mocking response from header with client-id=one
        headers.set("client-id", "one");
        headersHttpEntity = new HttpEntity<>(headers);

        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books&query=one&sort=asc", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mocking response from header with client-id=one
        headers.set("client-id", "error_400");
        headersHttpEntity = new HttpEntity<>(headers);

        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books&query=one&sort=asc", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 400, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");
    }

    @Test
        //contract path /books
    void getMockingForStoreBooks() throws IOException {
        String payload = "{\n" +
                "  \"title\": \"x\"\n" +
                "}";
        MockEntities mockEntities = this.mockTestHelper.generateMockEntities();
        Mockito.when(this.mockRepository.findById(Mockito.any(String.class))).thenReturn(java.util.Optional.ofNullable(mockEntities));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> headersHttpEntity = new HttpEntity<>(payload, headers);
        //test mock response default
        ResponseEntity<String> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.POST, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

        //test mock from payload title:noReff
        payload = "{\n" +
                "  \"title\": \"noRef\"\n" +
                "}";
        headersHttpEntity = new HttpEntity<>(payload, headers);
        //test mock response default
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.POST, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

        payload = "{\n" +
                "  \"title\": \"withRef\"\n" +
                "}";
        headersHttpEntity = new HttpEntity<>(payload, headers);
        //test mock response default
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.POST, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");
    }

    @Test
        //contract path /books/{BOOK_ID}/update
    void getMockingForBookDetail() throws IOException {
        MockEntities mockEntities = this.mockTestHelper.generateMockEntities();
        Mockito.when(this.mockRepository.findById(Mockito.any(String.class))).thenReturn(java.util.Optional.ofNullable(mockEntities));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
        //test mock response from BOOK_ID=10
        ResponseEntity<String> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/10", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

        //test mock response from BOOK_ID=99
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/99", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mock response from BOOK_ID=99
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/99", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mocking response from header with client-id=one
        headers.set("client-id", "empty");
        headersHttpEntity = new HttpEntity<>(headers);
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/919", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");
        Assert.isTrue(responseEntity.getHeaders().get(mockTestHelper.DEFAULT_HEADER_NAME).get(0)
                .equals(mockTestHelper.DEFAULT_HEADER_VALUE), "header not expected");

        //test mocking response from header with client-id=one
        headers.set("client-id", "one");
        headersHttpEntity = new HttpEntity<>(headers);
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/919", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 403, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");
        Assert.isTrue(responseEntity.getHeaders().get(mockTestHelper.DEFAULT_HEADER_NAME).get(0)
                .equals(mockTestHelper.DEFAULT_HEADER_VALUE), "header not expected");

        //test mocking response default
        headers.set("client-id", "x");
        headersHttpEntity = new HttpEntity<>(headers);
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/919", HttpMethod.GET, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 401, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");
        Assert.isTrue(responseEntity.getHeaders().get(mockTestHelper.DEFAULT_HEADER_NAME).get(0)
                .equals(mockTestHelper.DEFAULT_HEADER_VALUE), "header not expected");
    }

    @Test
        //contract /books/{BOOK_ID}/update
    void getMockingForBooksDetailUpdate() throws IOException {
        MockEntities mockEntities = this.mockTestHelper.generateMockEntities();
        Mockito.when(this.mockRepository.findById(Mockito.any(String.class))).thenReturn(java.util.Optional.ofNullable(mockEntities));

        String payload = "{}";
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> headersHttpEntity = new HttpEntity<>(payload, headers);
        //test mock response from BOOK_ID=10
        ResponseEntity<String> responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update", HttpMethod.PUT, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 401, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mock response from query query=title:empty
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update?query=title:empty", HttpMethod.PUT, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 422, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

        //test mock response from query query=title:empty
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update?sort=published:asc", HttpMethod.PUT, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mock response from query query=title:one
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update?query=title:one", HttpMethod.PUT, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

        //test mock response from path BOOK_ID=1
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/1/update?query=title:onxe", HttpMethod.PUT, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

        //test mock response from path BOOK_ID=1
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/5/update", HttpMethod.PUT, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 403, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mock response from path BOOK_ID=1
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/5/update", HttpMethod.PUT, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 403, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");

        //test mock response from payload tittle:noreff
        payload = "{\n" +
                "  \"title\": \"noRef\"\n" +
                "}";
        headersHttpEntity = new HttpEntity<>(payload, headers);
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/51/update", HttpMethod.PUT, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

        //test mock response from payload tittle:withRef
        payload = "{\n" +
                "  \"title\": \"withRef\"\n" +
                "}";
        headersHttpEntity = new HttpEntity<>(payload, headers);
        responseEntity = this.restTemplate
                .exchange(baseUrl + "/mocks/mocking/id?path=/books/51/update", HttpMethod.PUT, headersHttpEntity, String.class);
        Assert.isTrue(responseEntity.getStatusCodeValue() == 201, "http code is not expected");
        if (responseEntity.getBody() != null)
            Assert.isTrue(responseEntity.getBody().equals(mockTestHelper.DEFAULT_RESPONSE_MOCK), "response is not expected");
    }
}
