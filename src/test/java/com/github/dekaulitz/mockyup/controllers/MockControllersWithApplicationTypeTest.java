package com.github.dekaulitz.mockyup.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isTrue;

import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.repositories.MockHistoryRepository;
import com.github.dekaulitz.mockyup.db.repositories.MockRepository;
import com.github.dekaulitz.mockyup.helperTest.Helper;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MockControllersWithApplicationTypeTest extends BaseTest {

  @MockBean
  private MockRepository mockRepository;

  @MockBean
  private MockHistoryRepository mockHistoryRepository;


  @BeforeEach
  void setUp() throws IOException {
    super.setup();
  }

  @Test
  void getMockingPathNotFound() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    //do request with application/json
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);

    //test mock response from query=title:empty
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/boosks?query=title:empty&sort=published:desc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 404, "http code is not expected");

    //do request with application/xml
    headers.set("accept", MediaType.APPLICATION_XML_VALUE);
    headersHttpEntity = new HttpEntity<>(headers);

    //test mock response from query=title:empty
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/boosks?query=title:empty&sort=published:desc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 404, "http code is not expected");
  }

  @Test
  void getMocksCategoriesCatIdBooksBookId() throws IOException {
    //
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

    //test mock response from query but some header was missing
    ResponseEntity<String> response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/1/books/x", HttpMethod.GET, request,
            String.class);
    Assert.isTrue(response.getStatusCodeValue() == 201, "http code is not expected");
    Assert.isTrue(response.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        RESPONSE_BODY_NOT_EXPECTED);

    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/any/books/1", HttpMethod.GET,
            request, String.class);
    Assert.isTrue(response.getStatusCodeValue() == 403, "http code is not expected");
    Assert.isTrue(response.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        RESPONSE_BODY_NOT_EXPECTED);

    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/0/books/11", HttpMethod.GET,
            request, String.class);
    Assert.isTrue(response.getStatusCodeValue() == 200, "http code is not expected");
    Assert.isTrue(response.getBody().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        RESPONSE_BODY_NOT_EXPECTED);

    //do request with application/xml
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_XML_VALUE);
    request = new HttpEntity<>(headers);

    //test mock response from query but some header was missing
    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/1/books/x", HttpMethod.GET, request,
            String.class);
    Assert.isTrue(response.getStatusCodeValue() == 201, "http code is not expected");
    String result = Helper.parseXmlComponentTest(Objects.requireNonNull(response.getBody()));
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), RESPONSE_BODY_NOT_EXPECTED);

    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/any/books/1", HttpMethod.GET,
            request, String.class);
    Assert.isTrue(response.getStatusCodeValue() == 403, "http code is not expected");
    result = Helper.parseXmlComponentTest(Objects.requireNonNull(response.getBody()));
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), RESPONSE_BODY_NOT_EXPECTED);

    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/0/books/11", HttpMethod.GET,
            request, String.class);
    Assert.isTrue(response.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(Objects.requireNonNull(response.getBody()));
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), RESPONSE_BODY_NOT_EXPECTED);

    //do test with application/xhtml
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_XHTML_XML_VALUE);
    request = new HttpEntity<>(headers);
    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/1/books/x", HttpMethod.GET, request,
            String.class);
    Assert.isTrue(response.getStatusCodeValue() == 201, "http code is not expected");

    Assert.isTrue(response.getBody().contains(Helper.DEFAULT_COMPONENT_EXAMPLE),
        RESPONSE_BODY_NOT_EXPECTED);
  }

  @Test
  void getMocksCategoriesCatIdBooksBookIdXml() throws IOException {
    //
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    //do request with application/xml
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_XML_VALUE);
    HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

    //test mock response from query but some header was missing
    ResponseEntity<String> response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/1/books/x", HttpMethod.GET, request,
            String.class);
    Assert.isTrue(response.getStatusCodeValue() == 201, "http code is not expected");
    String result = Helper.parseXmlComponentTest(Objects.requireNonNull(response.getBody()));
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), RESPONSE_BODY_NOT_EXPECTED);

    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/any/books/1", HttpMethod.GET,
            request, String.class);
    Assert.isTrue(response.getStatusCodeValue() == 403, "http code is not expected");
    result = Helper.parseXmlComponentTest(Objects.requireNonNull(response.getBody()));
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), RESPONSE_BODY_NOT_EXPECTED);

    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/0/books/11", HttpMethod.GET,
            request, String.class);
    Assert.isTrue(response.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(Objects.requireNonNull(response.getBody()));
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), RESPONSE_BODY_NOT_EXPECTED);

    //do test with application/xhtml
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_XHTML_XML_VALUE);
    request = new HttpEntity<>(headers);
    response = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/categories/1/books/x", HttpMethod.GET, request,
            String.class);
    Assert.isTrue(response.getStatusCodeValue() == 201, "http code is not expected");

    Assert.isTrue(response.getBody().contains(Helper.DEFAULT_COMPONENT_EXAMPLE),
        RESPONSE_BODY_NOT_EXPECTED);
  }

  @Test
  void getMocksMockingIdPathBooksQuery() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    headers.set("client-id", "");
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_JSON_VALUE);
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
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(mockEntities));
    headers = new HttpHeaders();
    headers.set("client-id", "asd");
    headers.set("accept", "");
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
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_RESPONSE_MOCK),
          "response is not expected");
    }

    //test mock response from sort=published:asc because its defined the first then query=title:one
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:one&sort=published:asc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_RESPONSE_MOCK),
          "response is not expected");
    }

    //test mock response default
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=one&sort=asc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    if (responseEntity.getBody() != null) {
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_RESPONSE_MOCK),
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
      isTrue(responseEntity.getBody().equals(Helper.DEFAULT_RESPONSE_MOCK),
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

  @Test
  void getMocksMockingIdPathBooksQueryXml() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    headers.set("client-id", "");
    //do request with application/xml
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_XML_VALUE);
    HttpEntity<HttpHeaders> headersHttpEntity2 = new HttpEntity<>(headers);
    //test mock response from query but some header was missing
    ResponseEntity<String> responseEntity3 = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=123", HttpMethod.GET,
            headersHttpEntity2, String.class);
    isTrue(responseEntity3.getStatusCodeValue() == 422, "http code is not expected");

    String result = Helper.parseXmlComponentTest(responseEntity3.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.ofNullable(mockEntities));
    headers = new HttpHeaders();
    headers.set("client-id", "asd");
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_XML_VALUE);
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);

    //test mock response from query=
    ResponseEntity<String> responseEntity2 = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity2.getStatusCodeValue() == 422, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity3.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from query was not initiate
    responseEntity2 = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?sort=published:sdesc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity2.getStatusCodeValue() == 422, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity2.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from query=title:empty
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:empty&sort=published:desc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from sort=published:asc
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:aone&sort=published:asc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from sort=published:asc because its defined the first then query=title:one
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=title:one&sort=published:asc",
            HttpMethod.GET, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response default
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=one&sort=asc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mocking response from header with client-id=default
    headers.set("client-id", "default");
    headersHttpEntity = new HttpEntity<>(headers);

    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books?query=one&sort=asc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 400, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mocking response from header with client-id=one
    headers.set("client-id", "one");
    headersHttpEntity = new HttpEntity<>(headers);

    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books&query=one&sort=asc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mocking response from header with client-id=one
    headers.set("client-id", "error_400");
    headersHttpEntity = new HttpEntity<>(headers);

    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books&query=one&sort=asc", HttpMethod.GET,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 400, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");
  }

  @Test
  void getMocksMockingIdPathStoreBooks() throws IOException {
    String payload = "{\n" +
        "  \"title\": \"x\"\n" +
        "}";
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_JSON_VALUE);
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

  @Test
  void getMocksMockingIdPathStoreBooksXml() throws IOException {

    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));
    HttpHeaders headers = new HttpHeaders();
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    //do request with application/xml
    String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<id>0</id>\n" +
        "\t<title>x</title>\n" +
        "</books>";
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_XML_VALUE);
    HttpEntity<String> headersHttpEntity = new HttpEntity<>(payload, headers);
    //test mock response default
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.POST, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    String result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock from payload title:noReff
    payload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<id>0</id>\n" +
        "\t<title>noRef</title>\n" +
        "</books>";
    headersHttpEntity = new HttpEntity<>(payload, headers);
    //test mock response default
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.POST, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock from payload title:noReff
    payload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<id>0</id>\n" +
        "\t<title>withRef</title>\n" +
        "</books>";
    headersHttpEntity = new HttpEntity<>(payload, headers);
    //test mock response default
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books", HttpMethod.POST, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");
  }

  @Test
  void getMocksMockingIdBookDetail() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));
    //do request with application/json
    HttpHeaders headers = new HttpHeaders();
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_JSON_VALUE);
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

  @Test
  void getMocksMockingIdBookDetailXml() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any())).thenReturn(Optional.of(mockEntities));
    //do request with application/json
    HttpHeaders headers = new HttpHeaders();
    //do request with application/xml
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_XML_VALUE);
    HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(headers);
    //test mock response from BOOK_ID=10
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/10", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    String result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from BOOK_ID=99
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/99", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from BOOK_ID=99
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/99", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mocking response from header with client-id=one
    headers.set("client-id", "empty");
    headersHttpEntity = new HttpEntity<>(headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/919", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");
    isTrue(responseEntity.getHeaders().get(Helper.DEFAULT_HEADER_NAME).get(0)
        .equals(Helper.DEFAULT_HEADER_VALUE), "header not expected");

    //test mocking response from header with client-id=one
    headers.set("client-id", "one");
    headersHttpEntity = new HttpEntity<>(headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/919", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 403, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");
    isTrue(responseEntity.getHeaders().get(Helper.DEFAULT_HEADER_NAME).get(0)
        .equals(Helper.DEFAULT_HEADER_VALUE), "header not expected");

    //test mocking response default
    headers.set("client-id", "x");
    headersHttpEntity = new HttpEntity<>(headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/919", HttpMethod.GET, headersHttpEntity,
            String.class);
    isTrue(responseEntity.getStatusCodeValue() == 401, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");
    isTrue(responseEntity.getHeaders().get(Helper.DEFAULT_HEADER_NAME).get(0)
        .equals(Helper.DEFAULT_HEADER_VALUE), "header not expected");
  }

  @Test
  void getMocksMockingDetailUpdate() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any(String.class)))
        .thenReturn(Optional.ofNullable(mockEntities));

    String payload3 = "{}";
    HttpHeaders headers = new HttpHeaders();
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_JSON_VALUE);
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

  @Test
  void getMocksMockingDetailUpdateXml() throws IOException {
    MockEntities mockEntities = Helper.getMockEntities();
    when(this.mockRepository.findById(any(String.class)))
        .thenReturn(Optional.ofNullable(mockEntities));

    String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "</books>";
    HttpHeaders headers = new HttpHeaders();
    headers.set(MEDIA_TYPE_ACCEPT, MediaType.APPLICATION_XML_VALUE);
    HttpEntity<String> headersHttpEntity2 = new HttpEntity<>(payload, headers);
    //test mock response from BOOK_ID=10
    ResponseEntity<String> responseEntity2 = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update", HttpMethod.PUT,
            headersHttpEntity2, String.class);
    isTrue(responseEntity2.getStatusCodeValue() == 422, "http code is not expected");
    String result = Helper.parseXmlComponentTest(responseEntity2.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    payload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<title>null</title>\n" +
        "</books>";

    HttpEntity<String> headersHttpEntity = new HttpEntity<>(payload, headers);
    //test mock response from BOOK_ID=10
    ResponseEntity<String> responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 422, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from query query=title:empty
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update?query=title:empty",
            HttpMethod.PUT, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 422, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_RESPONSE_MOCK), "response is not expected");

    //test mock response from query query=title:empty
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update?sort=published:asc",
            HttpMethod.PUT, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from query query=title:one
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/x/update?query=title:one",
            HttpMethod.PUT, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from path BOOK_ID=1
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/1/update?query=title:onxe",
            HttpMethod.PUT, headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 500, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from path BOOK_ID=1
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/5/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 403, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from path BOOK_ID=1
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/5/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 403, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");

    //test mock response from payload tittle:noreff
    payload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<title>noRef</title>\n" +
        "</books>";
    headersHttpEntity = new HttpEntity<>(payload, headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/51/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 200, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_RESPONSE_MOCK), "response is not expected");

    //test mock response from payload tittle:withRef
    payload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<title>withRef</title>\n" +
        "</books>";
    headersHttpEntity = new HttpEntity<>(payload, headers);
    responseEntity = this.restTemplate
        .exchange(baseUrl + "/mocks/mocking/id?path=/books/51/update", HttpMethod.PUT,
            headersHttpEntity, String.class);
    isTrue(responseEntity.getStatusCodeValue() == 201, "http code is not expected");
    result = Helper.parseXmlComponentTest(responseEntity.getBody());
    isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "response is not expected");
  }
}
