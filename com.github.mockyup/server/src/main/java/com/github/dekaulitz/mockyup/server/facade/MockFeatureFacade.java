package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
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
    ProjectContractEntity contract = projectContractService.getById(
        mockRequestAttributeModel.getContractId(), ProjectContractEntity.class);
    return mockingService.mockingRequest(mockRequestAttributeModel, contract);
  }
}
