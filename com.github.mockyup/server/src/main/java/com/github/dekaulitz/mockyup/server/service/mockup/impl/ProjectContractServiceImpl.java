package com.github.dekaulitz.mockyup.server.service.mockup.impl;

import com.github.dekaulitz.mockyup.server.db.entities.v2.ProjectContractEntities;
import com.github.dekaulitz.mockyup.server.db.entities.v2.ProjectEntities;
import com.github.dekaulitz.mockyup.server.db.query.ProjectContractQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.CacheConstants;
import com.github.dekaulitz.mockyup.server.model.constants.DatabaseCollections;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import com.github.dekaulitz.mockyup.server.service.common.helper.MessageHelper;
import com.github.dekaulitz.mockyup.server.service.common.helper.constants.MessageType;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectService;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi.OpenApiCommonHelper;
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
  public ProjectContractEntities getById(String id) throws ServiceException {
    final String cacheKey = CacheConstants.PROJECT_CONTRACT_PREFIX + id;
    ProjectContractEntities entity = cacheService
        .findCacheByKey(cacheKey, ProjectContractEntities.class);
    if (entity == null) {
      GetProjectContractParam getProjectParam = GetProjectContractParam.builder()
          .id(id)
          .build();
      ProjectContractQuery query = new ProjectContractQuery();
      query.buildQuery(getProjectParam);
      entity = mongoTemplate.findOne(query.getQuery(), ProjectContractEntities.class);
      if (entity != null) {
        cacheService.createCache(CacheConstants.PROJECT_PREFIX + id, entity,
            CacheConstants.ONE_HOUR_IN_SECONDS);
      }
    }
    return entity;
  }

  @Override
  public List<ProjectContractEntities> getAll(GetProjectContractParam getProjectContractParam)
      throws ServiceException {
    final String cacheKey =
        CacheConstants.PROJECT_CONTRACT_PREFIX + getProjectContractParam.toStringSkipNulls();
    List<ProjectContractEntities> result = cacheService
        .findCacheListByKey(cacheKey, ProjectContractEntities.class);
    if (CollectionUtils.isEmpty(result)) {
      ProjectContractQuery query = new ProjectContractQuery();
      query.buildQuery(getProjectContractParam);
      result = mongoTemplate
          .find(query.getQueryWithPaging(), ProjectContractEntities.class,
              DatabaseCollections.PROJECT_CONTRACT_COLLECTIONS);
      if (CollectionUtils.isNotEmpty(result)) {
        cacheService
            .createCache(
                CacheConstants.PROJECT_PREFIX + getProjectContractParam.toStringSkipNulls(),
                result, CacheConstants.ONE_HOUR_IN_SECONDS);
      } else {
        throw new ServiceException(MessageHelper.getMessage(MessageType.DATA_NOT_FOUND));
      }
    }
    return result;
  }

  @Override
  public ProjectContractEntities createContract(
      CreateProjectContractRequest createProjectContractRequest) throws ServiceException {
    ProjectEntities projectEntities = projectService
        .getById(createProjectContractRequest.getProjectId());
    if (projectEntities == null) {
      throw new ServiceException(MessageHelper.getMessage(MessageType.DATA_NOT_FOUND),
          "project not found with id " + createProjectContractRequest.getProjectId());
    }
    try {
      SwaggerParseResult result;
      result = new OpenAPIParser().readContents(
          JsonMapper.mapper().writeValueAsString(createProjectContractRequest.getSpec()), null,
          null);
      OpenAPI openApi = result.getOpenAPI();
      ProjectContractEntities projectContractEntities = new ProjectContractEntities();
      projectContractEntities.setProjectId(createProjectContractRequest.getProjectId());
      projectContractEntities.setOpenApiVersion(openApi.getOpenapi());
      projectContractEntities
          .setRawSpecs(
              JsonMapper.mapper().writeValueAsString(createProjectContractRequest.getSpec()));
      projectContractEntities.setInfo(
          OpenApiTransformerHelper.initOpenApiInfo(openApi.getInfo(), modelMapper));
      projectContractEntities.setSecurity(
          OpenApiTransformerHelper.iniOpenApiSecurity(openApi.getSecurity()));
      projectContractEntities.setServers(
          OpenApiTransformerHelper.getOpenApiServers(openApi.getServers()));
      projectContractEntities.setTags(OpenApiTransformerHelper.getOpenApiTags(openApi.getTags()));
      projectContractEntities
          .setComponents(OpenApiTransformerHelper.getOpenApiComponents(openApi.getComponents()));
      projectContractEntities
          .setPaths(OpenApiTransformerHelper.initOpenApiPath(openApi.getPaths()));
//      OpenApiRefHelper.rendering$refInformation(projectContractEntities);
      return mongoTemplate
          .insert(projectContractEntities);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("invalid mock structure ex:{} request:{}", e, createProjectContractRequest);
      throw new ServiceException(MessageHelper.getMessage(MessageType.INVALID_MOCK_CONTRACT),
          e.getMessage());
    }
  }
}
