package com.github.dekaulitz.mockyup.server.service.mockup.helper.mockup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingAttributeEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingRequestEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingResponseContentEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingResponseEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.schemas.BaseSchema;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi.OpenApiSchemaHelper;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import io.swagger.v3.oas.models.media.Schema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;


@Slf4j
public class MockupHelper {

  public static LinkedList<MockingMatchingRequestEmbedded> getMatchingAttributes(JsonNode mockupNode,String mockingType) {
    if (!mockupNode.has(mockingType)) {
      log.debug("getRequestQueries {} not found on node :{}",mockingType, mockupNode);
      return null;
    }
    ArrayNode mockingQueryRequestNode = (ArrayNode) mockupNode.get(mockingType);
    if (null == mockingQueryRequestNode || mockingQueryRequestNode.isEmpty()) {
      return null;
    }
    LinkedList<MockingMatchingRequestEmbedded> mockingMatchingAttributeEmbeddedList = new LinkedList<>();
    mockingQueryRequestNode.forEach(jsonNode -> {
      if (!jsonNode.has("matchingProperty")) {
        return;
      }
      MockingMatchingRequestEmbedded mockingMatchingAttributeEmbedded = new MockingMatchingRequestEmbedded();
      mockingMatchingAttributeEmbedded
          .setMatchingProperty(initMatchingProperty(jsonNode.get("matchingProperty")));
      if (jsonNode.has("response")) {
        mockingMatchingAttributeEmbedded.setResponse(initResponse(jsonNode.get("response")));
      }
      mockingMatchingAttributeEmbeddedList.add(mockingMatchingAttributeEmbedded);
    });
    return mockingMatchingAttributeEmbeddedList;
  }

  private static MockingMatchingAttributeEmbedded initMatchingProperty(JsonNode matchingProperty) {
    MockingMatchingAttributeEmbedded mockingMatchingAttributeEmbedded = new MockingMatchingAttributeEmbedded();
    mockingMatchingAttributeEmbedded
        .setDescription(matchingProperty.get("description").textValue());

    /**
     * @TODO need enhance the logic
     * we should convert to baseSchema but we also convert but we should get what instance the type
     */
    if (matchingProperty.has("properties")) {
      Map<String, BaseSchema> properties = JsonMapper.mapper()
          .convertValue(matchingProperty.get("properties"),
              new TypeReference<Map<String, BaseSchema>>() {
              });
      Map<String, BaseSchema> schemaMap = new HashMap<>();
      properties.forEach((s, objectNode) -> {
        BaseSchema baseSchema = OpenApiSchemaHelper.createSchema(objectNode.getType(),objectNode.getFormat());
        BeanUtils.copyProperties(objectNode,baseSchema);
        schemaMap.put(s,baseSchema);
      });
      mockingMatchingAttributeEmbedded.setProperties(schemaMap);
    }

    return mockingMatchingAttributeEmbedded;
  }

  public static MockingMatchingResponseEmbedded getResponseDefault(JsonNode mockupNode) {

    if (!mockupNode.has("mockingDefaultResponse")) {
      log.debug("getResponseDefault mockingDefaultRequest not found on node :{}", mockupNode);
      return null;
    }
    JsonNode mockingDefaultRequestNode = mockupNode.get("mockingDefaultResponse");
    return initResponse(mockingDefaultRequestNode);
  }

  private static MockingMatchingResponseEmbedded initResponse(JsonNode mockingDefaultRequestNode) {

    Map<String, Object> mockingDefaultRequestMap = JsonMapper.mapper()
        .convertValue(mockingDefaultRequestNode,
            new TypeReference<Map<String, Object>>() {
            });
    MockingMatchingResponseEmbedded mockingMatchingResponseEmbedded = new MockingMatchingResponseEmbedded();
    mockingMatchingResponseEmbedded
        .setStatusCode((Integer) mockingDefaultRequestMap.get("statusCode"));
    if (mockingDefaultRequestMap.containsKey("headers")) {
      Map<String, Object> headers = JsonMapper.mapper()
          .convertValue(mockingDefaultRequestNode.get("headers"),
              new TypeReference<Map<String, Object>>() {
              });
      mockingMatchingResponseEmbedded.setHeaders(headers);
    }
    if (mockingDefaultRequestMap.containsKey("content")) {
      Map<String, MockingMatchingResponseContentEmbedded> contentMap = JsonMapper.mapper()
          .convertValue(mockingDefaultRequestMap.get("content"),
              new TypeReference<Map<String, MockingMatchingResponseContentEmbedded>>() {
              });
      contentMap.forEach((s, mockingMatchingResponseContentEmbedded) -> {
        OpenApiContentType contentType = OpenApiContentType.get(s);
        if (contentType != null) {
          Map<OpenApiContentType, MockingMatchingResponseContentEmbedded> mockupContent = new HashMap<>();
          mockupContent.put(contentType, mockingMatchingResponseContentEmbedded);
          mockingMatchingResponseEmbedded.setContent(mockupContent);
        }
      });
    }
    return mockingMatchingResponseEmbedded;
  }
}
