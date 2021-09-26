package com.github.dekaulitz.mockyup.server.service.cms.api;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.request.UpdateProjectRequest;
import com.github.dekaulitz.mockyup.server.service.common.api.BaseCrudService;
import java.util.List;

public interface ProjectService extends BaseCrudService<ProjectEntity> {

  ProjectEntity createProject(CreateProjectRequest createProjectRequest,
      AuthProfileModel authProfileModel) throws ServiceException;

  ProjectEntity updateProject(String id, UpdateProjectRequest updateProjectRequest,
      AuthProfileModel authProfileModel)
      throws ServiceException;

  List<ProjectEntity> getAll(GetProjectParam getProjectParam);

  long getCount(GetProjectParam getProjectParam);
}
