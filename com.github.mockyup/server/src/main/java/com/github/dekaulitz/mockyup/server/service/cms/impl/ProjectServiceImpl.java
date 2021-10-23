package com.github.dekaulitz.mockyup.server.service.cms.impl;

import com.github.dekaulitz.mockyup.server.db.aggregation.ProjectAggregation;
import com.github.dekaulitz.mockyup.server.db.entities.ProjectEntity;
import com.github.dekaulitz.mockyup.server.db.query.ProjectQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.dto.ProjectTagsModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectTagsParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.request.UpdateProjectRequest;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectService;
import com.github.dekaulitz.mockyup.server.service.common.impl.BaseCrudServiceImpl;
import java.util.Date;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service(value = "projectService")
public class ProjectServiceImpl extends BaseCrudServiceImpl<ProjectEntity> implements
    ProjectService {

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public ProjectEntity createProject(CreateProjectRequest createProjectRequest,
      AuthProfileModel authProfileModel)
      throws ServiceException {
    ProjectEntity projectEntity = new ProjectEntity();
    projectEntity.setCreatedByUserId(authProfileModel.getId());
    projectEntity.setUpdatedByUserId(authProfileModel.getId());
    projectEntity.setCreatedDate(new Date());
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
    projectEntity.setProjectName(updateProjectRequest.getProjectName());
    projectEntity.setProjectDescription(updateProjectRequest.getProjectDescription());
    projectEntity.setProjectTags(updateProjectRequest.getProjectTags());
    projectEntity.setUpdatedByUserId(authProfileModel.getId());
    return this.save(projectEntity);
  }

  @Override
  public List<ProjectEntity> getAll(GetProjectParam getProjectParam) {
    ProjectQuery projectQuery = new ProjectQuery();
    projectQuery.buildQuery(getProjectParam);
    return this.getAll(projectQuery.getQueryWithPaging(), ProjectEntity.class);
  }

  @Override
  public long getCount(GetProjectParam getProjectParam) {
    ProjectQuery query = new ProjectQuery();
    query.buildQuery(getProjectParam);
    return this.getMongoTemplate().count(query.getQuery(), ProjectEntity.class);
  }

  @Override
  public List<ProjectTagsModel> getProjectTags(
      GetProjectTagsParam getProjectTagsParam) {
    return this.getMongoTemplate()
        .aggregate(ProjectAggregation.getTaggingAggregation(getProjectTagsParam), "projects",
            ProjectTagsModel.class).getMappedResults();
  }

  @Override
  public void deleteById(String id) throws ServiceException {
    ProjectEntity entity = getById(id, ProjectEntity.class);
    if (entity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND,
          "project not found id: " + id);
    }
    this.delete(entity);
  }
}
