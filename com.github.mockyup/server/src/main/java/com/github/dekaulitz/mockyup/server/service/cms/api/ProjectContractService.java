package com.github.dekaulitz.mockyup.server.service.cms.api;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.request.contract.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.request.contract.UpdateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.response.contract.ContractCardResponseModel;
import com.github.dekaulitz.mockyup.server.service.common.api.BaseCrudService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ProjectContractService extends BaseCrudService<ProjectContractEntity> {

  ProjectContractEntity createContract(CreateProjectContractRequest createProjectContractRequest,
      AuthProfileModel authProfileModel)
      throws ServiceException;

  ProjectContractEntity updateContract(String id,
      UpdateProjectContractRequest updateProjectContractRequest,
      AuthProfileModel authProfileModel)
      throws ServiceException;

  List<ContractCardResponseModel> getContractCards(GetProjectContractParam getProjectContractParam);


  long getCount(GetProjectContractParam getProjectContractParam);
}
