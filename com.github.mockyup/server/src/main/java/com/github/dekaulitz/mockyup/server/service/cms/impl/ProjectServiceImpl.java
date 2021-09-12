package com.github.dekaulitz.mockyup.server.service.cms.impl;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectEntity;
import com.github.dekaulitz.mockyup.server.db.query.ProjectQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.request.UpdateProjectRequest;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectService;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.service.common.impl.BaseCrudServiceImpl;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service(value = "projectService")
public class ProjectServiceImpl extends BaseCrudServiceImpl<ProjectEntity> implements
    ProjectService {

  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private ModelMapper modelMapper;

  @Override
  public ProjectEntity createProject(CreateProjectRequest createProjectRequest,
      AuthProfileModel authProfileModel)
      throws ServiceException {
    ProjectEntity projectEntity = new ProjectEntity();
    projectEntity.setCreatedByUserId(authProfileModel.getId());
    projectEntity.setUpdatedByUserId(authProfileModel.getId());
    modelMapper.map(createProjectRequest, projectEntity);
    return this.save(projectEntity);
  }

  @Override
  @Retryable(value = {
      OptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000))
  public ProjectEntity updateProject(String id, UpdateProjectRequest updateProjectRequest,
      AuthProfileModel authProfileModel)
      throws ServiceException {
    ProjectEntity projectEntity = this.getById(id, ProjectEntity.class);
    if (projectEntity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND);
    }
    modelMapper.map(updateProjectRequest, projectEntity);
    projectEntity.setUpdatedByUserId(authProfileModel.getId());
    return this.save(projectEntity);
  }

  @Override
  public List<ProjectEntity> getAll(GetProjectParam getProjectParam) {
    ProjectQuery projectQuery = new ProjectQuery();
    projectQuery.buildQuery(getProjectParam);
    return this.getAll(projectQuery.getQueryWithPaging(), ProjectEntity.class);
  }


}
