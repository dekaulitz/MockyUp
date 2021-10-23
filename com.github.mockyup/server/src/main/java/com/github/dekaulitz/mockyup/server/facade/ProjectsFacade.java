package com.github.dekaulitz.mockyup.server.facade;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.dto.ProjectTagsModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectTagsParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.request.UpdateProjectRequest;
import com.github.dekaulitz.mockyup.server.service.auth.WithAuthService;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProjectsFacade extends WithAuthService {
  @Autowired
  @Qualifier(value = "projectService")
  @Getter
  private ProjectService projectService;


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
}
