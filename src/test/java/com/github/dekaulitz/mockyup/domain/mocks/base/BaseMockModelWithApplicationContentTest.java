package com.github.dekaulitz.mockyup.domain.mocks.base;

import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoPathItemOpenApiVmodel;
import com.github.dekaulitz.mockyup.helperTest.Helper;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.InvalidMockException;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

class BaseMockModelWithApplicationContentTest {

  private final String APPLICATION_TYPE_ACCEPT = "accept";
  private OpenAPI openAPI;
  private BaseMockModel baseMockModel;
  private HttpServletRequest httpServletRequest;

  @BeforeEach
  void setUp() throws IOException {
    this.baseMockModel = new BaseMockModel();
    this.httpServletRequest = Mockito.mock(HttpServletRequest.class);
    ClassLoader classLoader = getClass().getClassLoader();
    //using static swagger json for testing
    File file = new File(classLoader.getResource("public/mocking_test.json").getFile());
    String defaultTemplate = new String(Files.readAllBytes(file.toPath()));
    SwaggerParseResult result = new OpenAPIParser().readContents(defaultTemplate, null, null);
    this.openAPI = result.getOpenAPI();
    Paths newPath = new Paths();
    this.openAPI.getPaths().forEach((s, pathItem) -> {
      newPath.put(s.replace("{", "*{"), pathItem);
    });
    this.openAPI.setPaths(newPath);
  }

  @Test
  void getMockResponseWhenPathIsExactlySameWithOpenApiPath()
      throws IOException, InvalidMockException, NotFoundException {
    String path = "/books";
    String body = "{}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    String[] openAPIPaths = path.split("/");
    PathItem item = this.openAPI.getPaths().get(path);
    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("get");
    Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("");
    Mockito.when(this.httpServletRequest.getHeader("client-id")).thenReturn("any");
    //do request with application/xml
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(item, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 422,
        "mockhelper was not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getResponse() != null,
        "mockhelper was not expected");
    //its static from mocking_test.json
    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper was not expected");

    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);
    mockHelper = this.baseMockModel
        .renderingMockResponse(item, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper was not expected");
  }

  @Test
  void getQueryMockResponse() throws IOException, InvalidMockException, NotFoundException {
    String path = "/books?query=title:empty";
    String body = "{}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    String[] openAPIPaths = path.split("/");

    PathItem pathItem = generatePathMock(openAPIPaths, paths, this.openAPI);

    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("get");
    Mockito.when(this.httpServletRequest.getHeader("client-id")).thenReturn("any");
    Mockito.when(this.httpServletRequest.getQueryString())
        .thenReturn("path=/books?query=title:empty&sort=published:desc");
    //do request with application/xml
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    //test when using example query=title:empty
    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");

    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper was not expected");

    //test when using example sort=published:asc
    Mockito.when(this.httpServletRequest.getQueryString())
        .thenReturn("path=/books&sort=published:asc");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    result = Helper.parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper was not expected");

    //test when using example with query=title:one
    Mockito.when(this.httpServletRequest.getQueryString())
        .thenReturn("path=/books&query=title:one");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    result = Helper.parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper was not expected");

    //test when using example default response
    Mockito.when(this.httpServletRequest.getQueryString())
        .thenReturn("path=/books?query=title:1one");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    result = Helper.parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper was not expected");

    //try to request with application/json
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);

    Mockito.when(this.httpServletRequest.getQueryString())
        .thenReturn("path=/books?query=title:empty&sort=published:desc");
    //test when using example query=title:empty
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");

    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper was not expected");

    //test when using example sort=published:asc
    Mockito.when(this.httpServletRequest.getQueryString())
        .thenReturn("path=/books&sort=published:asc");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper was not expected");

    //test when using example with query=title:one
    Mockito.when(this.httpServletRequest.getQueryString())
        .thenReturn("path=/books&query=title:one");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper was not expected");

