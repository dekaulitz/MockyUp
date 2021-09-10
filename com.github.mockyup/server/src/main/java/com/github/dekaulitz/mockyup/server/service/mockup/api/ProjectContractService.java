package com.github.dekaulitz.mockyup.server.service.mockup.api;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectContractRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ProjectContractService {

  ProjectContractEntity getById(String id) throws ServiceException;

  List<ProjectContractEntity> getAll(GetProjectContractParam getProjectContractParam)
      throws ServiceException;

  ProjectContractEntity createContract(CreateProjectContractRequest createProjectContractRequest)
      throws ServiceException;

}
