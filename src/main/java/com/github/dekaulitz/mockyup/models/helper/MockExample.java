package com.github.dekaulitz.mockyup.models.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.models.MockResponseModel;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import io.swagger.util.Json;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class MockExample {
    @Getter
    @Setter
    private Map<String, Object> property;
    public static final String X_PATH = "x-path-including";

    public final static String NAME_PROPERTY = "name";
    public final static String VALUE_PROPERTY = "value";

    public static final String X_EXAMPLES = "x-examples";
    public static final String X_HEADERS = "x-header-including";
    public static final String X_QUERY = "x-query-including";
    public static final String X_BODY = "x-body-including";
    public static final String X_DEFAULT = "x-default";
    @Getter
    @Setter
    private MockResponseModel response;

    /**
     * @param request
     * @param extension
     * @return
     * @desc generate mock response base on header
     */
    public static MockExample generateResponseHeader(HttpServletRequest request, List<Map<String, Object>> extension) throws InvalidMockException {
        for (Map<String, Object> stringObjectMap : extension) {
            try {
                MockExample mockExample = new MockExample();
                mockExample.setProperty(JsonMapper.mapper().convertValue(stringObjectMap.get("property"), Map.class));
                mockExample.setResponse(JsonMapper.mapper().convertValue(stringObjectMap.get("response"), MockResponseModel.class));
                throwInvalidMockExample(mockExample, X_HEADERS);
                String requestHeader = request.getHeader((String) mockExample.getProperty().get(MockExample.NAME_PROPERTY));
                if (requestHeader != null) {
                    if (requestHeader.equals(mockExample.getProperty().get(MockExample.VALUE_PROPERTY))) {
                        return mockExample;
                    }
                } else {
                    if (mockExample.getProperty().get(VALUE_PROPERTY) == null) {
                        return mockExample;
                    }
                }
            } catch (Exception e) {
                throw new InvalidMockException("invalid mocks exception " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * @param request
     * @param extension
     * @param body
     * @return
     * @throws JsonProcessingException
     * @desc generate mock response base on body
     */
    public static MockExample generateResponseBody(HttpServletRequest request, List<Map<String, Object>> extension, String body) throws JsonProcessingException, InvalidMockException {
        if (request.getMethod().equals(HttpMethod.POST.toString()) || request.getMethod().equals(HttpMethod.PATCH.toString())
                || request.getMethod().equals(HttpMethod.PUT.toString()))
            for (Map<String, Object> stringObjectMap : extension) {
                JsonNode requestBody = Json.mapper().readTree(body);
                MockExample mockExample = new MockExample();
                mockExample.setProperty(JsonMapper.mapper().convertValue(stringObjectMap.get("property"), Map.class));
                mockExample.setResponse(JsonMapper.mapper().convertValue(stringObjectMap.get("response"), MockResponseModel.class));
                throwInvalidMockExample(mockExample, X_BODY);
                boolean isBodyExist = requestBody.has((String) mockExample.getProperty().get(MockExample.NAME_PROPERTY));
                if (isBodyExist) {
                    String bodyRequest = requestBody.findPath((String) mockExample.getProperty().get(MockExample.NAME_PROPERTY)).asText();
                    if (bodyRequest != null) {
                        if (!bodyRequest.isEmpty()) {
                            if (bodyRequest.equals(mockExample.getProperty().get(MockExample.VALUE_PROPERTY))) {
                                return mockExample;
                            }
                        } else {
                            if (mockExample.getProperty().get(VALUE_PROPERTY) == null) {
                                return mockExample;
                            }
                        }
                    }
                }
            }
        return null;
    }

    /**
     * @param request
     * @param extension
     * @param openAPIPaths
     * @param paths
     * @return
     * @desc generate mock response base on path
     */
    public static MockExample generateResponnsePath(HttpServletRequest request, List<Map<String, Object>> extension, String[] openAPIPaths, String[] paths) throws InvalidMockException {
        for (Map<String, Object> stringObjectMap : extension) {
            MockExample mockExample = new MockExample();
            mockExample.setProperty(JsonMapper.mapper().convertValue(stringObjectMap.get("property"), Map.class));
            mockExample.setResponse(JsonMapper.mapper().convertValue(stringObjectMap.get("response"), MockResponseModel.class));

            throwInvalidMockExample(mockExample, X_PATH);
            for (int i = 0; i < openAPIPaths.length; i++) {
                if (!openAPIPaths[i].equals(paths[i])) {
                    if (openAPIPaths[i].contains((CharSequence) mockExample.getProperty().get(MockExample.NAME_PROPERTY))) {
                        if (paths[i].equals(mockExample.getProperty().get(MockExample.VALUE_PROPERTY))) {
                            return mockExample;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param request
     * @param extension
     * @return
     * @desc generate response query base on query
     */
    public static MockExample generateResponnseQuery(HttpServletRequest request, List<Map<String, Object>> extension) throws InvalidMockException, UnsupportedEncodingException {
        for (Map<String, Object> stringObjectMap : extension) {
            MockExample mockExample = new MockExample();
            mockExample.setProperty(JsonMapper.mapper().convertValue(stringObjectMap.get("property"), Map.class));
            mockExample.setResponse(JsonMapper.mapper().convertValue(stringObjectMap.get("response"), MockResponseModel.class));
            throwInvalidMockExample(mockExample, X_QUERY);
            String cleanQueryString = java.net.URLDecoder.decode(request.getQueryString(), String.valueOf(StandardCharsets.UTF_8));
            String[] queryStrings = cleanQueryString.split("\\?");
            if (queryStrings.length > 1) {
                Map<String, String> q = decodeQueryString(queryStrings[1]);
                for (Map.Entry<String, String> qmap : q.entrySet()) {
                    if ((qmap.getKey().equals(mockExample.getProperty().get(MockExample.NAME_PROPERTY)))) {
                        if ((qmap.getValue().equals(mockExample.getProperty().get(MockExample.VALUE_PROPERTY))))
                            return mockExample;
                    } else {
                        if (mockExample.getProperty().get(VALUE_PROPERTY) == null) {
                            return mockExample;
                        }
                    }
                }
            } else {
                if (mockExample.getProperty().get(VALUE_PROPERTY) == null) {
                    return mockExample;
                } else {
                    if (cleanQueryString.contains(mockExample.getProperty().get(MockExample.NAME_PROPERTY) + "=" + mockExample.getProperty().get(MockExample.VALUE_PROPERTY))) {
                        return mockExample;
                    }
                }
            }

        }
        return null;
    }

    /**
     * @param value
     * @return
     * @desc generate default response
     */
    public static MockExample generateResponseDefault(LinkedHashMap<String, Object> value) throws InvalidMockException {
        MockExample mockExample = new MockExample();
        mockExample.setProperty(JsonMapper.mapper().convertValue(value.get("property"), Map.class));
        mockExample.setResponse(JsonMapper.mapper().convertValue(value.get("response"), MockResponseModel.class));
        throwInvalidMockExample(mockExample, X_DEFAULT);
        return mockExample;
    }

    private static Map<String, String> decodeQueryString(String query) throws InvalidMockException {
        String[] params = query.split("\\&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            if (param.split("=").length <= 1) throw new InvalidMockException("Invalid query format");
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    private static void throwInvalidMockExample(MockExample mockExample, String type) throws InvalidMockException {
        if (mockExample.getResponse() == null || mockExample.getProperty() == null) {
            throw new InvalidMockException("invalid mock " + type);
        }
    }
}
