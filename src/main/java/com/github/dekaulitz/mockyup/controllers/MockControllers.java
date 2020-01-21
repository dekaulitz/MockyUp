package com.github.dekaulitz.mockyup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.entities.MockEntities;
import com.github.dekaulitz.mockyup.models.MockExample;
import com.github.dekaulitz.mockyup.models.MockModel;
import com.github.dekaulitz.mockyup.repositories.MockRepositories;
import com.github.dekaulitz.mockyup.vmodels.MockVmodel;
import io.swagger.parser.OpenAPIParser;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@Log4j2
public class MockControllers {
    private final static String NAME_PROPERTY = "name";
    private final static String VALUE_PROPERTY = "value";
    @Autowired
    private final MockRepositories mockRepositories;

    public MockControllers(MockRepositories mockRepositories) {
        this.mockRepositories = mockRepositories;
    }

    public static Map<String, String> decodeQueryString(String query) {
        String[] params = query.split("\\&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    @GetMapping(value = "/")
    public ResponseEntity greeting() {
        return ResponseEntity.ok("mock example");
    }

    @RequestMapping(value = "/mocks/mocking/{id}", method = {RequestMethod.OPTIONS, RequestMethod.DELETE,
            RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PATCH, RequestMethod.PUT,
            RequestMethod.TRACE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity mockingPath(@RequestParam(value = "path", required = false) String path,
                                      @PathVariable String id, @RequestBody(required = false) String body,
                                      HttpServletRequest request)
            throws JsonProcessingException {
        //this call for avoiding multiple query string
        String[] originalPathUri = path.split("\\?");
        ResponseEntity responseEntity = null;
        Optional<MockEntities> mockEntities = mockRepositories.findById(id);
        if (!mockEntities.isPresent())
            return new ResponseEntity<Object>("not found", HttpStatus.NOT_FOUND);
        MockVmodel mockResponseVmodel = new MockVmodel();
        mockResponseVmodel.setId(mockEntities.get().getId());
        mockResponseVmodel.setSpec(Json.mapper().readValue(mockEntities.get().getSpec(), OpenAPI.class));
        mockResponseVmodel.setDescription(mockEntities.get().getDescription());
        mockResponseVmodel.setTitle(mockEntities.get().getTitle());
        OpenAPI openAPI = (OpenAPI) mockResponseVmodel.getSpec();
        final Object[] response = new Object[1];
        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String s = entry.getKey();
            PathItem pathItem = entry.getValue();
            String openApiPath = s.replace("_", ".");
            String[] openAPIPaths = openApiPath.split("/");
            String[] paths = originalPathUri[0].split("/");
            String[] regexPath = new String[paths.length];
            if (openAPIPaths.length == paths.length) {
                for (int i = 0; i < openAPIPaths.length; i++) {
                    if (!openAPIPaths[i].equals(paths[i])) {
                        if (openAPIPaths[i].contains("*")) {
                            regexPath[i] = openAPIPaths[i];
                        }
                    } else {
                        regexPath[i] = openAPIPaths[i];
                    }
                }
                if (openApiPath.equals(String.join("/", regexPath))) {
                    JsonNode jsonNode = Json.mapper().valueToTree(pathItem);
                    Operation ops = Json.mapper().treeToValue(jsonNode.get(request.getMethod().toLowerCase()), Operation.class);
                    Map<String, List<Map<String, Object>>> examples = (Map<String, List<Map<String, Object>>>) ops.getExtensions().get(MockModel.X_EXAMPLES);
                    for (Map.Entry<String, List<Map<String, Object>>> extension : examples.entrySet()) {
                        if (extension.getKey().equals(MockModel.X_HEADERS)) {
                            for (Map<String, Object> stringObjectMap : extension.getValue()) {
                                MockExample mockExample = Json.mapper().convertValue(stringObjectMap, MockExample.class);
                                if (request.getHeader((String) mockExample.getProperty().get(NAME_PROPERTY)).equals(mockExample.getProperty().get(VALUE_PROPERTY))) {
                                    return new ResponseEntity<>(mockExample.getResponse().getResponse(), HttpStatus.valueOf(mockExample.getResponse().getHttpCode()));
                                }
                            }
                        }
                        if (extension.getKey().equals(MockModel.x_BODY)) {
                            if (request.getMethod().equals(HttpMethod.POST.toString()) || request.getMethod().equals(HttpMethod.PATCH.toString())
                                    || request.getMethod().equals(HttpMethod.PUT.toString()))
                                for (Map<String, Object> stringObjectMap : extension.getValue()) {
                                    JsonNode requestBody = Json.mapper().readTree(body);
                                    MockExample mockExample = Json.mapper().convertValue(stringObjectMap, MockExample.class);
                                    if (requestBody.findPath((String) mockExample.getProperty().get(NAME_PROPERTY)).asText().equals(mockExample.getProperty().get(VALUE_PROPERTY))) {
                                        return new ResponseEntity<>(mockExample.getResponse().getResponse(), HttpStatus.valueOf(mockExample.getResponse().getHttpCode()));
                                    }
                                }
                        }
                        if (extension.getKey().equals(MockModel.x_PATH)) {
                            for (Map<String, Object> stringObjectMap : extension.getValue()) {
                                MockExample mockExample = Json.mapper().convertValue(stringObjectMap, MockExample.class);
                                for (int i = 0; i < openAPIPaths.length; i++) {
                                    if (!openAPIPaths[i].equals(paths[i])) {
                                        if (openAPIPaths[i].contains((CharSequence) mockExample.getProperty().get(NAME_PROPERTY))) {
                                            if (paths[i].equals(mockExample.getProperty().get(VALUE_PROPERTY))) {
                                                return new ResponseEntity<>(mockExample.getResponse().getResponse(), HttpStatus.valueOf(mockExample.getResponse().getHttpCode()));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (extension.getKey().equals(MockModel.x_QUERY)) {
                            for (Map<String, Object> stringObjectMap : extension.getValue()) {
                                MockExample mockExample = Json.mapper().convertValue(stringObjectMap, MockExample.class);
                                String[] queryStrings = request.getQueryString().split("\\?");
                                Map<String, String> q = decodeQueryString(queryStrings[1]);
                                for (Map.Entry<String, String> qmap : q.entrySet()) {
                                    log.info(qmap.getKey());
                                    log.info(qmap.getValue());
                                    if ((qmap.getKey().equals(mockExample.getProperty().get(NAME_PROPERTY))) &&
                                            (qmap.getValue().equals(mockExample.getProperty().get(VALUE_PROPERTY)))) {
                                        return new ResponseEntity<>(mockExample.getResponse().getResponse(),
                                                HttpStatus.valueOf(mockExample.getResponse().getHttpCode()));
                                    }
                                }
                            }
                        }
                    }
                    response[0] = ops.getExtensions();
                }
            } else {
                return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
            }
        }
        if (response == null)
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @GetMapping(value = "/mocks")
    public ResponseEntity<List<MockVmodel>> listingMockRepositories() {
        List<MockVmodel> mockResponseVmodels = new ArrayList<>();
        mockRepositories.findAll().forEach(mockEntities -> {
            MockVmodel mockResponseVmodel = new MockVmodel();
            mockResponseVmodel.setId(mockEntities.getId());
            mockResponseVmodel.setDescription(mockEntities.getDescription());
            mockResponseVmodel.setTitle(mockEntities.getTitle());
            try {
                OpenAPI openAPI = Json.mapper().readValue(mockEntities.getSpec(), OpenAPI.class);
                Paths newPath = new Paths();
                openAPI.getPaths().forEach((s, pathItem) -> {
                    newPath.put(s.replace("_", ".").replace("*{", "{"), pathItem);
                });
                openAPI.setPaths(newPath);
                mockResponseVmodel.setSpec(openAPI);
                mockResponseVmodels.add(mockResponseVmodel);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e.getCause());
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok(mockResponseVmodels);
    }

    @PostMapping(value = "/mocks")
    public ResponseEntity<MockVmodel> storeMocksEntity(@RequestBody MockVmodel body) throws JsonProcessingException {
        SwaggerParseResult result = new OpenAPIParser().readContents(Json.mapper().writeValueAsString(body.getSpec()), null, null);
        OpenAPI openAPI = result.getOpenAPI();
        Paths newPath = new Paths();
        openAPI.getPaths().forEach((s, pathItem) -> {
            newPath.put(s.replace(".", "_").replace("{", "*{"), pathItem);
        });
        openAPI.setPaths(newPath);
        MockEntities mockEntities = new MockEntities();
        mockEntities.setTitle(body.getTitle());
        mockEntities.setDescription(body.getDescription());
        mockEntities.setSpec(Json.mapper().writeValueAsString(openAPI));
        MockEntities mock = mockRepositories.save(mockEntities);
        MockVmodel mockResponseVmodel = new MockVmodel();
        mockResponseVmodel.setId(mock.getId());
        mockResponseVmodel.setSpec(Json.mapper().readValue(mock.getSpec(), OpenAPI.class));
        mockResponseVmodel.setDescription(mockEntities.getDescription());
        mockResponseVmodel.setTitle(mockEntities.getTitle());
        return ResponseEntity.ok(mockResponseVmodel);
    }
}
