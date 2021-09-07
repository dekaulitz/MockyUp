package com.github.dekaulitz.mockyup.server.service.mockup.helper.mockup;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingRequestEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.schemas.BaseSchema;
import com.github.dekaulitz.mockyup.server.db.entities.v2.features.mockup.MockUpRequestEmbedded;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestModel;
import com.github.dekaulitz.mockyup.server.service.common.helper.MessageHelper;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.MessageType;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi.OpenApiSchemaHelper;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import com.github.dekaulitz.mockyup.server.utils.XmlMapper;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.collections4.MapUtils;

public class MockRequestHelper {

  private MockRequestHelper() {
  }

  public static void initMockRequest(List<OpenApiPathEmbedded> pathInfos,
      Map<String, String> headers,
      Map<String, String[]> parameters, String path, String body,
      MockRequestModel mockRequestModel, String id,
      OpenApiContentType openApiContentType) throws ServiceException {
    for (OpenApiPathEmbedded pathInfo : pathInfos) {
      if (null == pathInfo.getOperation() || null == pathInfo.getOperation().getMockup()) {
        throw new ServiceException(MessageHelper.getMessage(MessageType.MOCK_NOT_FOUND),
            "contractId: " + id + " with path" + path);
      }
      MockUpRequestEmbedded mockingPath = pathInfo
          .getOperation().getMockup();
      if (null != mockingPath.getMockingRequestPaths()) {
        initMockingRequestPaths(mockingPath.getMockingRequestPaths(),
            mockRequestModel, path);
        if (mockRequestModel.getResponse() != null) {
          break;
        }
      }

      if (null != mockingPath.getMockingRequestHeaders()) {
        initMockingRequestHeaders(mockingPath.getMockingRequestHeaders(),
            mockRequestModel, headers);
        if (mockRequestModel.getResponse() != null) {
          break;
        }
      }
      if (null != mockingPath.getMockingRequestQueries()) {
        initMockingRequestQueries(mockingPath.getMockingRequestQueries(),
            mockRequestModel, parameters);
        if (mockRequestModel.getResponse() != null) {
          break;
        }
      }
      if (null != mockingPath.getMockingRequestBodies()) {
        MockRequestHelper
            .getMockingBodyRequest(mockingPath.getMockingRequestBodies(), openApiContentType, body,
                mockRequestModel);
        if (mockRequestModel.getResponse() != null) {
          break;
        }
      }
      if (null != mockingPath.getResponseDefault()) {
        mockRequestModel.setResponse(mockingPath.getResponseDefault());
        if (mockRequestModel.getResponse() != null) {
          break;
        }
      }
    }
  }

  private static void getMockingBodyRequest(
      List<MockingMatchingRequestEmbedded> mockingRequestBodies,
      OpenApiContentType openApiContentType, String body, MockRequestModel mockRequestModel)
      throws ServiceException {
    JsonNode requestBody;
    try {
      if (OpenApiContentType.APPLICATION_XML == openApiContentType) {
        requestBody = XmlMapper.mapper().readTree(body.getBytes());
      } else {
        requestBody = JsonMapper.mapper().readTree(body);
      }
    } catch (Exception ex) {
      throw new ServiceException(MessageHelper.getMessage(MessageType.UNSUPPORTED_MOCK_TYPE),
          ex);
    }
    mockingRequestBodies.forEach(mockingMatchingRequestEmbedded -> {
      if (null == mockingMatchingRequestEmbedded.getMatchingProperty() || MapUtils
          .isEmpty(mockingMatchingRequestEmbedded.getMatchingProperty().getProperties())) {
        return;
      }
      AtomicInteger matchingSize = new AtomicInteger(
          mockingMatchingRequestEmbedded.getMatchingProperty()
              .getProperties().size());
      mockingMatchingRequestEmbedded.getMatchingProperty().getProperties()
          .forEach((s, baseSchema) -> {
            if (requestBody.has(s)) {
              checkMockingRequestBody(requestBody, s, baseSchema, matchingSize);
            }
          });
      if (matchingSize.get() == 0) {
        mockRequestModel.setResponse(mockingMatchingRequestEmbedded.getResponse());
      }
    });
  }

  private static void checkMockingRequestBody(JsonNode requestBody, String s,
      BaseSchema baseSchema,
      AtomicInteger matchingSize) {
    switch (baseSchema.getType()) {
      case OpenApiSchemaHelper.STRING_TYPE: {
        String expectedValue = (String) baseSchema.getValue();
        if (expectedValue.equals(requestBody.get(s).textValue())) {
          matchingSize.getAndDecrement();
        }
        break;
      }
      case OpenApiSchemaHelper.INTEGER_TYPE: {
        int expectedValue = Integer.parseInt((String) baseSchema.getValue());
        if (expectedValue == requestBody.get(s).intValue()) {
          matchingSize.getAndDecrement();
        }
        break;
      }
      case OpenApiSchemaHelper.BOOLEAN_TYPE: {
        boolean expectedValue = Boolean.parseBoolean((String) baseSchema.getValue());
        if (expectedValue == requestBody.get(s).booleanValue()) {
          matchingSize.getAndDecrement();
        }
        break;
      }
      case OpenApiSchemaHelper.NUMBER_TYPE:
        checkMockingNumberFormat(baseSchema, requestBody, s, matchingSize);
        break;
    }
  }

