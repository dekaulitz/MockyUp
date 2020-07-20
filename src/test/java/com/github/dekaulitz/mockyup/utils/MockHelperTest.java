package com.github.dekaulitz.mockyup.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockResponseVmodel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.InvalidMockException;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.isNull;
import static org.springframework.util.Assert.isTrue;

class MockHelperTest {
    private final String DEFAULT_COMPONENT_VALUE = "ok";
    private final String DEFAULT_COMPONENT_NAME = "componentResponse";
    private final String DEFAULT_COMPONENT_NAME_2 = "componentResponse2";
    private final String DEFAULT_COMPONENT_NAME_2_VALUE = "not ok";
    private final String DEFAULT_RESPONSE_HEADER_1 = "x-header-id";
    private final String DEFAULT_RESPONSE_HEADER_VALUE_1 = "1";
    private final String DEFAULT_RESPONSE_HEADER_2 = "x-meta-id";
    private final String DEFAULT_RESPONSE_HEADER_VALUE_2 = "2";
    private Map<String, Object> dtoResponseHeader;
    private HttpServletRequest httpServletRequest;
    private OpenAPI openAPI;

    @BeforeEach
    void setUp() {
        this.httpServletRequest = Mockito.mock(HttpServletRequest.class);
        this.openAPI = Mockito.mock(OpenAPI.class);
        this.dtoResponseHeader = new HashMap<>();
        dtoResponseHeader.put(this.DEFAULT_RESPONSE_HEADER_1, DEFAULT_RESPONSE_HEADER_VALUE_1);
        dtoResponseHeader.put(this.DEFAULT_RESPONSE_HEADER_2, DEFAULT_RESPONSE_HEADER_VALUE_2);
//        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File(classLoader.getResource("public/example_mocking_books.json").getFile());
//        this.openAPI= JsonMapper.mapper().readValue(new String(Files.readAllBytes(file.toPath())), OpenAPI.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateResponseHeaderWhenUsingResponseAsReturn() throws InvalidMockException {
        /**
         * Var response json structure
         *             [{
         *               "property": {
         *                 "name": "clientId",
         *                 "value": clientId
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "response": "ok"
         *               }]
         */
        List<Map<String, Object>> varResponseHeader = new ArrayList<>();
        Map<String, Object> headerProperty = new HashMap<>();
        Map<String, String> headerProperty1 = new HashMap<>();
        headerProperty1.put(MockHelper.NAME_PROPERTY, "clientId");
        headerProperty1.put(MockHelper.VALUE_PROPERTY, "clientId");
        headerProperty.put(MockHelper.PROPERTY, headerProperty1);

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setResponse("ok");
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.setHeaders(this.dtoResponseHeader);
        headerProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);

        varResponseHeader.add(headerProperty);

        Mockito.when(this.httpServletRequest.getHeader("clientId")).thenReturn("clientId");
        MockHelper mockHelper = MockHelper.generateResponseHeader(this.httpServletRequest, varResponseHeader, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals("ok"), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");
    }

    @Test
    void generateResponseHeaderWhenUsingComponentAsReturn() throws InvalidMockException {
        /**
         * Var response json structure
         *             [{
         *               "property": {
         *                 "name": "clientId",
         *                 "value": clientId
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "header":{
         *                     "x-header-id":"1",
         *                     "x-meta-id":"2"
         *                 }
         *                 "$ref": "componentResponse"
         *               }]
         */

        List<Map<String, Object>> varResponseHeader = new ArrayList<>();
        Map<String, Object> headerProperty = new HashMap<>();
        Map<String, String> headerProperty1 = new HashMap<>();
        headerProperty1.put(MockHelper.NAME_PROPERTY, "clientId");
        headerProperty1.put(MockHelper.VALUE_PROPERTY, "clientId");
        headerProperty.put(MockHelper.PROPERTY, headerProperty1);

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.set$ref("componentResponse");
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.setHeaders(this.dtoResponseHeader);
        headerProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);

        varResponseHeader.add(headerProperty);

        Mockito.when(this.openAPI.getComponents()).thenReturn(this.generateComponents());
        Mockito.when(this.httpServletRequest.getHeader("clientId")).thenReturn("clientId");
        MockHelper mockHelper = MockHelper.generateResponseHeader(this.httpServletRequest, varResponseHeader, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals(this.DEFAULT_COMPONENT_VALUE), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");

    }

