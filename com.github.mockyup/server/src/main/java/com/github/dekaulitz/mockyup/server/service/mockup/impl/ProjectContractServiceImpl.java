package com.github.dekaulitz.mockyup.server.service.mockup.impl;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.db.entities.ProjectEntity;
import com.github.dekaulitz.mockyup.server.db.query.ProjectContractQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.CacheConstants;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import com.github.dekaulitz.mockyup.server.service.common.helper.MessageHelper;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectService;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi.OpenApiTransformerHelper;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectContractServiceImpl implements ProjectContractService {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private CacheService cacheService;

  @Autowired
  private ProjectService projectService;
  @Autowired
  private ModelMapper modelMapper;


  @Override
  public ProjectContractEntity getById(String id) throws ServiceException {
    final String cacheKey = CacheConstants.PROJECT_CONTRACT_PREFIX + id;
    ProjectContractEntity entity = cacheService
        .findCacheByKey(cacheKey, ProjectContractEntity.class);
    if (entity == null) {
      GetProjectContractParam getProjectParam = GetProjectContractParam.builder()
          .id(id)
          .build();
      ProjectContractQuery query = new ProjectContractQuery();
      query.buildQuery(getProjectParam);
      entity = mongoTemplate.findOne(query.getQuery(), ProjectContractEntity.class);
      if (entity != null) {
        cacheService.createCache(CacheConstants.PROJECT_PREFIX + id, entity,
            CacheConstants.ONE_HOUR_IN_SECONDS);
      } else {
        throw new ServiceException(MessageHelper.getMessage(ResponseCode.DATA_NOT_FOUND));
      }
    }
    return entity;
  }

  @Override
  public List<ProjectContractEntity> getAll(GetProjectContractParam getProjectContractParam)
      throws ServiceException {
    final String cacheKey =
        CacheConstants.PROJECT_CONTRACT_PREFIX + getProjectContractParam.toStringSkipNulls();
    List<ProjectContractEntity> result = cacheService
        .findCacheListByKey(cacheKey, ProjectContractEntity.class);
    if (CollectionUtils.isEmpty(result)) {
      ProjectContractQuery query = new ProjectContractQuery();
      query.buildQuery(getProjectContractParam);
      result = mongoTemplate
          .find(query.getQueryWithPaging(), ProjectContractEntity.class);
      if (CollectionUtils.isNotEmpty(result)) {
        cacheService
            .createCache(
                CacheConstants.PROJECT_PREFIX + getProjectContractParam.toStringSkipNulls(),
                result, CacheConstants.ONE_HOUR_IN_SECONDS);
      } else {
        throw new ServiceException(MessageHelper.getMessage(ResponseCode.DATA_NOT_FOUND));
      }
    }
    return result;
  }

  @Override
  public ProjectContractEntity createContract(
      CreateProjectContractRequest createProjectContractRequest) throws ServiceException {
    ProjectEntity projectEntity = projectService
        .getById(createProjectContractRequest.getProjectId());
    if (projectEntity == null) {
      throw new ServiceException(MessageHelper.getMessage(ResponseCode.DATA_NOT_FOUND),
          "project not found with id " + createProjectContractRequest.getProjectId());
    }
    try {
      SwaggerParseResult result;
      result = new OpenAPIParser().readContents(
          JsonMapper.mapper().writeValueAsString(createProjectContractRequest.getSpec()), null,
          null);
      OpenAPI openApi = result.getOpenAPI();
      ProjectContractEntity projectContractEntity = new ProjectContractEntity();
      projectContractEntity.setProjectId(createProjectContractRequest.getProjectId());
      projectContractEntity.setOpenApiVersion(openApi.getOpenapi());
      projectContractEntity
          .setRawSpecs(
              JsonMapper.mapper().writeValueAsString(createProjectContractRequest.getSpec()));
      projectContractEntity.setInfo(
          OpenApiTransformerHelper.initOpenApiInfo(openApi.getInfo(), modelMapper));
      // @TODO i think it will be nice if we also injecting mockup server too after saved data into database
      projectContractEntity.setSecurity(
          OpenApiTransformerHelper.iniOpenApiSecurity(openApi.getSecurity()));
      projectContractEntity.setServers(
          OpenApiTransformerHelper.getOpenApiServers(openApi.getServers()));
      projectContractEntity.setTags(OpenApiTransformerHelper.getOpenApiTags(openApi.getTags()));
      projectContractEntity
          .setComponents(OpenApiTransformerHelper.getOpenApiComponents(openApi.getComponents()));
      projectContractEntity
          .setPaths(OpenApiTransformerHelper.initOpenApiPath(openApi.getPaths()));
//      OpenApiRefHelper.rendering$refInformation(projectContractEntities);
      return mongoTemplate
          .insert(projectContractEntity);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("invalid mock structure ex:{} request:{}", e, createProjectContractRequest);
      throw new ServiceException(MessageHelper.getMessage(ResponseCode.INVALID_MOCK_CONTRACT),
          e.getMessage());
    }
  }
}
