package com.github.dekaulitz.mockyup.server.service.mockup.impl;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectEntity;
import com.github.dekaulitz.mockyup.server.db.query.ProjectQuery;
import com.github.dekaulitz.mockyup.server.model.constants.CacheConstants;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private CacheService cacheService;

  @Override
  public ProjectEntity getById(String id) {
    final String cacheKey = CacheConstants.PROJECT_PREFIX + id;
    ProjectEntity entity = cacheService.findCacheByKey(cacheKey, ProjectEntity.class);
    if (entity == null) {
      GetProjectParam getProjectParam = GetProjectParam.builder()
          .id(id)
          .build();
      ProjectQuery query = new ProjectQuery();
      query.buildQuery(getProjectParam);
      entity = mongoTemplate.findOne(query.getQuery(), ProjectEntity.class);
      if (entity != null) {
        cacheService.createCache(CacheConstants.PROJECT_PREFIX + id, entity,
            CacheConstants.ONE_HOUR_IN_SECONDS);
      }
    }
    return entity;
  }

  @Override
  public List<ProjectEntity> getAll(GetProjectParam getProjectParam) {
    final String cacheKey = CacheConstants.PROJECT_PREFIX + getProjectParam.toStringSkipNulls();
    List<ProjectEntity> result = cacheService
        .findCacheListByKey(cacheKey, ProjectEntity.class);
    if (CollectionUtils.isEmpty(result)) {
      ProjectQuery query = new ProjectQuery();
      query.buildQuery(getProjectParam);
      result = mongoTemplate
          .find(query.getQueryWithPaging(), ProjectEntity.class);
      if (CollectionUtils.isNotEmpty(result)) {
        cacheService
            .createCache(CacheConstants.PROJECT_PREFIX + getProjectParam.toStringSkipNulls(),
                result, CacheConstants.ONE_HOUR_IN_SECONDS);
      }
    }
    return result;
  }

  @Override
  public ProjectEntity createProject(CreateProjectRequest createProjectRequest) {
    ProjectEntity projectEntity = modelMapper.map(createProjectRequest, ProjectEntity.class);
    return mongoTemplate.insert(projectEntity);
  }
}
