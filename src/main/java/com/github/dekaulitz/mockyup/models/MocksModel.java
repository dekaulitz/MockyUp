package com.github.dekaulitz.mockyup.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.entities.MockEntities;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.repositories.MockRepositories;
import com.github.dekaulitz.mockyup.vmodels.MockVmodel;
import io.swagger.parser.OpenAPIParser;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MocksModel {
    @Autowired
    private final MockRepositories mockRepositories;

    public MocksModel(MockRepositories mockRepositories) {
        this.mockRepositories = mockRepositories;
    }

    public List<MockEntities> getMocks() {
        return mockRepositories.findAll();
    }

    public MockExample getMockMocking(HttpServletRequest request, String path, String _id, String body) throws NotFoundException, JsonProcessingException, InvalidMockException {
        String[] originalPathUri = path.split("\\?");
        Optional<MockEntities> mockEntities = mockRepositories.findById(_id);
        if (!mockEntities.isPresent())
            throw new NotFoundException("data not found");
        MockExample mock;
        OpenAPI openAPI = Json.mapper().readValue(mockEntities.get().getSpec(), OpenAPI.class);
        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String s = entry.getKey();
            PathItem pathItem = entry.getValue();
            String openApiPath = s.replace("_", ".");
            String[] openAPIPaths = openApiPath.split("/");
            String[] paths = originalPathUri[0].split("/");
            String[] regexPath = new String[paths.length];
            /*
                every targeting mocks uri should has same length
             */
            if (openAPIPaths.length == paths.length) {
                /*
                converting request path to original for searching data
                 */
                for (int i = 0; i < openAPIPaths.length; i++) {
                    if (!openAPIPaths[i].equals(paths[i])) {
                        if (openAPIPaths[i].contains("*")) {
                            regexPath[i] = openAPIPaths[i];
                        }
                    } else {
                        regexPath[i] = openAPIPaths[i];
                    }
                }
                /*
                find every path that equeal with orginal request path
                 */
                if (openApiPath.equals(String.join("/", regexPath))) {
                    JsonNode jsonNode = Json.mapper().valueToTree(pathItem);
                    Operation ops = Json.mapper().treeToValue(jsonNode.get(request.getMethod().toLowerCase()), Operation.class);
                    if (ops.getExtensions() == null) throw new NotFoundException("no extension found");
                    Map<String, Object> examples = (Map<String, Object>) ops.getExtensions().get(MockExample.X_EXAMPLES);
                    if (examples == null) throw new NotFoundException("no mock example found");
                    for (Map.Entry<String, Object> extension : examples.entrySet()) {
                       /*
                       if there some example on headers will check every field on request header and mapping with example
                        */
                        if (extension.getKey().equals(MockExample.X_HEADERS)) {
                            mock = MockExample.generateResponseHeader(request, (List<Map<String, Object>>) extension.getValue());
                            if (mock != null) {
                                return mock;
                            }
                        }
                        /*
                         if there some example on body will check every field on request body and mapping with example
                         */
                        if (extension.getKey().equals(MockExample.x_BODY)) {
                            mock = MockExample.generateResponseBody(request, (List<Map<String, Object>>) extension.getValue(), body);
                            if (mock != null) {
                                return mock;
                            }
                        }
                         /*
                         if there some example on path will check every field on request body and mapping with example
                         */
                        if (extension.getKey().equals(MockExample.x_PATH)) {
                            mock = MockExample.generateResponnsePath(request, (List<Map<String, Object>>) extension.getValue(), openAPIPaths, paths);
                            if (mock != null) {
                                return mock;
                            }

                        }
                            /*
                         if there some on query on path will check every field on request body and mapping with example
                         */
                        if (extension.getKey().equals(MockExample.x_QUERY)) {
                            mock = MockExample.generateResponnseQuery(request, (List<Map<String, Object>>) extension.getValue());
                            if (mock != null) {
                                return mock;
                            }
                        }
                        if (extension.getKey().equals(MockExample.x_DEFAULT)) {
                            mock = MockExample.generateResponseDefault(extension.getValue());
                            if (mock != null) {
                                return mock;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public MockEntities storeMock(MockVmodel body) throws InvalidMockException {
        SwaggerParseResult result = null;
        try {
            MockEntities mockEntities = new MockEntities();
            mockEntities.setSwagger(Json.mapper().writeValueAsString(body.getSpec()));
            mockEntities.setTitle(body.getTitle());
            mockEntities.setDescription(body.getDescription());
            result = new OpenAPIParser().readContents(Json.mapper().writeValueAsString(body.getSpec()), null, null);
            OpenAPI openAPI = result.getOpenAPI();
            Paths newPath = new Paths();
            openAPI.getPaths().forEach((s, pathItem) -> {
                newPath.put(s.replace(".", "_").replace("{", "*{"), pathItem);
            });
            openAPI.setPaths(newPath);
            mockEntities.setSpec(Json.mapper().writeValueAsString(openAPI));
            return mockRepositories.save(mockEntities);
        } catch (JsonProcessingException e) {
            throw new InvalidMockException("invalid mock exception " + e.getMessage());
        }
    }

    public void updateMock(String _id, MockEntities mockEntities) {

    }

    public MockEntities getMockById(String _id) throws NotFoundException {
        Optional<MockEntities> mockEntities = mockRepositories.findById(_id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException("mocks not found");
        }
        return mockEntities.get();
    }

    public void deleteMock(String _id) throws NotFoundException {
        Optional<MockEntities> mockEntities = mockRepositories.findById(_id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException("mocks not found");
        }
        mockRepositories.deleteById(mockEntities.get().getId());
    }


}
