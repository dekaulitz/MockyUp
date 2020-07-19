package com.github.dekaulitz.mockyup.domain.mocks.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.infrastructure.db.entities.UserMocksEntities;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.InvalidMockException;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.utils.Role;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseMockModel {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public void setSaveMockEntity(MockVmodel body, MockEntities mockEntities, AuthenticationProfileModel authenticationProfileModel) throws InvalidMockException {
        try {
            mockEntities.setSwagger(JsonMapper.mapper().writeValueAsString(body.getSpec()));
            mockEntities.setTitle(body.getTitle());
            mockEntities.setDescription(body.getDescription());
            parsingSpecToOpenApi(body, mockEntities);
            List<UserMocksEntities> users = new ArrayList<>();
            UserMocksEntities creator = new UserMocksEntities();
            creator.setUserId(authenticationProfileModel.get_id());
            creator.setAccess(Role.MOCKS_READ_WRITE.name());
            users.add(creator);
            if (body.getUsers() != null) {
                body.getUsers().forEach(userMocks -> {
                    UserMocksEntities user = new UserMocksEntities();
                    user.setAccess(userMocks.getAccess());
                    user.setUserId(userMocks.getUserId());
                    users.add(user);
                });
            }
            mockEntities.setUsers(users);
        } catch (JsonProcessingException e) {
            throw new InvalidMockException(ResponseCode.INVALID_MOCKUP_STRUCTURE, e);
        }

    }

    private void parsingSpecToOpenApi(MockVmodel body, MockEntities mockEntities) throws JsonProcessingException {
        SwaggerParseResult result;
        result = new OpenAPIParser().readContents(JsonMapper.mapper().writeValueAsString(body.getSpec()), null, null);
        OpenAPI openAPI = result.getOpenAPI();
        Paths newPath = new Paths();
        openAPI.getPaths().forEach((s, pathItem) -> {
            newPath.put(s.replace("{", "*{"), pathItem);
        });
        openAPI.setPaths(newPath);
        mockEntities.setSpec(JsonMapper.mapper().writeValueAsString(openAPI));
    }

    public void setUpdateMockEntity(MockVmodel body, MockEntities mockEntities) throws InvalidMockException {
        try {
            mockEntities.setSwagger(JsonMapper.mapper().writeValueAsString(body.getSpec()));
            parsingSpecToOpenApi(body, mockEntities);
        } catch (JsonProcessingException e) {
            throw new InvalidMockException(ResponseCode.INVALID_MOCKUP_STRUCTURE, e);
        }

    }

    public MockHelper getMockResponse(PathItem pathItem, HttpServletRequest request, String body, String[] openAPIPaths, String[] paths, Components components)
            throws NotFoundException, JsonProcessingException, InvalidMockException, UnsupportedEncodingException {
        MockHelper mock = null;
        JsonNode jsonNode = JsonMapper.mapper().valueToTree(pathItem);
        Operation ops = JsonMapper.mapper().treeToValue(jsonNode.get(request.getMethod().toLowerCase()), Operation.class);
        if (ops.getExtensions() == null) throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        Map<String, Object> examples = (Map<String, Object>) ops.getExtensions().get(MockHelper.X_EXAMPLES);
        if (examples == null) throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        for (Map.Entry<String, Object> extension : examples.entrySet()) {
            switch (extension.getKey()) {
                case MockHelper.X_PATH:
                    mock = MockHelper.generateResponsePath((List<Map<String, Object>>) extension.getValue(), openAPIPaths, paths, components);
                    if (mock != null) return mock;
                    break;
                case MockHelper.X_HEADERS:
                    mock = MockHelper.generateResponseHeader(request, (List<Map<String, Object>>) extension.getValue(), components);
                    if (mock != null) return mock;
                    break;
                case MockHelper.X_QUERY:
                    mock = MockHelper.generateResponseQuery(request, (List<Map<String, Object>>) extension.getValue(), components);
                    if (mock != null) return mock;
                    break;
                case MockHelper.X_BODY:
                    mock = MockHelper.generateResponseBody(request, (List<Map<String, Object>>) extension.getValue(), body, components);
                    if (mock != null) return mock;
                    break;
                case MockHelper.X_DEFAULT:
                    mock = MockHelper.generateResponseDefault((Map<String, Object>) extension.getValue(), components);
                    if (mock != null) return mock;
                    break;
                default:
                    mock = null;
            }
        }
        return mock;
    }

    protected Criteria getSearchParameter(String q) {
        Criteria criteria = null;
        if (q != null) {
            String[] search = q.split(":");
            if (search.length == 2 && !search[1].isEmpty()) {
                if (search[0].equals("_id") || search[0].equals("id")) {
                    criteria = Criteria.where(search[0]).regex(".*" + search[1] + ".*", "i");
                } else
                    criteria = Criteria.where(search[0]).regex(".*" + search[1] + ".*", "i");
            }
        }
        return criteria;
    }


}
