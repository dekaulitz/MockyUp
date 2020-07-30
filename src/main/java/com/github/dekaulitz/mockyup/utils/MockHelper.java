package com.github.dekaulitz.mockyup.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockResponseVmodel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.InvalidMockException;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.Components;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
/**
 * mock helper for handling mock extesion from openapi
 */
public class MockHelper {
    public static final String X_PATH = "x-path-including";
    public final static String NAME_PROPERTY = "name";
    public final static String VALUE_PROPERTY = "value";
    public static final String X_EXAMPLES = "x-examples";
    public static final String X_HEADERS = "x-header-including";
    public static final String X_QUERY = "x-query-including";
    public static final String X_BODY = "x-body-including";
    public static final String X_DEFAULT = "x-default";

    public final static String PROPERTY = "property";
    public final static String RESPONSE = "response";
    @Getter
    @Setter
    private Map<String, Object> property;
    @Setter
    @Getter
    private DtoMockResponseVmodel response;

    /**
     * @param request           {@link HttpServletRequest} request from servlet for geting header request attributes
     * @param exampleExtensions {@link List<Map>} example extension [x-examples] from operation
     * @param components        {@link Components} components from open api
     * @return MockHelper mock response
     * @desc generate mock response base on header
     */
    public static MockHelper generateResponseHeader(HttpServletRequest request, List<Map<String, Object>> exampleExtensions, Components components) throws InvalidMockException {
        //iterate [x-header-including] properties
        for (Map<String, Object> extension : exampleExtensions) {
            try {
                MockHelper mockHelper = new MockHelper();
                //parsing extension into mockhelper
                parsingMockFromJsonMapper(mockHelper, extension);
                throwInvalidMockExample(mockHelper);
                //try to get header name with extension property name if matched with request header
                String extentionHeaderMatched = request.getHeader((String) mockHelper.getProperty().get(MockHelper.NAME_PROPERTY));
                if (!extentionHeaderMatched.isEmpty()) {
                    //get the value from request header and comparing with [x-header-including] value
                    if (extentionHeaderMatched.equals(mockHelper.getProperty().get(MockHelper.VALUE_PROPERTY))) {
                        //if the response from [x-header-including] is component that referencing with Componenents will get the value from components
                        getComponentReff(mockHelper, components);
                        return mockHelper;
                    }
                } else {
                    //[x-header-including] is null which is the value cannot be null
                    if (mockHelper.getProperty().get(VALUE_PROPERTY) == null) {
                        //if the response from [x-header-including] is component that referencing with Componenents will get the value from components
                        getComponentReff(mockHelper, components);
                        return mockHelper;
                    }
                }
            } catch (Exception e) {
                throw new InvalidMockException(ResponseCode.INVALID_MOCK_HEADER, e);
            }
        }
        return null;
    }

    /**
     * @param request           {@link HttpServletRequest} request from servlet for geting header request attributes
     * @param exampleExtensions {@link List<Map>} example extension [x-examples] from operation
     * @param body              {@link String} this for mocking the request body if is required
     * @param components        {@link Components} components from open api
     * @return MockHelper for mock response
     * @throws JsonProcessingException
     */
    public static MockHelper generateResponseBody(HttpServletRequest request, List<Map<String, Object>> exampleExtensions, String body, Components components) throws JsonProcessingException, InvalidMockException {
        //get all method type that matched with request method
        if (request.getMethod().equals(HttpMethod.POST.toString()) || request.getMethod().equals(HttpMethod.PATCH.toString())
                || request.getMethod().equals(HttpMethod.PUT.toString()))
            //iterate [x-body-including] properties
            for (Map<String, Object> extension : exampleExtensions) {
                //converting the string payload request into jsonnode
                JsonNode requestBody = Json.mapper().readTree(body);
                MockHelper mockHelper = new MockHelper();
                //parsing extension[x-body-including] into mockHelper
                parsingMockFromJsonMapper(mockHelper, extension);
                throwInvalidMockExample(mockHelper);

                //checking jsonnode is containing  [x-body-including] property
                boolean isBodyRequestMatched = requestBody.has((String) mockHelper.getProperty().get(MockHelper.NAME_PROPERTY));
                if (isBodyRequestMatched) {
                    // get the bodyRequest node that matcheng with [x-body-including] property
                    String bodyRequest = requestBody.findPath((String) mockHelper.getProperty().get(MockHelper.NAME_PROPERTY)).asText();
                    if (bodyRequest != null) {
                        //check the body request
                        if (!bodyRequest.isEmpty()) {
                            //check the bodyRequest matched with [x-body-including] property value
                            if (bodyRequest.equals(mockHelper.getProperty().get(MockHelper.VALUE_PROPERTY))) {
                                //if the response from [x-body-including] is component that referencing with Componenents will get the value from components
                                getComponentReff(mockHelper, components);
                                return mockHelper;
                            }
                            //if [x-body-including] value is null which is the value should exist
                            //will check the value
                            if (bodyRequest.equalsIgnoreCase("null") && mockHelper.getProperty().get(VALUE_PROPERTY) == null) {
                                //if the response from [x-body-including] is component that referencing with Componenents will get the value from components
                                getComponentReff(mockHelper, components);
                                return mockHelper;
                            }
                        }
                    }
                    //[x-body-including] property is not exist on the bodyRequest but the property is mandatory from [swagger_path_contract]
                } else if (mockHelper.getProperty().get(VALUE_PROPERTY) == null) {
                    //if the response from [x-body-including] is component that referencing with Componenents will get the value from components
                    getComponentReff(mockHelper, components);
                    return mockHelper;
                }
            }
        return null;
    }

