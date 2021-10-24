package com.github.dekaulitz.mockyup.server.facade.cms;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.db.entities.ProjectEntity;
import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.dto.ProjectTagsModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectTagsParam;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.request.UpdateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.request.contract.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.request.contract.UpdateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.request.user.CreateUserRequest;
import com.github.dekaulitz.mockyup.server.model.request.user.UpdateUserRequest;
import com.github.dekaulitz.mockyup.server.model.response.contract.ContractCardResponseModel;
import com.github.dekaulitz.mockyup.server.service.auth.WithAuthService;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectService;
import com.github.dekaulitz.mockyup.server.service.cms.api.UserService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CmsFacade extends WithAuthService {

  @Autowired
  @Qualifier(value = "userService")
  private UserService userService;
  @Autowired
  @Qualifier(value = "projectService")
  private ProjectService projectService;

  @Autowired
  @Qualifier(value = "projectContractService")
  private ProjectContractService projectContractService;

  public UserEntity createUser(@Valid @NotNull CreateUserRequest createUserRequest)
      throws ServiceException {
    AuthProfileModel authProfile = this.getAuthProfile();
    return userService.createUser(createUserRequest, authProfile);
  }

  public UserEntity getUserDetail(String userId)
      throws ServiceException {
    UserEntity userEntity = userService.getById(userId, UserEntity.class);
    if (userEntity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND);
    }
    return userEntity;
  }

  public UserEntity updateUser(String id, @Valid @NotNull UpdateUserRequest updateUserRequest)
      throws ServiceException {
    return userService.updateUser(id, updateUserRequest, getAuthProfile());
  }

  public List<UserEntity> allUsers(@Valid GetUserParam getUserParam)
      throws ServiceException {
    return userService.getAll(getUserParam);
  }

  public long getCount(@Valid GetUserParam getUserParam)
      throws ServiceException {
    return userService.getCount(getUserParam);
  }

  public ProjectEntity createProject(CreateProjectRequest createProjectRequest)
      throws ServiceException {
    AuthProfileModel authProfileModel = this.getAuthProfile();
    return projectService.createProject(createProjectRequest, authProfileModel);
  }

  public ProjectEntity getProjectDetail(String projectId)
      throws ServiceException {
    ProjectEntity projectEntity = projectService.getById(projectId, ProjectEntity.class);
    if (projectEntity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND);
    }
    return projectEntity;
  }

  public List<ProjectTagsModel> getProjectTag(GetProjectTagsParam getProjectTagsParam) {
    return projectService.getProjectTags(getProjectTagsParam);
  }

  public ProjectEntity updateProject(String id, UpdateProjectRequest updateProjectRequest)
      throws ServiceException {
    AuthProfileModel authProfileModel = this.getAuthProfile();
    return projectService.updateProject(id, updateProjectRequest, authProfileModel);
  }

  public List<ProjectEntity> allProjects(@Valid @NotNull GetProjectParam getProjectParam)
      throws ServiceException {
    return projectService.getAll(getProjectParam);
  }

  public long getCount(@Valid GetProjectParam getProjectParam)
      throws ServiceException {
    return projectService.getCount(getProjectParam);
  }

  public List<ContractCardResponseModel> allContractCards(
      GetProjectContractParam getProjectContractParam) {
    return projectContractService.getContractCards(getProjectContractParam);
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
