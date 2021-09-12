package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestAttributeModel;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestModel;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.constants.OpenApiPathHttpMethod;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.mockup.MockRequestHelper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockFeatureFacade {

  @Autowired
  private ProjectContractService projectContractService;

  public MockRequestModel getMockingRequest(MockRequestAttributeModel mockRequestAttributeModel)
      throws ServiceException {
    ProjectContractEntity contract = projectContractService.getById(
        mockRequestAttributeModel.getContractId(), ProjectContractEntity.class);

    OpenApiPathHttpMethod openApiPathHttpMethod;
    if (EnumUtils.isValidEnum(OpenApiPathHttpMethod.class,
        mockRequestAttributeModel.getRequestMethod().toUpperCase())) {
      openApiPathHttpMethod = OpenApiPathHttpMethod.valueOf(
          mockRequestAttributeModel.getRequestMethod().toUpperCase());
    } else {
      throw new ServiceException(ResponseCode.UNSUPPORTED_MOCK_TYPE);
    }
    OpenApiContentType openApiContentType = null;

    if (OpenApiContentType.isValid(mockRequestAttributeModel.getContentType())) {
      openApiContentType = OpenApiContentType.get(mockRequestAttributeModel.getContentType());
    }

    String[] paths = mockRequestAttributeModel.getRequestPath().split("/");

    final OpenApiPathHttpMethod pathHttpMethod = openApiPathHttpMethod;
    List<OpenApiPathEmbedded> pathInfos = contract
        .getPaths().stream()
        .filter(openApiPathEmbedded ->
            openApiPathEmbedded.getPath().split("/").length == paths.length)
        .filter(openApiPathEmbedded -> openApiPathEmbedded.getHttpMethod() == pathHttpMethod)
        .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(pathInfos)) {
      throw new ServiceException(ResponseCode.MOCK_NOT_FOUND,
          " contractId: " + mockRequestAttributeModel.getContractId() + " with path: "
              + mockRequestAttributeModel.getRequestPath() + " method: "
              + pathHttpMethod);
    }
    MockRequestModel mockRequestModel = new MockRequestModel();
    mockRequestModel.setPath(mockRequestAttributeModel.getRequestPath());
    MockRequestHelper
        .initMockRequest(pathInfos, mockRequestAttributeModel.getHeaders(),
            mockRequestAttributeModel.getParameters(), mockRequestAttributeModel.getRequestPath(),
            mockRequestAttributeModel.getBody(), mockRequestModel,
            mockRequestAttributeModel.getContractId(),
            openApiContentType);
    return mockRequestModel;
  }
}
