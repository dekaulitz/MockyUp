package com.github.dekaulitz.mockyup.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.entities.MockEntities;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.models.helper.MockExample;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.vmodels.MockVmodel;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

abstract class BaseModel<M, M1> implements Model<M, M1> {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public void setMockEntity(MockVmodel body, MockEntities mockEntities) throws JsonProcessingException {
        SwaggerParseResult result = null;
        mockEntities.setSwagger(JsonMapper.mapper().writeValueAsString(body.getSpec()));
        mockEntities.setTitle(body.getTitle());
        mockEntities.setDescription(body.getDescription());
        result = new OpenAPIParser().readContents(JsonMapper.mapper().writeValueAsString(body.getSpec()), null, null);
        OpenAPI openAPI = result.getOpenAPI();
        Paths newPath = new Paths();
        openAPI.getPaths().forEach((s, pathItem) -> {
            newPath.put(s.replace(".", "_").replace("{", "*{"), pathItem);
        });
        openAPI.setPaths(newPath);
        mockEntities.setSpec(JsonMapper.mapper().writeValueAsString(openAPI));
    }

    public MockExample getMockResponse(PathItem pathItem, HttpServletRequest request, String body, String[] openAPIPaths, String[] paths)
            throws NotFoundException, JsonProcessingException, InvalidMockException, UnsupportedEncodingException {
        MockExample mock = null;
        JsonNode jsonNode = JsonMapper.mapper().valueToTree(pathItem);
        Operation ops = JsonMapper.mapper().treeToValue(jsonNode.get(request.getMethod().toLowerCase()), Operation.class);
        if (ops.getExtensions() == null) throw new NotFoundException("no extension found");
        Map<String, Object> examples = (Map<String, Object>) ops.getExtensions().get(MockExample.X_EXAMPLES);
        if (examples == null) throw new NotFoundException("no mock example found");
        for (Map.Entry<String, Object> extension : examples.entrySet()) {
            if (extension.getKey().equals(MockExample.X_HEADERS)) {
                mock = MockExample.generateResponseHeader(request, (List<Map<String, Object>>) extension.getValue());
                if (mock != null) {
                    return mock;
                }
            }
            if (extension.getKey().equals(MockExample.X_BODY)) {
                mock = MockExample.generateResponseBody(request, (List<Map<String, Object>>) extension.getValue(), body);
                if (mock != null) {
                    return mock;
                }
            }
            if (extension.getKey().equals(MockExample.X_PATH)) {
                mock = MockExample.generateResponnsePath(request, (List<Map<String, Object>>) extension.getValue(), openAPIPaths, paths);
                if (mock != null) {
                    return mock;
                }
            }
            if (extension.getKey().equals(MockExample.X_QUERY)) {
                mock = MockExample.generateResponnseQuery(request, (List<Map<String, Object>>) extension.getValue());
                if (mock != null) {
                    return mock;
                }
            }
            if (extension.getKey().equals(MockExample.X_DEFAULT)) {
                mock = MockExample.generateResponseDefault(extension.getValue());
                if (mock != null) {
                    return mock;
                }
            }
        }
        return mock;
    }

}