    @Test
    void generateResponseHeaderNoHeaderFound() throws InvalidMockException {
        /**
         * Var response json structure
         *             [{
         *               "property": {
         *                 "name": "clientId",
         *                 "value": clientId
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "header":{
         *                     "x-header-id":"1",
         *                     "x-meta-id":"2"
         *                 }
         *                 "$ref": "componentResponse"
         *               }]
         */

        List<Map<String, Object>> varResponseHeader = new ArrayList<>();
        Map<String, Object> headerProperty = new HashMap<>();
        Map<String, String> headerProperty1 = new HashMap<>();
        headerProperty1.put(MockHelper.NAME_PROPERTY, "clientId");
        headerProperty1.put(MockHelper.VALUE_PROPERTY, "clientId");
        headerProperty.put(MockHelper.PROPERTY, headerProperty1);

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.set$ref("componentResponse");
        dtoMockResponseVmodel.setHttpCode(200);
        headerProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);

        varResponseHeader.add(headerProperty);

        Components components = new Components();
        Example example1 = new Example();
        example1.setValue("ok");
        Map<String, Example> componentExample1 = new HashMap<>();
        componentExample1.put("componentResponse", example1);
        components.setExamples(componentExample1);

        Mockito.when(this.openAPI.getComponents()).thenReturn(components);
        Mockito.when(this.httpServletRequest.getHeader("clientId")).thenReturn("notfound");
        MockHelper mockHelper = MockHelper.generateResponseHeader(this.httpServletRequest, varResponseHeader, this.openAPI.getComponents());
        isNull(mockHelper, "mockhelper is not null");
    }


    @Test
    void generateResponseBody() throws JsonProcessingException, InvalidMockException {
        /**
         * Var response json structure
         *             [{
         *               "property": {
         *                 "name": "firstname",
         *                 "value": fahmi
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "response": "ok"
         *               }]
         */

        List<Map<String, Object>> varResponseBody = new ArrayList<>();
        Map<String, Object> bodyProperty = new HashMap<>();

        Map<String, Object> bodyProperty1 = new HashMap<>();
        bodyProperty1.put(MockHelper.NAME_PROPERTY, "firstname");
        bodyProperty1.put(MockHelper.VALUE_PROPERTY, "fahmi");

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.setResponse("ok");

        bodyProperty.put(MockHelper.PROPERTY, bodyProperty1);
        bodyProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);
        varResponseBody.add(bodyProperty);

        //when json payload was  same with x-body-including
        String jsonPayload = "{\n" +
                "  \"firstname\": \"fahmi\"\n" +
                "}";

        Mockito.when(this.httpServletRequest.getMethod()).thenReturn(HttpMethod.POST.toString());

        MockHelper mockHelper = MockHelper.generateResponseBody(this.httpServletRequest, varResponseBody, jsonPayload, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals("ok"), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");

    }

    @Test
    void generateResponseBodyWhenNoMockFound() throws JsonProcessingException, InvalidMockException {
        /**
         * Var response json structure
         *             [{
         *               "property": {
         *                 "name": "firstname",
         *                 "value": fahmi
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "response": "ok"
         *               }]
         */
        List<Map<String, Object>> varResponseBody = new ArrayList<>();
        Map<String, Object> bodyProperty = new HashMap<>();

        Map<String, Object> bodyProperty1 = new HashMap<>();
        bodyProperty1.put(MockHelper.NAME_PROPERTY, "firstname");
        bodyProperty1.put(MockHelper.VALUE_PROPERTY, "fahmi");

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.setResponse("ok");

        bodyProperty.put(MockHelper.PROPERTY, bodyProperty1);
        bodyProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);
        varResponseBody.add(bodyProperty);

        Mockito.when(this.httpServletRequest.getMethod()).thenReturn(HttpMethod.POST.toString());

        //when json payload was not same with x-body-including
        String jsonPayload = "{\n" +
                "  \"firstname\": \"ajo\"\n" +
                "}";
        MockHelper mockHelper = MockHelper.generateResponseBody(this.httpServletRequest, varResponseBody, jsonPayload, this.openAPI.getComponents());
        isNull(mockHelper, "mockhelper is not null");
    }

    @Test
    void generateResponseBodyFromComponentsExample() throws JsonProcessingException, InvalidMockException {
        /**
         * Var response json structure
         *             [{
         *               "property": {
         *                 "name": "firstname",
         *                 "value": fahmi
         *               },
         *               "response": {
         *                 "httpCode": 400,
         *                 "$ref": "componentResponse"
         *               },
         *               {
         *               "property": {
         *                 "name": "firstname",
         *                 "value": ajo
         *               },
         *               "response": {
         *                 "httpCode": 400,
         *                 "$ref": "componentResponse2"
         *               }
         *               ]
         */
        List<Map<String, Object>> varResponseBody = new ArrayList<>();

        Map<String, Object> varProperty = new HashMap<>();
        Map<String, Object> bodyProperty1 = new HashMap<>();
        bodyProperty1.put(MockHelper.NAME_PROPERTY, "firstname");
        bodyProperty1.put(MockHelper.VALUE_PROPERTY, "fahmi");

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(400);
        dtoMockResponseVmodel.set$ref(this.DEFAULT_COMPONENT_NAME);

        varProperty.put(MockHelper.PROPERTY, bodyProperty1);
        varProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);


        Map<String, Object> varProperty2 = new HashMap<>();
        Map<String, Object> bodyProperty2 = new HashMap<>();
        bodyProperty2.put(MockHelper.NAME_PROPERTY, "firstname");
        bodyProperty2.put(MockHelper.VALUE_PROPERTY, "ajo");

        DtoMockResponseVmodel dtoMockResponseVmodel2 = new DtoMockResponseVmodel();
        dtoMockResponseVmodel2.setHttpCode(200);
        dtoMockResponseVmodel2.set$ref(this.DEFAULT_COMPONENT_NAME_2);

        varProperty2.put(MockHelper.PROPERTY, bodyProperty2);
        varProperty2.put(MockHelper.RESPONSE, dtoMockResponseVmodel2);


        varResponseBody.add(varProperty);
        varResponseBody.add(varProperty2);


        String jsonPayload = "{\n" +
                "  \"firstname\": \"fahmi\"\n" +
                "}";

        Mockito.when(this.openAPI.getComponents()).thenReturn(this.generateComponents());
        Mockito.when(this.httpServletRequest.getMethod()).thenReturn(HttpMethod.POST.toString());

        MockHelper mockHelper = MockHelper.generateResponseBody(this.httpServletRequest, varResponseBody, jsonPayload, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals(this.DEFAULT_COMPONENT_VALUE), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 400, "response is not expected");

        jsonPayload = "{\n" +
                "  \"firstname\": \"ajo\"\n" +
                "}";
        mockHelper = MockHelper.generateResponseBody(this.httpServletRequest, varResponseBody, jsonPayload, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals(this.DEFAULT_COMPONENT_NAME_2_VALUE), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");
    }

    @Test
    void generateResponseBodyFromComponentsExampleNotFound() throws JsonProcessingException {
        /**
         * Var response json structure
         *             [{
         *               "property": {
         *                 "name": "firstname",
         *                 "value": fahmi
         *               },
         *               "response": {
         *                 "httpCode": 400,
         *                 "$ref": "componentResponse"
         *               }
         *               ]
         */
        List<Map<String, Object>> varResponseBody = new ArrayList<>();
        Map<String, Object> bodyProperty = new HashMap<>();

        Map<String, Object> bodyProperty1 = new HashMap<>();
        bodyProperty1.put(MockHelper.NAME_PROPERTY, "firstname");
        bodyProperty1.put(MockHelper.VALUE_PROPERTY, "fahmi");

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(400);
        dtoMockResponseVmodel.setResponse(null);
        dtoMockResponseVmodel.set$ref("a");

        bodyProperty.put(MockHelper.PROPERTY, bodyProperty1);
        bodyProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);
        varResponseBody.add(bodyProperty);
        bodyProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);
        String jsonPayload = "{\n" +
                "  \"firstname\": \"fahmi\"\n" +
                "}";

        Mockito.when(this.openAPI.getComponents()).thenReturn(this.generateComponents());
        Mockito.when(this.httpServletRequest.getMethod()).thenReturn(HttpMethod.POST.toString());

        try {
            MockHelper.generateResponseBody(this.httpServletRequest, varResponseBody, jsonPayload, this.openAPI.getComponents());
        } catch (InvalidMockException e) {
            isTrue(e.getMessage().equals("Rereference mocks its not valid"), "exception get message is not same");
        }
    }

    @Test
    void generateResponseDefault() throws InvalidMockException {
        /**
         * Var response json structure
         *             {
         *               "response": {
         *                 "httpCode": 200,
         *                 "response": "ok"
         *               }
         */
        Map<String, Object> varResponse = new HashMap<>();

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.setResponse("ok");
        varResponse.put(MockHelper.RESPONSE, dtoMockResponseVmodel);

        MockHelper mockHelper = MockHelper.generateResponseDefault(varResponse, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals("ok"), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");
        isNull(mockHelper.getResponse().getHeaders(), "headers is not null");

    }

    @Test
    void generateResponseDefaultFromComponentExample() throws InvalidMockException {
        /**
         * Var response json structure
         *             {
         *               "response": {
         *                 "httpCode": 200,
         *                 "headers":{
         *                     "x-header-id":"1",
         *                     "x-meta-id":"2"
         *                 }
         *                 "$ref": "componentResponse"
         *               }
         */
        Map<String, Object> varResponse = new HashMap<>();

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.set$ref(this.DEFAULT_COMPONENT_NAME);
        dtoMockResponseVmodel.setHeaders(dtoResponseHeader);

        varResponse.put(MockHelper.RESPONSE, dtoMockResponseVmodel);

        Mockito.when(this.openAPI.getComponents()).thenReturn(this.generateComponents());

        MockHelper mockHelper = MockHelper.generateResponseDefault(varResponse, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals(this.DEFAULT_COMPONENT_VALUE), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_1).equals(DEFAULT_RESPONSE_HEADER_VALUE_1), "response header is null");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_2).equals(DEFAULT_RESPONSE_HEADER_VALUE_2), "response header is null");

    }

    @Test
    void generateResponsePath() throws InvalidMockException {
        /**
         * Var response json structure
         *             {
         *               "property": {
         *                 "name": "ID",
         *                 "value": 10
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "headers":{
         *                     "x-request-id":"1",
         *                     "x-meta-id":"2"
         *                 }
         *                 "$ref": "componentResponse"
         *               }
         */
        List<Map<String, Object>> varResponse = new ArrayList<>();
        Map<String, Object> bodyProperty = new HashMap<>();

        Map<String, Object> bodyProperty1 = new HashMap<>();
        bodyProperty1.put(MockHelper.NAME_PROPERTY, "ID");
        bodyProperty1.put(MockHelper.VALUE_PROPERTY, "10");

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.setResponse("ok");
        dtoMockResponseVmodel.setHeaders(dtoResponseHeader);

        bodyProperty.put(MockHelper.PROPERTY, bodyProperty1);
        bodyProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);
        varResponse.add(bodyProperty);

        //openapi path example `/path2/{id}
        String[] extractedResultOpenapiPath = {"", "path2", "*{ID}"};
        //path request example `/path2/10
        String[] extractedPathRequest = {"", "path2", "10"};

        MockHelper mockHelper = MockHelper.generateResponsePath(varResponse, extractedResultOpenapiPath, extractedPathRequest, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals("ok"), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_1).equals(DEFAULT_RESPONSE_HEADER_VALUE_1), "response header is null");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_2).equals(DEFAULT_RESPONSE_HEADER_VALUE_2), "response header is null");

    }

    @Test
    void generateResponsePathFromComponentExample() throws InvalidMockException {
        /**
         * Var response json structure
         *             {
         *               "property": {
         *                 "name": "ID",
         *                 "value": 10
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "$ref": "componentResponse"
         *               }
         */
        List<Map<String, Object>> varResponse = new ArrayList<>();
        Map<String, Object> bodyProperty = new HashMap<>();

        Map<String, Object> bodyProperty1 = new HashMap<>();
        bodyProperty1.put(MockHelper.NAME_PROPERTY, "ID");
        bodyProperty1.put(MockHelper.VALUE_PROPERTY, "10");

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.set$ref(this.DEFAULT_COMPONENT_NAME);

        bodyProperty.put(MockHelper.PROPERTY, bodyProperty1);
        bodyProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);
        varResponse.add(bodyProperty);

        Mockito.when(this.openAPI.getComponents()).thenReturn(this.generateComponents());

        //openapi path example `/path2/{id}
        String[] extractedResultOpenapiPath = {"", "path2", "*{ID}"};
        //path request example `/path2/10
        String[] extractedPathRequest = {"", "path2", "10"};

        MockHelper mockHelper = MockHelper.generateResponsePath(varResponse, extractedResultOpenapiPath, extractedPathRequest, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals(this.DEFAULT_COMPONENT_VALUE), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");
        isNull(mockHelper.getResponse().getHeaders(), "headers is not null");

    }

    @Test
    void generateResponsePathNotFound() throws InvalidMockException {
        /**
         * Var response json structure
         *             [{
         *               "property": {
         *                 "name": "ID",
         *                 "value": 11
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "$ref": "componentResponse"
         *               }]
         */
        List<Map<String, Object>> varResponse = new ArrayList<>();
        Map<String, Object> bodyProperty = new HashMap<>();

        Map<String, Object> bodyProperty1 = new HashMap<>();
        bodyProperty1.put(MockHelper.NAME_PROPERTY, "ID");
        bodyProperty1.put(MockHelper.VALUE_PROPERTY, "10");

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.set$ref(this.DEFAULT_COMPONENT_NAME);

        bodyProperty.put(MockHelper.PROPERTY, bodyProperty1);
        bodyProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);
        varResponse.add(bodyProperty);


        //openapi path example `/path2/{id}
        String[] extractedResultOpenapiPath = {"", "path2", "*{ID}", "test"};
        //path request example `/path2/10
        String[] extractedPathRequest = {"", "path2", "110", "test"};

        MockHelper mockHelper = MockHelper.generateResponsePath(varResponse, extractedResultOpenapiPath, extractedPathRequest, this.openAPI.getComponents());
        isNull(mockHelper, "mockhelper is not null");

    }

    @Test
    void generateResponsePathMultiplePath() throws InvalidMockException {
        /**
         * Var response json structure
         *             {
         *               "property": {
         *                 "name": "ID",
         *                 "value": 10
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "$ref": "componentResponse"
         *               }
         */
        List<Map<String, Object>> varResponse = new ArrayList<>();

        Map<String, Object> varProperty = new HashMap<>();
        Map<String, Object> bodyProperty1 = new HashMap<>();
        bodyProperty1.put(MockHelper.NAME_PROPERTY, "ID");
        bodyProperty1.put(MockHelper.VALUE_PROPERTY, "10");

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.set$ref(this.DEFAULT_COMPONENT_NAME);
        dtoMockResponseVmodel.setHeaders(dtoResponseHeader);
        ;

        varProperty.put(MockHelper.PROPERTY, bodyProperty1);
        varProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);

        Map<String, Object> varProperty2 = new HashMap<>();
        Map<String, Object> bodyProperty2 = new HashMap<>();
        bodyProperty2.put(MockHelper.NAME_PROPERTY, "test");
        bodyProperty2.put(MockHelper.VALUE_PROPERTY, "10");

        DtoMockResponseVmodel dtoMockResponseVmodel2 = new DtoMockResponseVmodel();
        dtoMockResponseVmodel2.setHttpCode(400);
        dtoMockResponseVmodel2.setResponse("ok");
        Map<String, Object> dtoResponseHeader2 = new HashMap<>();
        dtoResponseHeader2.put(this.DEFAULT_RESPONSE_HEADER_1, DEFAULT_RESPONSE_HEADER_VALUE_1);
        dtoResponseHeader2.put(this.DEFAULT_RESPONSE_HEADER_2, DEFAULT_RESPONSE_HEADER_VALUE_2);
        dtoMockResponseVmodel2.setHeaders(dtoResponseHeader2);
        ;
        varProperty2.put(MockHelper.PROPERTY, bodyProperty2);
        varProperty2.put(MockHelper.RESPONSE, dtoMockResponseVmodel2);

        varResponse.add(varProperty);
        varResponse.add(varProperty2);


        Mockito.when(this.openAPI.getComponents()).thenReturn(this.generateComponents());

        //openapi path example `/path2/{id}
        String[] extractedResultOpenapiPath = {"", "path2", "*{ID}", "test", "*{test}"};
        //path request example `/path2/10
        String[] extractedPathRequest = {"", "path2", "10", "test", "112"};

        MockHelper mockHelper = MockHelper.generateResponsePath(varResponse, extractedResultOpenapiPath, extractedPathRequest, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals("ok"), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_1).equals(DEFAULT_RESPONSE_HEADER_VALUE_1), "response header is null");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_2).equals(DEFAULT_RESPONSE_HEADER_VALUE_2), "response header is null");

        //openapi path example `/path2/{id}
        String[] extractedResultOpenapiPath1 = {"", "path2", "*{ID}", "test", "*{test}"};
        //path request example `/path2/10
        String[] extractedPathRequest1 = {"", "path2", "110", "test", "10"};

        mockHelper = MockHelper.generateResponsePath(varResponse, extractedResultOpenapiPath1, extractedPathRequest1, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals("ok"), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 400, "response is not expected");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_1).equals(DEFAULT_RESPONSE_HEADER_VALUE_1), "response header is null");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_2).equals(DEFAULT_RESPONSE_HEADER_VALUE_2), "response header is null");

    }

    @Test
    void generateResponseQuery() throws UnsupportedEncodingException, InvalidMockException {
        /**
         * Var response json structure
         *             [{
         *               "property": {
         *                 "name": "query",
         *                 "value": "test"
         *               },
         *               "response": {
         *                 "httpCode": 200,
         *                 "response": "ok",
         *                 "headers":{
         *                     "x-header-id":"1",
         *                     "x-meta-id":"2"
         *                 }
         *               }]
         */
        List<Map<String, Object>> varResponse = new ArrayList<>();
        Map<String, Object> bodyProperty = new HashMap<>();

        Map<String, Object> bodyProperty1 = new HashMap<>();
        bodyProperty1.put(MockHelper.NAME_PROPERTY, "query");
        bodyProperty1.put(MockHelper.VALUE_PROPERTY, "test");

        DtoMockResponseVmodel dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(200);
        dtoMockResponseVmodel.setResponse("ok");
        dtoMockResponseVmodel.setHeaders(dtoResponseHeader);
        ;

        bodyProperty.put(MockHelper.PROPERTY, bodyProperty1);
        bodyProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);
        varResponse.add(bodyProperty);

        //example query string `path=/books&query=title:empty&sort=published:desc`
        Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("path=/test?query=test&sort=published:desc");
        MockHelper mockHelper = MockHelper.generateResponseQuery(this.httpServletRequest, varResponse, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals("ok"), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");
        isTrue(mockHelper.getResponse().getHeaders() != null, "response header is null");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_1).equals(DEFAULT_RESPONSE_HEADER_VALUE_1), "response header is null");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_2).equals(DEFAULT_RESPONSE_HEADER_VALUE_2), "response header is null");

        Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("path=/test&query=test&sort=published:desc");
        mockHelper = MockHelper.generateResponseQuery(this.httpServletRequest, varResponse, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals("ok"), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 200, "response is not expected");

        //getting from component example
        dtoMockResponseVmodel = new DtoMockResponseVmodel();
        dtoMockResponseVmodel.setHttpCode(500);
        dtoMockResponseVmodel.setResponse(null);
        dtoMockResponseVmodel.set$ref(this.DEFAULT_COMPONENT_NAME);
        dtoMockResponseVmodel.setHeaders(dtoResponseHeader);
        bodyProperty.put(MockHelper.RESPONSE, dtoMockResponseVmodel);

        Mockito.when(this.httpServletRequest.getQueryString()).thenReturn("path=/test&query=test&sort=published:desc");
        Mockito.when(this.openAPI.getComponents()).thenReturn(this.generateComponents());
        mockHelper = MockHelper.generateResponseQuery(this.httpServletRequest, varResponse, this.openAPI.getComponents());
        isTrue(mockHelper.getResponse().getResponse().equals("ok"), "response is not expected");
        isTrue(mockHelper.getResponse().getHttpCode() == 500, "response is not expected");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_1).equals(DEFAULT_RESPONSE_HEADER_VALUE_1), "response header is null");
        isTrue(mockHelper.getResponse().getHeaders().get(DEFAULT_RESPONSE_HEADER_2).equals(DEFAULT_RESPONSE_HEADER_VALUE_2), "response header is null");

    }


    private Components generateComponents() {
        Components components = new Components();

        Example example1 = new Example();
        example1.setValue(this.DEFAULT_COMPONENT_VALUE);

        Example example2 = new Example();
        example2.setValue(this.DEFAULT_COMPONENT_NAME_2_VALUE);

        Map<String, Example> componentExample1 = new HashMap<>();
        componentExample1.put(this.DEFAULT_COMPONENT_NAME, example1);
        componentExample1.put(this.DEFAULT_COMPONENT_NAME_2, example2);
        components.setExamples(componentExample1);
        return components;
    }
}