    //test when using example default response
    Mockito.when(this.httpServletRequest.getQueryString())
        .thenReturn("path=/books?query=title:1one");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper was not expected");

  }

  @Test
  void getHeaderMockResponse() throws IOException, InvalidMockException, NotFoundException {
    String path = "/books";
    String body = "{}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    String[] openAPIPaths = path.split("/");

    PathItem pathItem = generatePathMock(openAPIPaths, paths, this.openAPI);

    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("get");
    Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("");

    //test when using example header client-id=default
    Mockito.when(this.httpServletRequest.getHeader("client-id")).thenReturn("default");
    //do request with application/json
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);

    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 400,
        "mock helper is not expected");

    //test when using example header client-id=one
    Mockito.when(this.httpServletRequest.getHeader("client-id")).thenReturn("one");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 200,
        "mock helper is not expected");

    //test when using example header client-id=error_400
    Mockito.when(this.httpServletRequest.getHeader("client-id")).thenReturn("error_400");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 400,
        "mock helper is not expected");

    //do request wih application/xml
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);

    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper was not expected");

    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 400,
        "mock helper is not expected");

    //test when using example header client-id=one
    Mockito.when(this.httpServletRequest.getHeader("client-id")).thenReturn("one");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    result = Helper.parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper was not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 200,
        "mock helper is not expected");

    //test when using example header client-id=error_400
    Mockito.when(this.httpServletRequest.getHeader("client-id")).thenReturn("error_400");
    mockHelper = this.baseMockModel
        .renderingMockResponse(pathItem, this.httpServletRequest, body, openAPIPaths, paths,
            openAPI.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    result = Helper.parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper was not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 400,
        "mock helper is not expected");


  }

  @Test
  void getPathMockResponse() throws Exception {
    String path = "/books/10";
    String body = "{}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    DtoPathItemOpenApiVmodel dtoPathItemOpenApiVmodel = new DtoPathItemOpenApiVmodel();
    dtoPathItemOpenApiVmodel.setPaths(paths);
    dtoPathItemOpenApiVmodel.setComponents(this.openAPI.getComponents());
    generatePathMockPath(dtoPathItemOpenApiVmodel, this.openAPI);
    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("get");
    Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("");
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);
    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
            this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
            paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 200,
        "mock helper is not expected");

    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    mockHelper = this.baseMockModel.renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
        this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
        paths, dtoPathItemOpenApiVmodel.getComponents());

    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 200,
        "mock helper is not expected");


  }

  @Test
  void getAnotherPathMockResponse() throws Exception {
    String path = "/books/99";
    String body = "{}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    DtoPathItemOpenApiVmodel dtoPathItemOpenApiVmodel = new DtoPathItemOpenApiVmodel();
    dtoPathItemOpenApiVmodel.setPaths(paths);
    dtoPathItemOpenApiVmodel.setComponents(this.openAPI.getComponents());
    generatePathMockPath(dtoPathItemOpenApiVmodel, this.openAPI);
    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("get");
    Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("");
    //try to request with application/json
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);

    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
            this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
            paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 500,
        "mock helper is not expected");

    //do request with application/xml
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    mockHelper = this.baseMockModel.renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
        this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
        paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 500,
        "mock helper is not expected");


  }

  @Test
  void getAnotherPathMockResponseWithDefault() throws Exception {
    String path = "/books/101";
    String body = "{}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    DtoPathItemOpenApiVmodel dtoPathItemOpenApiVmodel = new DtoPathItemOpenApiVmodel();
    dtoPathItemOpenApiVmodel.setPaths(paths);
    dtoPathItemOpenApiVmodel.setComponents(this.openAPI.getComponents());
    generatePathMockPath(dtoPathItemOpenApiVmodel, this.openAPI);
    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("get");
    Mockito.when(this.httpServletRequest.getHeader("client-id")).thenReturn("any");
    Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("");
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);
    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
            this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
            paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 401,
        "mock helper is not expected");

    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    mockHelper = this.baseMockModel.renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
        this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
        paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 401,
        "mock helper is not expected");
  }

  @Test
  void getRequestBoydMockResponse() throws Exception {
    String path = "/books/101/update";
    String body = "{\n" +
        "  \"title\": \"noRef\"\n" +
        "}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    DtoPathItemOpenApiVmodel dtoPathItemOpenApiVmodel = new DtoPathItemOpenApiVmodel();
    dtoPathItemOpenApiVmodel.setPaths(paths);
    dtoPathItemOpenApiVmodel.setComponents(this.openAPI.getComponents());
    generatePathMockPath(dtoPathItemOpenApiVmodel, this.openAPI);
    //default method from servlet request using UPPERCASE
    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("PUT");
    Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("");
    //do request with application/json
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);
    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
            this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
            paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 200,
        "mock helper is not expected");

    body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<title>noRef</title>\n" +
        "</books>";

    //do request with application/json
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    mockHelper = this.baseMockModel.renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
        this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
        paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_RESPONSE_MOCK), "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 200,
        "mock helper is not expected");

  }

  @Test
  void getAnotherRequestBoydMockResponse() throws Exception {
    String path = "/books/101/update";
    String body = "{\n" +
        "  \"title\": \"withRef\"\n" +
        "}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    DtoPathItemOpenApiVmodel dtoPathItemOpenApiVmodel = new DtoPathItemOpenApiVmodel();
    dtoPathItemOpenApiVmodel.setPaths(paths);
    dtoPathItemOpenApiVmodel.setComponents(this.openAPI.getComponents());
    generatePathMockPath(dtoPathItemOpenApiVmodel, this.openAPI);
    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("PUT");
    Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("");
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);
    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
            this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
            paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 201,
        "mock helper is not expected");

    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<id>0</id>\n" +
        "\t<title>withRef</title>\n" +
        "</books>";

    mockHelper = this.baseMockModel.renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
        this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
        paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 201,
        "mock helper is not expected");

  }

  @Test
  void getAnotherRequestBoydMockResponseWhenNoDefaultResponseWithPathPriority() throws Exception {
    String path = "/books/1/update";
    String body = "{\n" +
        "  \"title\": \"withR1ef\"\n" +
        "}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    DtoPathItemOpenApiVmodel dtoPathItemOpenApiVmodel = new DtoPathItemOpenApiVmodel();
    dtoPathItemOpenApiVmodel.setPaths(paths);
    dtoPathItemOpenApiVmodel.setComponents(this.openAPI.getComponents());
    generatePathMockPath(dtoPathItemOpenApiVmodel, this.openAPI);
    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("PUT");
    Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("");
    //do request application/json
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);
    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
            this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
            paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 500,
        "mock helper is not expected");

    //do request application/xml
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    mockHelper = this.baseMockModel.renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
        this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
        paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 500,
        "mock helper is not expected");

  }

  @Test
  void getAnotherRequestBoydMockResponseWhenNoDefaultResponseWithQueryPriority() throws Exception {
    String path = "/books/12/update";
    String body = "{\n" +
        "  \"title\": \"withR1ef\"\n" +
        "}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    DtoPathItemOpenApiVmodel dtoPathItemOpenApiVmodel = new DtoPathItemOpenApiVmodel();
    dtoPathItemOpenApiVmodel.setPaths(paths);
    dtoPathItemOpenApiVmodel.setComponents(this.openAPI.getComponents());
    generatePathMockPath(dtoPathItemOpenApiVmodel, this.openAPI);
    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("PUT");
    Mockito.when(this.httpServletRequest.getQueryString())
        .thenReturn("path=/books/12/update?query=title:empty");
    //do request application/json
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);
    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
            this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
            paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    Assert.isTrue(
        mockHelper.getResponseProperty().getResponse().equals(Helper.DEFAULT_COMPONENT_EXAMPLE),
        "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 422,
        "mock helper is not expected");

    //do request application/xml
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<id>0</id>\n" +
        "\t<title>withaRef</title>\n" +
        "</books>";

    mockHelper = this.baseMockModel.renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
        this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
        paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.notNull(mockHelper, "mockhelper is not expected");
    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_RESPONSE_MOCK), "mockhelper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHeaders().get(Helper.DEFAULT_HEADER_NAME)
        .equals(Helper.DEFAULT_HEADER_VALUE), "mock helper is not expected");
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 422,
        "mock helper is not expected");
  }

  @Test
  void getAnotherRequestBoydMockResponseWhenNoDefaultResponse() throws Exception {
    String path = "/books/101/update";
    String body = "{\n" +
        "  \"title\": \"withR1ef\"\n" +
        "}";
    String[] originalPathUri = path.split("\\?");
    String[] paths = originalPathUri[0].split("/");
    DtoPathItemOpenApiVmodel dtoPathItemOpenApiVmodel = new DtoPathItemOpenApiVmodel();
    dtoPathItemOpenApiVmodel.setPaths(paths);
    dtoPathItemOpenApiVmodel.setComponents(this.openAPI.getComponents());
    generatePathMockPath(dtoPathItemOpenApiVmodel, this.openAPI);
    Mockito.when(this.httpServletRequest.getMethod()).thenReturn("PUT");
    Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("");
    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_JSON_VALUE);
    MockHelper mockHelper = this.baseMockModel
        .renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
            this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
            paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 401,
        "mock helper is not expected");

    Mockito.when(this.httpServletRequest.getHeader(APPLICATION_TYPE_ACCEPT))
        .thenReturn(MediaType.APPLICATION_XML_VALUE);
    body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<books>\n" +
        "\t<id>0</id>\n" +
        "\t<title>withaRef</title>\n" +
        "</books>";
    mockHelper = this.baseMockModel.renderingMockResponse(dtoPathItemOpenApiVmodel.getPathItem(),
        this.httpServletRequest, body, dtoPathItemOpenApiVmodel.getOpenAPIPaths(),
        paths, dtoPathItemOpenApiVmodel.getComponents());
    Assert.isTrue(mockHelper.getResponseProperty().getHttpCode() == 401,
        "mock helper is not expected");
    String result = Helper
        .parseXmlComponentTest((String) mockHelper.getResponseProperty().getResponse());
    Assert.isTrue(result.equals(Helper.DEFAULT_COMPONENT_EXAMPLE), "mock helper is not expected");
  }

  PathItem generatePathMock(String[] openAPIPaths, String[] paths, OpenAPI openApi) {
    //path item with path parameter we should check every endpoint that related with data
    PathItem pathItem = null;
    for (Map.Entry<String, PathItem> entry : openApi.getPaths().entrySet()) {
      String s = entry.getKey();
      pathItem = entry.getValue();
      openAPIPaths = s.split("/");
      String[] regexPath = new String[paths.length];
      // every targeting mocks uri should has same length
      if (openAPIPaths.length == paths.length) {
        // converting request path to original for searching data
        for (int i = 0; i < openAPIPaths.length; i++) {
          //checking path parameter
          if (!openAPIPaths[i].equals(paths[i])) {
            if (openAPIPaths[i].contains("*")) {
              regexPath[i] = openAPIPaths[i];
            }
          } else {
            //this is for checking wildcard with path parameter
            if (!paths[i].contains("*") && openAPIPaths[i].equals(paths[i])) {
              regexPath[i] = openAPIPaths[i];
            }
          }
        }
        //check if current openapi path equal with regexpath (path)
        if (s.equals(String.join("/", regexPath))) {
          return pathItem;
        }
      }
    }
    return pathItem;
  }

  private void generatePathMockPath(DtoPathItemOpenApiVmodel dtoPathItemOpenApiVmodel,
      OpenAPI openApi) throws Exception {
    boolean mapped = false;
    //path item with path parameter we should check every endpoint that related with data
    for (Map.Entry<String, PathItem> entry : openApi.getPaths().entrySet()) {
      String s = entry.getKey();
      dtoPathItemOpenApiVmodel.setPathItem(entry.getValue());
      dtoPathItemOpenApiVmodel.setOpenAPIPaths(s.split("/"));
      String[] regexPath = new String[dtoPathItemOpenApiVmodel.getPaths().length];
      // every targeting mocks uri should has same length
      if (dtoPathItemOpenApiVmodel.getOpenAPIPaths().length == dtoPathItemOpenApiVmodel
          .getPaths().length) {
        // converting request path to original for searching data
        for (int i = 0; i < dtoPathItemOpenApiVmodel.getOpenAPIPaths().length; i++) {
          //checking path parameter
          if (!dtoPathItemOpenApiVmodel.getOpenAPIPaths()[i]
              .equals(dtoPathItemOpenApiVmodel.getPaths()[i])) {
            if (dtoPathItemOpenApiVmodel.getOpenAPIPaths()[i].contains("*")) {
              regexPath[i] = dtoPathItemOpenApiVmodel.getOpenAPIPaths()[i];
            }
          } else {
            //this is for checking wildcard with path parameter
            if (!dtoPathItemOpenApiVmodel.getPaths()[i].contains("*") && dtoPathItemOpenApiVmodel
                .getOpenAPIPaths()[i].equals(dtoPathItemOpenApiVmodel.getPaths()[i])) {
              regexPath[i] = dtoPathItemOpenApiVmodel.getOpenAPIPaths()[i];
            }
          }
        }
        //check if current openapi path equal with regexpath (path)
        if (s.equals(String.join("/", regexPath))) {
          mapped = true;
          break;
        }
      }
    }
    if (!mapped) {
      throw new Exception("invalid type");
    }
  }
}
