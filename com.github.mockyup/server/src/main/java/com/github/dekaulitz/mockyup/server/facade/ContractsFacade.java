package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.request.contract.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.request.contract.UpdateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.response.contract.ContractCardResponseModel;
import com.github.dekaulitz.mockyup.server.service.auth.WithAuthService;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectContractService;
import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ContractsFacade extends WithAuthService {
  @Autowired
  @Qualifier(value = "projectContractService")
  @Getter
  private ProjectContractService projectContractService;


  public List<ContractCardResponseModel> allContractCards(
      GetProjectContractParam getProjectContractParam) {
    return projectContractService.getContractCards(getProjectContractParam);
  }

  public long getCount(@Valid GetProjectContractParam getProjectContractParam)
      throws ServiceException {
    return projectContractService.getCount(getProjectContractParam);
  }

  public ProjectContractEntity getByContractId(String contractId) throws ServiceException {
    ProjectContractEntity projectContractEntity = projectContractService.getById(contractId,
        ProjectContractEntity.class);
    if (projectContractEntity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND);
    }
    return projectContractEntity;
  }

  public ProjectContractEntity createContract(
      CreateProjectContractRequest createProjectContractRequest) throws ServiceException {
    AuthProfileModel authProfileModel = this.getAuthProfile();
    return projectContractService.createContract(createProjectContractRequest, authProfileModel);
  }

  public ProjectContractEntity updateContract(String id,
      UpdateProjectContractRequest updateProjectContractRequest) throws ServiceException {
    AuthProfileModel authProfileModel = this.getAuthProfile();
    return projectContractService.updateContract(id, updateProjectContractRequest,
        authProfileModel);
  }
}