  private static void checkMockingNumberFormat(BaseSchema baseSchema, JsonNode requestBody,
      String s,
      AtomicInteger matchingSize) {
    if (null == baseSchema.getFormat()) {
      int expectedValue = Integer.parseInt((String) baseSchema.getValue());
      if (expectedValue == requestBody.get(s).intValue()) {
        matchingSize.getAndDecrement();
      }
    } else {
      if (baseSchema.getFormat().equals(OpenApiSchemaHelper.FLOAT_FORMAT)) {
        float expectedValue = Float.parseFloat((String) baseSchema.getValue());
        if (expectedValue == requestBody.get(s).floatValue()) {
          matchingSize.getAndDecrement();
        }
      }
      if (baseSchema.getFormat().equals(OpenApiSchemaHelper.DOUBLE_FORMAT)) {
        double expectedValue = Double.parseDouble((String) baseSchema.getValue());
        if (expectedValue == requestBody.get(s).doubleValue()) {
          matchingSize.getAndDecrement();
        }
      }
    }
  }

  private static void initMockingRequestPaths(
      List<MockingMatchingRequestEmbedded> mockingRequestPaths,
      MockRequestModel mockRequestModel, String path) {
    mockingRequestPaths
        .forEach(mockingMatchingRequestEmbedded -> {
          if (null == mockingMatchingRequestEmbedded.getMatchingProperty() || MapUtils
              .isEmpty(mockingMatchingRequestEmbedded.getMatchingProperty().getProperties())) {
            return;
          }
          mockingMatchingRequestEmbedded.getMatchingProperty().getProperties()
              .forEach((s, baseSchema) -> mockRequestModel
                  .setPath(mockRequestModel.getPath().replace("\\{" + s + "\\}",
                      (CharSequence) baseSchema.getValue())));
          if (mockRequestModel.getPath().equalsIgnoreCase(path)) {
            mockRequestModel.setResponse(mockingMatchingRequestEmbedded.getResponse());
          }
        });
  }

  private static void initMockingRequestHeaders(
      List<MockingMatchingRequestEmbedded> mockingRequestHeaders,
      MockRequestModel mockRequestModel, Map<String, String> headers) {
    mockingRequestHeaders
        .forEach(mockingMatchingRequestEmbedded -> {
          if (null == mockingMatchingRequestEmbedded.getMatchingProperty() || MapUtils
              .isEmpty(mockingMatchingRequestEmbedded.getMatchingProperty().getProperties())) {
            return;
          }
          AtomicInteger matchingSize = new AtomicInteger(
              mockingMatchingRequestEmbedded.getMatchingProperty()
                  .getProperties().size());
          mockingMatchingRequestEmbedded.getMatchingProperty().getProperties()
              .forEach((s, baseSchema) -> {
                if (headers.get(s).equalsIgnoreCase((String) baseSchema.getValue())) {
                  matchingSize.getAndDecrement();
                }
              });
          if (matchingSize.get() == 0) {
            mockRequestModel.setResponse(mockingMatchingRequestEmbedded.getResponse());
          }
        });
  }

  private static void initMockingRequestQueries(
      List<MockingMatchingRequestEmbedded> mockingRequestQueries,
      MockRequestModel mockRequestModel,
      Map<String, String[]> parameters) {
    mockingRequestQueries
        .forEach(mockingMatchingRequestEmbedded -> {
          if (null == mockingMatchingRequestEmbedded.getMatchingProperty() || MapUtils
              .isEmpty(mockingMatchingRequestEmbedded.getMatchingProperty().getProperties())) {
            return;
          }
          AtomicInteger matchingSize = new AtomicInteger(
              mockingMatchingRequestEmbedded.getMatchingProperty()
                  .getProperties().size());
          mockingMatchingRequestEmbedded.getMatchingProperty().getProperties()
              .forEach((s, baseSchema) -> {
                if (parameters.containsKey(s)) {
                  String parameterValues = String.join(",", parameters.get(s));
                  if (parameterValues.equalsIgnoreCase((String) baseSchema.getValue())) {
                    matchingSize.getAndDecrement();
                  }
                }
              });
          if (matchingSize.get() == 0) {
            mockRequestModel.setResponse(mockingMatchingRequestEmbedded.getResponse());
          }
        });

  }
}