    /**
     * @param exampleExtensions {@link List<Map>} example extension [x-examples] from operation
     * @param openApiRoutePath  {@link String[]}[swagger_path_contract] that mapped with [contract_path_that_want_to_mock]
     * @param paths             {@link String[]} [contract_path_that_want_to_mock]
     * @param components        {@link Components} components from open api
     * @return MockHelper {@link MockHelper} the response from mock response
     */
    public static MockHelper generateResponsePath(List<Map<String, Object>> exampleExtensions, String[] openApiRoutePath, String[] paths, Components components) throws InvalidMockException {
        //iterate [x-path-including] properties
        for (Map<String, Object> stringObjectMap : exampleExtensions) {
            MockHelper mockHelper = new MockHelper();

            //parsing [x-path-including] response into mockhelper
            parsingMockFromJsonMapper(mockHelper, stringObjectMap);
            // throw the exception if mockhelper was null
            throwInvalidMockExample(mockHelper);

            //iterate openApiRoutePath [swagger_path_contract] its the source of the truth from route
            for (int i = 0; i < openApiRoutePath.length; i++) {
                //mapping the openApiRoutePath with path [contract_path_that_want_to_mock]
                if (!openApiRoutePath[i].equals(paths[i])) {

                    //if the segment was matched will check the property from [x-path-including] property
                    if (openApiRoutePath[i].contains((CharSequence) mockHelper.getProperty().get(MockHelper.NAME_PROPERTY))) {
                        //if the segement [contract_path_that_want_to_mock] is matched with [x-path-including] property will check the value
                        if (paths[i].equals(mockHelper.getProperty().get(MockHelper.VALUE_PROPERTY).toString())) {
                            //if the response from [x-path-including] is component that referencing with Componenents will get the value from components
                            getComponentReff(mockHelper, components);
                            return mockHelper;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param request           {@link HttpServletRequest} for getting the query from the request attribute
     * @param exampleExtensions {@link List<Map>} example extension [x-examples] from operation
     * @param components        {@link Components} components from open api
     * @return
     * @desc generate response query base on query
     */
    public static MockHelper generateResponseQuery(HttpServletRequest request, List<Map<String, Object>> exampleExtensions, Components components) throws InvalidMockException, UnsupportedEncodingException {
        //for query mock response for the ordering its depend on your mock query-including
        //if youre first query was a=x&b=y but from the query mock struct b=y its defined first so it will throwing response from b=y
        //iterate [x-query-including] properties
        for (Map<String, Object> extension : exampleExtensions) {
            MockHelper mockHelper = new MockHelper();
            //parsing [x-query-including] response into mockhelper
            parsingMockFromJsonMapper(mockHelper, extension);
            throwInvalidMockExample(mockHelper);

            //get all query string from request
            String cleanQueryString = java.net.URLDecoder.decode(request.getQueryString(), String.valueOf(StandardCharsets.UTF_8));
            //extract query string for mapping with [x-query-including] properties
            String[] queryStrings = cleanQueryString.split("\\?");

            //check query string length if more that 1 which is query string should be validate with [x-query-including] property
            if (queryStrings.length > 1) {
                Map<String, String> q = decodeQueryString(queryStrings[1]);
                for (Map.Entry<String, String> qmap : q.entrySet()) {
                    if ((qmap.getKey().equals(mockHelper.getProperty().get(MockHelper.NAME_PROPERTY)))) {
                        if ((qmap.getValue().equals(mockHelper.getProperty().get(MockHelper.VALUE_PROPERTY)))) {
                            //if the response from [x-query-including] is component that referencing with Componenents will get the value from components
                            getComponentReff(mockHelper, components);
                            return mockHelper;
                        }
                        if (mockHelper.getProperty().get(VALUE_PROPERTY) == null && qmap.getValue().equals("null")) {
                            //if the response from [x-query-including] is component that referencing with Componenents will get the value from components
                            getComponentReff(mockHelper, components);
                            return mockHelper;
                        }
                    } else {
                        if (mockHelper.getProperty().get(VALUE_PROPERTY) == null &&
                                !cleanQueryString.contains(mockHelper.getProperty().get(MockHelper.NAME_PROPERTY).toString())) {
                            //if the response from [x-query-including] is component that referencing with Componenents will get the value from components
                            getComponentReff(mockHelper, components);
                            return mockHelper;
                        }
                    }
                }
            } else {
                if (mockHelper.getProperty().get(VALUE_PROPERTY) == null) {
                    //if the response from [x-query-including] is component that referencing with Componenents will get the value from components
                    getComponentReff(mockHelper, components);
                    return mockHelper;
                } else {
                    if (cleanQueryString.contains(mockHelper.getProperty().get(MockHelper.NAME_PROPERTY) + "=" + mockHelper.getProperty().get(MockHelper.VALUE_PROPERTY))) {
                        //if the response from [x-query-including] is component that referencing with Componenents will get the value from components
                        getComponentReff(mockHelper, components);
                        return mockHelper;
                    }
                }
            }

        }
        return null;
    }

    /**
     * @param exampleExtensions {@link List<Map>} example extension [x-examples] from operation
     * @param components        {@link Components} components from open api
     * @return MockHelper mock response
     */
    public static MockHelper generateResponseDefault(Map<String, Object> exampleExtensions, Components components) throws InvalidMockException {
        //if [x-default] was decided but there no request that matched with the mock response so it will rendering the response from [x-default]
        MockHelper mockHelper = new MockHelper();
        //parsing [x-default] into mockHelper
        parsingMockFromJsonMapper(mockHelper, exampleExtensions);
        //reff component example was decided but there is no component that registered so will throw the exception
        if (mockHelper.getResponse() == null) {
            throw new InvalidMockException(ResponseCode.INVALID_MOCK_DEFAULT);
        }
        //if the response from [x-default-including] is component that referencing with Componenents will get the value from components
        getComponentReff(mockHelper, components);
        return mockHelper;
    }

    private static void parsingMockFromJsonMapper(MockHelper mockHelper, Map<String, Object> xExamples) {
        mockHelper.setProperty(JsonMapper.mapper().convertValue(xExamples.get(PROPERTY), Map.class));
        mockHelper.setResponse(JsonMapper.mapper().convertValue(xExamples.get(RESPONSE), DtoMockResponseVmodel.class));
    }

    /**
     * @param mockHelper {@link MockHelper} response mock
     * @param components {@link Components} components from open api
     * @throws InvalidMockException will throw if the parser was fail
     */
    private static void getComponentReff(MockHelper mockHelper, Components components) throws InvalidMockException {
        //check if reff is defined
        if (mockHelper.getResponse().get$ref() != null) {
            //get the name component
            String refName = OpenAPITools.getSimpleRef((String) mockHelper.getResponse().get$ref());
            if (!refName.isEmpty()) {
                try {
                    //injecting the component schema into mock helper response
                    mockHelper.getResponse().setResponse(components.getExamples().get(refName).getValue());
                } catch (NullPointerException n) {
                    //if something componeng injecting was failing will thrown the exception
                    throw new InvalidMockException(ResponseCode.INVALID_MOCK_REF);
                }
            }

        }
    }

    /**
     * @param query {@link String} queryString from request
     * @return Map<String, String>
     * @throws InvalidMockException when querystring is failing
     */
    private static Map<String, String> decodeQueryString(String query) throws InvalidMockException {
        String[] params = query.split("\\&");
        Map<String, String> map = new HashMap<>();
        String value = "";
        String name = "";
        for (String param : params) {
            if (param.split("=").length <= 1) {
                name = param.split("=")[0];
                value = "null";
            } else {
                name = param.split("=")[0];
                value = param.split("=")[1];
            }
            map.put(name, value);
        }

        return map;
    }

    /**
     * @param mockHelper {@link MockHelper} mock response
     * @throws InvalidMockException will throw if the mockhelper is not defined properly
     */
    private static void throwInvalidMockExample(MockHelper mockHelper) throws InvalidMockException {
        if (mockHelper.getResponse() == null || mockHelper.getProperty() == null) {
            throw new InvalidMockException(ResponseCode.INVALID_MOCKUP_STRUCTURE);
        }
    }
}
