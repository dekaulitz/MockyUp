package com.github.dekaulitz.mockyup.server.service.mockup.helper.mockup;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.mockup.MockingMatchingRequestEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.BaseSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.features.mockup.MockUpRequestEmbedded;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestModel;
import com.github.dekaulitz.mockyup.server.service.common.helper.MessageHelper;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi.OpenApiSchemaHelper;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import com.github.dekaulitz.mockyup.server.utils.XmlMapper;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

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
        throw new ServiceException(MessageHelper.getMessage(ResponseCode.MOCK_NOT_FOUND),
            " contractId: " + id + " with path: " + path + " method: ");
      }
      MockUpRequestEmbedded mockingPath = pathInfo
          .getOperation().getMockup();
      if (null != mockingPath.getMockingRequestPaths()) {
        initMockingRequestPaths(mockingPath.getMockingRequestPaths(),
            mockRequestModel, pathInfo.getPath());
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
      if (null != mockingPath.getMockingDefaultResponse()) {
        mockRequestModel.setResponse(mockingPath.getMockingDefaultResponse());
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
    if (StringUtils.isEmpty(body)) {
      return;
    }
    JsonNode requestBody;
    try {
      if (OpenApiContentType.APPLICATION_XML == openApiContentType) {
        requestBody = XmlMapper.mapper().readTree(body.getBytes());
      } else {
        requestBody = JsonMapper.mapper().readTree(body);
      }
    } catch (Exception ex) {
      throw new ServiceException(MessageHelper.getMessage(ResponseCode.UNSUPPORTED_MOCK_TYPE),
          ex);
    }
    for (MockingMatchingRequestEmbedded mockingMatchingRequestEmbedded : mockingRequestBodies) {
      if (null == mockingMatchingRequestEmbedded.getMatchingProperty() || MapUtils
          .isEmpty(mockingMatchingRequestEmbedded.getMatchingProperty().getProperties())) {
        continue;
      }
      AtomicInteger matchingSize = new AtomicInteger(
          mockingMatchingRequestEmbedded.getMatchingProperty()
              .getProperties().size());
      for (Entry<String, BaseSchema> entry : mockingMatchingRequestEmbedded.getMatchingProperty()
          .getProperties().entrySet()) {
        String s = entry.getKey();
        BaseSchema baseSchema = entry.getValue();
        checkMockingRequestBody(requestBody, s, baseSchema, matchingSize);
        if (matchingSize.get() == 0) {
          mockRequestModel.setResponse(mockingMatchingRequestEmbedded.getResponse());
          return;
        }
      }
      if (mockRequestModel.getResponse() != null) {
        return;
      }
    }
  }

  private static void checkMockingRequestBody(JsonNode requestBody, String s,
      BaseSchema baseSchema,
      AtomicInteger matchingSize) {
    if (!requestBody.has(s)) {
      return;
    }
    switch (baseSchema.getType()) {
      case OpenApiSchemaHelper.STRING_TYPE: {
        // this is for handling when the properties value set to null
        // and request from the apps is null also
        String expectedValue = (String) baseSchema.getValue();
        // comparing null to null
        if (expectedValue == null) {
          if (null == requestBody.get(s).textValue()) {
            matchingSize.getAndDecrement();
          }
        } else if (expectedValue.equals(requestBody.get(s).textValue())) {
          matchingSize.getAndDecrement();
        }
        break;
      }
      case OpenApiSchemaHelper.INTEGER_TYPE: {
        Integer expectedValue = Integer.parseInt((String) baseSchema.getValue());
        if (expectedValue == requestBody.get(s).intValue()) {
          matchingSize.getAndDecrement();
        }
        break;
      }
      case OpenApiSchemaHelper.BOOLEAN_TYPE: {
        Boolean expectedValue = Boolean.parseBoolean((String) baseSchema.getValue());
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
      Integer expectedValue = Integer.parseInt((String) baseSchema.getValue());
      if (expectedValue == requestBody.get(s).intValue()) {
        matchingSize.getAndDecrement();
      }
    } else {
      if (baseSchema.getFormat().equals(OpenApiSchemaHelper.FLOAT_FORMAT)) {
        Float expectedValue = Float.parseFloat((String) baseSchema.getValue());
        if (expectedValue == requestBody.get(s).floatValue()) {
          matchingSize.getAndDecrement();
        }
      } else if (baseSchema.getFormat().equals(OpenApiSchemaHelper.DOUBLE_FORMAT)) {
        Double expectedValue = Double.parseDouble((String) baseSchema.getValue());
        if (expectedValue == requestBody.get(s).doubleValue()) {
          matchingSize.getAndDecrement();
        }
      }
    }
  }

  private static void initMockingRequestPaths(
      List<MockingMatchingRequestEmbedded> mockingRequestPaths,
      MockRequestModel mockRequestModel, String openApiPath) {
    for (MockingMatchingRequestEmbedded mockingMatchingRequestEmbedded : mockingRequestPaths) {
      if (null == mockingMatchingRequestEmbedded.getMatchingProperty() || MapUtils
          .isEmpty(mockingMatchingRequestEmbedded.getMatchingProperty().getProperties())) {
        break;
      }
      String pathNeedToReplace = openApiPath;
      for (Entry<String, BaseSchema> entry : mockingMatchingRequestEmbedded.getMatchingProperty()
          .getProperties().entrySet()) {
        String key = entry.getKey();
        BaseSchema value = entry.getValue();
        pathNeedToReplace = pathNeedToReplace.replaceAll("\\{" + key + "\\}",
            (String) value.getValue());
        if (pathNeedToReplace.equals(mockRequestModel.getPath())) {
          mockRequestModel.setResponse(mockingMatchingRequestEmbedded.getResponse());
        }
        if (mockRequestModel.getResponse() != null) {
          break;
        }
      }
    }
  }

  private static void initMockingRequestHeaders(
      List<MockingMatchingRequestEmbedded> mockingRequestHeaders,
      MockRequestModel mockRequestModel, Map<String, String> headers) {
    if (MapUtils.isEmpty(headers)) {
      return;
    }
    for (MockingMatchingRequestEmbedded mockingMatchingRequestEmbedded : mockingRequestHeaders) {
      if (null == mockingMatchingRequestEmbedded.getMatchingProperty() || MapUtils
          .isEmpty(mockingMatchingRequestEmbedded.getMatchingProperty().getProperties())) {
        break;
      }
      AtomicInteger matchingSize = new AtomicInteger(
          mockingMatchingRequestEmbedded.getMatchingProperty()
              .getProperties().size());
      for (Entry<String, BaseSchema> entry : mockingMatchingRequestEmbedded
          .getMatchingProperty().getProperties().entrySet()) {
        String key = entry.getKey();
        BaseSchema value = entry.getValue();
        if (headers.containsKey(key)) {
          if (headers.get(key).equalsIgnoreCase((String) value.getValue())) {
            matchingSize.getAndDecrement();
          }
        }
        if (matchingSize.get() == 0) {
          mockRequestModel.setResponse(mockingMatchingRequestEmbedded.getResponse());
          break;
        }
      }
      if (mockRequestModel.getResponse() != null) {
        break;
      }
    }
  }

  private static void initMockingRequestQueries(
      List<MockingMatchingRequestEmbedded> mockingRequestQueries,
      MockRequestModel mockRequestModel,
      Map<String, String[]> parameters) {
    if (MapUtils.isEmpty(parameters)) {
      return;
    }
    for (MockingMatchingRequestEmbedded mockingMatchingRequestEmbedded : mockingRequestQueries) {
      if (null == mockingMatchingRequestEmbedded.getMatchingProperty() || MapUtils
          .isEmpty(mockingMatchingRequestEmbedded.getMatchingProperty().getProperties())) {
        break;
      }
      AtomicInteger matchingSize = new AtomicInteger(
          mockingMatchingRequestEmbedded.getMatchingProperty()
              .getProperties().size());
      for (Entry<String, BaseSchema> entry : mockingMatchingRequestEmbedded
          .getMatchingProperty().getProperties().entrySet()) {
        String key = entry.getKey();
        BaseSchema value = entry.getValue();
        if (parameters.containsKey(key)) {
          String parameterValues = String.join(",", parameters.get(key));
          if (parameterValues.equalsIgnoreCase((String) value.getValue())) {
            matchingSize.getAndDecrement();
          }
        }
        if (matchingSize.get() == 0) {
          mockRequestModel.setResponse(mockingMatchingRequestEmbedded.getResponse());
          break;
        }
      }
      if (mockRequestModel.getResponse() != null) {
        break;
      }
    }

  }
}
