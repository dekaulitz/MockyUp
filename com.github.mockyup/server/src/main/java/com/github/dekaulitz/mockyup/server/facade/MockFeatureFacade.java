package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.db.query.ProjectContractQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestAttributeModel;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestModel;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.MockingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockFeatureFacade {

  @Autowired
  private ProjectContractService projectContractService;

  @Autowired
  private MockingService mockingService;

  public MockRequestModel getMockingRequest(MockRequestAttributeModel mockRequestAttributeModel)
      throws ServiceException {
    ProjectContractQuery projectContractQuery = new ProjectContractQuery();
    projectContractQuery.contractEndpoint(mockRequestAttributeModel.getMockEndpoint());
    ProjectContractEntity contract = projectContractService.findOne(projectContractQuery.getQuery(),
        ProjectContractEntity.class);
    if (contract == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND);
    }
    return mockingService.mockingRequest(mockRequestAttributeModel, contract);
  }
}
