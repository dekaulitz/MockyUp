package com.github.dekaulitz.mockyup.server.tmp.domain.mocks.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.server.configuration.jwt.JwtProfileModel;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.MockEntities;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.UserMocksEntities;
import com.github.dekaulitz.mockyup.server.tmp.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.server.tmp.errors.handlers.InvalidMockException;
import com.github.dekaulitz.mockyup.server.tmp.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.server.model.constants.Role;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import com.github.dekaulitz.mockyup.server.utils.MockHelper;
import com.github.dekaulitz.mockyup.server.utils.ResponseCode;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseMockModel {

  protected Logger log = LoggerFactory.getLogger(this.getClass());

  public void setSaveMockEntity(MockVmodel body, MockEntities mockEntities,
      JwtProfileModel jwtProfileModel) throws InvalidMockException {
    try {
      mockEntities.setSwagger(JsonMapper.mapper().writeValueAsString(body.getSpec()));
      mockEntities.setTitle(body.getTitle());
      mockEntities.setDescription(body.getDescription());
      parsingSpecToOpenApi(body, mockEntities);
      List<UserMocksEntities> users = new ArrayList<>();
      UserMocksEntities creator = new UserMocksEntities();
      creator.setUserId(jwtProfileModel.getId());
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

  private void parsingSpecToOpenApi(MockVmodel body, MockEntities mockEntities)
      throws JsonProcessingException {
    SwaggerParseResult result;
    result = new OpenAPIParser()
        .readContents(JsonMapper.mapper().writeValueAsString(body.getSpec()), null, null);
    OpenAPI openAPI = result.getOpenAPI();
    Paths newPath = new Paths();
    openAPI.getPaths().forEach((s, pathItem) -> {
      newPath.put(s.replace("{", "*{"), pathItem);
    });
    openAPI.setPaths(newPath);
    mockEntities.setSpec(JsonMapper.mapper().writeValueAsString(openAPI));
  }

  public void setUpdateMockEntity(MockVmodel body, MockEntities mockEntities)
      throws InvalidMockException {
    try {
      mockEntities.setSwagger(JsonMapper.mapper().writeValueAsString(body.getSpec()));
      parsingSpecToOpenApi(body, mockEntities);
    } catch (JsonProcessingException e) {
      throw new InvalidMockException(ResponseCode.INVALID_MOCKUP_STRUCTURE, e);
    }

  }

  /**
   * @param pathItem         {@link PathItem}
   * @param request          {@link HttpServletRequest}
   * @param body             {@link String}
   * @param openApiRoutePath {@link String[]}[swagger_path_contract] that mapped with
   *                         [contract_path_that_want_to_mock]
   * @param paths            {@link String[]} [contract_path_that_want_to_mock]
   * @param components       {@link Components} components from open api
   * @return MockHelper {@link MockHelper} the response from mock response
   * @throws NotFoundException            if there no mock response detected
   * @throws JsonProcessingException      if parsing body was fail
   * @throws InvalidMockException         if mock response is invalid
   * @throws UnsupportedEncodingException if encoding query string is fail
   */
  public MockHelper renderingMockResponse(PathItem pathItem, HttpServletRequest request,
      String body, String[] openApiRoutePath, String[] paths, Components components)
      throws NotFoundException, IOException, InvalidMockException {
    MockHelper mock;

    //parsing pathitem into JsonNode
    //its because from the pathItem you its more easier for geting type of method request that wanted
    JsonNode jsonNode = JsonMapper.mapper().valueToTree(pathItem);

    //find from node that matched with request method and convert into Operation
    Operation ops = JsonMapper.mapper()
        .treeToValue(jsonNode.get(request.getMethod().toLowerCase()), Operation.class);

    //if there is no request method that matched with operation method bas on the [swagger_path_contract]
    if (ops.getExtensions() == null) {
      throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
    }

    //get custom extension for mock response from operation with extension [x-examples]
    @SuppressWarnings("unchecked")
    Map<String, Object> examples = (Map<String, Object>) ops.getExtensions()
        .get(MockHelper.X_EXAMPLES);
    if (examples == null) {
      throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
    }

    //iterate [x-examples] that available from operation extensions
    for (Map.Entry<String, Object> extension : examples.entrySet()) {
      /*
       *  for the mock it will responding by the order
       *  1. path
       *  2. query
       *  3. headers
       *  4. body
       *  5. default
       *  if the mock found with the expected criteria
       *  will stop the other search and returning the response
       */

      //get mock response from path
      if (extension.getKey().equals(MockHelper.X_PATH)) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> extensionValues = (List<Map<String, Object>>) extension
            .getValue();
        mock = MockHelper
            .generateResponsePath(request, extensionValues, openApiRoutePath, paths, components);
        if (mock != null) {
          return mock;
        }
      }

      //get mock from query string
      if (extension.getKey().equals(MockHelper.X_QUERY)) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> extensionValues = (List<Map<String, Object>>) extension
            .getValue();
        mock = MockHelper.generateResponseQuery(request, extensionValues, components);
        if (mock != null) {
          return mock;
        }
      }

      //checking the mock from the headers
      if (extension.getKey().equals(MockHelper.X_HEADERS)) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> extensionValues = (List<Map<String, Object>>) extension
            .getValue();
        mock = MockHelper.generateResponseHeader(request, extensionValues, components);
        if (mock != null) {
          return mock;
        }
      }

      //checking the mock from the boyd
      if (extension.getKey().equals(MockHelper.X_BODY)) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> extensionValues = (List<Map<String, Object>>) extension
            .getValue();
        mock = MockHelper.generateResponseBody(request, extensionValues, body, components);
        if (mock != null) {
          return mock;
        }
      }

      //if there is no mock defined on path,header,query and body but default was defined
      if (extension.getKey().equals(MockHelper.X_DEFAULT)) {
        @SuppressWarnings("unchecked")
        Map<String, Object> extensionValues = (Map<String, Object>) extension.getValue();
        mock = MockHelper.generateResponseDefault(request, extensionValues, components);
        return mock;
      }
    }
    return null;
  }
}
