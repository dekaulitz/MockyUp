package com.github.dekaulitz.mockyup.server.service.mockup.api;

import com.github.dekaulitz.mockyup.server.db.entities.v2.ProjectContractEntities;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectContractRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ProjectContractService {

  ProjectContractEntities getById(String id) throws ServiceException;

  List<ProjectContractEntities> getAll(GetProjectContractParam getProjectContractParam)
      throws ServiceException;

  ProjectContractEntities createContract(CreateProjectContractRequest createProjectContractRequest)
      throws ServiceException;

}
