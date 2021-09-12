package com.github.dekaulitz.mockyup.server.service.mockup.api;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestAttributeModel;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestModel;
import org.springframework.stereotype.Service;

@Service
public interface MockingService {

  MockRequestModel mockingRequest(MockRequestAttributeModel mockRequestAttributeModel,
      ProjectContractEntity projectContractEntity)
      throws ServiceException;
}
