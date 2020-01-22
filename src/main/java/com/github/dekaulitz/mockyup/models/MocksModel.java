package com.github.dekaulitz.mockyup.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.entities.MockEntities;
import com.github.dekaulitz.mockyup.errorHandlers.NotFoundException;
import com.github.dekaulitz.mockyup.repositories.MockRepositories;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
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

    public MockExample getMockMocking(HttpServletRequest request, String _id, String[] originalPathUri, String body) throws NotFoundException, JsonProcessingException {
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
                    Map<String, List<Map<String, Object>>> examples = (Map<String, List<Map<String, Object>>>) ops.getExtensions().get(MockExample.X_EXAMPLES);
                    for (Map.Entry<String, List<Map<String, Object>>> extension : examples.entrySet()) {
                       /*
                       if there some example on headers will check every field on request header and mapping with example
                        */
                        if (extension.getKey().equals(MockExample.X_HEADERS)) {
                            mock = MockExample.generateResponseHeader(request, extension.getValue());
                            if (mock != null) {
                                return mock;
                            }
                        }
                        /*
                         if there some example on body will check every field on request body and mapping with example
                         */
                        if (extension.getKey().equals(MockExample.x_BODY)) {
                            mock = MockExample.generateResponseBody(request, extension.getValue(), body);
                            if (mock != null) {
                                return mock;
                            }
                        }
                         /*
                         if there some example on path will check every field on request body and mapping with example
                         */
                        if (extension.getKey().equals(MockExample.x_PATH)) {
                            mock = MockExample.generateResponnsePath(request, extension.getValue(), openAPIPaths, paths);
                            if (mock != null) {
                                return mock;
                            }

                        }
                            /*
                         if there some on query on path will check every field on request body and mapping with example
                         */
                        if (extension.getKey().equals(MockExample.x_QUERY)) {
                            mock = MockExample.generateResponnseQuery(request, extension.getValue());
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

    public MockEntities storeMock(MockEntities mockEntities) {
        return mockRepositories.save(mockEntities);
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
