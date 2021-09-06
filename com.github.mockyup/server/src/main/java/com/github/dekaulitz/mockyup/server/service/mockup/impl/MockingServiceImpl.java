package com.github.dekaulitz.mockyup.server.service.mockup.impl;

import com.github.dekaulitz.mockyup.server.db.entities.v2.ProjectContractEntities;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiPathHttpMethod;
import com.github.dekaulitz.mockyup.server.db.entities.v2.features.mockup.MockUpRequestEmbedded;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestModel;
import com.github.dekaulitz.mockyup.server.service.common.helper.MessageHelper;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.MessageType;
import com.github.dekaulitz.mockyup.server.service.mockup.api.MockingService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectContractService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MockingServiceImpl implements MockingService {

  @Autowired
  private ProjectContractService projectContractService;

  @Override
  public Object mockingRequest(String id, String path, String body, String httpMethod,
      Map<String, String> headers, Map<String, String[]> parameters) throws ServiceException {
    OpenApiPathHttpMethod openApiPathHttpMethod = null;
    if (EnumUtils.isValidEnum(OpenApiPathHttpMethod.class, httpMethod.toUpperCase())) {
      openApiPathHttpMethod = OpenApiPathHttpMethod.valueOf(httpMethod.toUpperCase());
    }
    ProjectContractEntities contract = projectContractService.getById(id);
    String[] paths = path.split("/");

    final OpenApiPathHttpMethod pathHttpMethod = openApiPathHttpMethod;
    List<OpenApiPathEmbedded> pathInfos = contract
        .getPaths().stream()
        .filter(openApiPathEmbedded ->
            openApiPathEmbedded.getPath().split("/").length == paths.length)
        .filter(openApiPathEmbedded -> openApiPathEmbedded.getHttpMethod() == pathHttpMethod)
        .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(pathInfos)) {
      throw new ServiceException(MessageHelper.getMessage(MessageType.MOCK_NOT_FOUND),
          "contractId: " + id + " with path" + path);
    }
    MockRequestModel mockRequestModel = new MockRequestModel();
    mockRequestModel.setPath(path);
    for (OpenApiPathEmbedded pathInfo : pathInfos) {
      /*
       * @TODO its not finished yet and need to reduce complexity
       */

//      private List<MockingMatchingRequestEmbedded> mockingRequestBodies;
//      private MockingMatchingResponseEmbedded responseDefault;
      if (null == pathInfo.getOperation() || null == pathInfo.getOperation().getMockup()) {
        throw new ServiceException(MessageHelper.getMessage(MessageType.MOCK_NOT_FOUND),
            "contractId: " + id + " with path" + path);
      }
      MockUpRequestEmbedded mockingPath = pathInfo
          .getOperation().getMockup();

      if (null != mockingPath.getMockingRequestPaths()) {
        pathInfo.getOperation().getMockup().getMockingRequestPaths()
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
        if (mockRequestModel.getResponse() != null) {
          break;
        }
      }

      if (null != mockingPath.getMockingRequestHeaders()) {
        pathInfo.getOperation().getMockup().getMockingRequestHeaders()
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
        if (mockRequestModel.getResponse() != null) {
          break;
        }
      }
      if (null != mockingPath.getMockingRequestQueries()) {
        pathInfo.getOperation().getMockup().getMockingRequestQueries()
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
        if (mockRequestModel.getResponse() != null) {
          break;
        }
      }

    }
    return mockRequestModel.getResponse();
  }


}
