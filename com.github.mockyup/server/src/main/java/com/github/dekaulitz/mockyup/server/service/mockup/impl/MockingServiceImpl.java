package com.github.dekaulitz.mockyup.server.service.mockup.impl;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntities;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiPathHttpMethod;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestModel;
import com.github.dekaulitz.mockyup.server.service.common.helper.MessageHelper;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.MessageType;
import com.github.dekaulitz.mockyup.server.service.mockup.api.MockingService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.mockup.MockRequestHelper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MockingServiceImpl implements MockingService {

  @Autowired
  private ProjectContractService projectContractService;

  @Override
  public MockRequestModel mockingRequest(String contractId, String requestPath, String requestBody, String httpMethod,
      Map<String, String> headers, Map<String, String[]> parameters, String contentType)
      throws ServiceException {
    OpenApiPathHttpMethod openApiPathHttpMethod = null;
    if (EnumUtils.isValidEnum(OpenApiPathHttpMethod.class, httpMethod.toUpperCase())) {
      openApiPathHttpMethod = OpenApiPathHttpMethod.valueOf(httpMethod.toUpperCase());
    } else {
      throw new ServiceException(MessageHelper.getMessage(MessageType.UNSUPPORTED_MOCK_TYPE));
    }
    OpenApiContentType openApiContentType = null;

    if (OpenApiContentType.isValid(contentType)) {
      openApiContentType = OpenApiContentType.get(contentType);
    }

    ProjectContractEntities contract = projectContractService.getById(contractId);
    String[] paths = requestPath.split("/");

    final OpenApiPathHttpMethod pathHttpMethod = openApiPathHttpMethod;
    List<OpenApiPathEmbedded> pathInfos = contract
        .getPaths().stream()
        .filter(openApiPathEmbedded ->
            openApiPathEmbedded.getPath().split("/").length == paths.length)
        .filter(openApiPathEmbedded -> openApiPathEmbedded.getHttpMethod() == pathHttpMethod)
        .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(pathInfos)) {
      throw new ServiceException(MessageHelper.getMessage(MessageType.MOCK_NOT_FOUND),
          " contractId: " + contractId + " with path: " + requestPath + " method: " + pathHttpMethod);
    }
    MockRequestModel mockRequestModel = new MockRequestModel();
    mockRequestModel.setPath(requestPath);
    MockRequestHelper
        .initMockRequest(pathInfos, headers, parameters, requestPath, requestBody, mockRequestModel, contractId,
            openApiContentType);
    return mockRequestModel;
  }


}
