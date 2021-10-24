package com.github.dekaulitz.mockyup.server.service.cms.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dekaulitz.mockyup.server.db.entities.ProjectContractEntity;
import com.github.dekaulitz.mockyup.server.db.entities.ProjectEntity;
import com.github.dekaulitz.mockyup.server.db.query.ProjectContractQuery;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.request.contract.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.request.contract.UpdateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.response.contract.ContractCardResponseModel;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.cms.api.ProjectService;
import com.github.dekaulitz.mockyup.server.service.common.impl.BaseCrudServiceImpl;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi.OpenApiTransformerHelper;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service("projectContractService")
@Slf4j
public class ProjectContractServiceImpl extends
    BaseCrudServiceImpl<ProjectContractEntity> implements
    ProjectContractService {

  @Value("${com.github.dekaulitz.mockyup.host}")
  private String serverHost;
  @Autowired
  @Qualifier("projectService")
  private ProjectService projectService;
  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private ModelMapper modelMapper;

  @Override
  public ProjectContractEntity createContract(
      CreateProjectContractRequest createProjectContractRequest,
      AuthProfileModel authProfileModel)
      throws ServiceException {
    ProjectEntity projectEntity = projectService.getById(
        createProjectContractRequest.getProjectId(), ProjectEntity.class);
    if (projectEntity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND,
          "project not found id: " + createProjectContractRequest.getProjectId());
    }
    ProjectContractEntity projectContractEntity = new ProjectContractEntity();
    projectContractEntity.setCreatedByUserId(authProfileModel.getId());
    projectContractEntity.setCreatedDate(new Date());
    projectContractEntity.setUpdatedByUserId(authProfileModel.getId());
    projectContractEntity.setPrivate(createProjectContractRequest.isPrivate());
    initProjectContract(projectContractEntity, createProjectContractRequest.getProjectId(),
        createProjectContractRequest.getSpec());
    return this.save(projectContractEntity);
  }

  @Override
  @Retryable(value = {
      OptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000))
  public ProjectContractEntity updateContract(String id,
      UpdateProjectContractRequest updateProjectContractRequest, AuthProfileModel authProfileModel)
      throws ServiceException {
    ProjectEntity projectEntity = projectService.getById(
        updateProjectContractRequest.getProjectId(), ProjectEntity.class);
    if (projectEntity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND,
          "project not found id: " + updateProjectContractRequest.getProjectId());
    }
    ProjectContractEntity projectContractEntity = this.getById(id, ProjectContractEntity.class);
    if (projectContractEntity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND,
          "contract not found id: " + id);
    }
    projectContractEntity.setPrivate(updateProjectContractRequest.isPrivate());
    projectContractEntity.setUpdatedByUserId(authProfileModel.getId());
    initProjectContract(projectContractEntity, updateProjectContractRequest.getProjectId(),
        updateProjectContractRequest.getSpec());
    return this.update(projectContractEntity);
  }

  @Override
  public List<ContractCardResponseModel> getContractCards(
      GetProjectContractParam getProjectContractParam) {
    ProjectContractQuery projectContractQuery = new ProjectContractQuery();
    projectContractQuery.buildQuery(getProjectContractParam);
    return mongoTemplate.find(projectContractQuery.getQueryWithPaging(),
        ContractCardResponseModel.class, ProjectContractEntity.COLLECTION_NAME);
  }

  @Override
  public long getCount(GetProjectContractParam getProjectContractParam) {
    ProjectContractQuery projectContractQuery = new ProjectContractQuery();
    projectContractQuery.buildQuery(getProjectContractParam);
    return this.getMongoTemplate().count(projectContractQuery.getQuery(), ProjectEntity.class);
  }


  @Override
  public void deleteById(String id) throws ServiceException {
    ProjectContractEntity entity = getMongoTemplate().findById(id, ProjectContractEntity.class);
    if (entity == null) {
      throw new ServiceException(ResponseCode.DATA_NOT_FOUND,
          "project contract not found id: " + id);
    }
    this.delete(entity);
  }


  private void initProjectContract(ProjectContractEntity projectContractEntity,
      String projectId, LinkedHashMap<String, Object> spec)
      throws ServiceException {
    try {
      LinkedHashMap<String, Object> server = new LinkedHashMap<>();
      String mockEndpoint =
          StringUtils.isBlank(projectContractEntity.getMockEndpoint()) ? UUID.randomUUID()
              .toString() : projectContractEntity.getMockEndpoint();
      server.put("url", serverHost + ApplicationConstants.MOCK_REQUEST_PREFIX + mockEndpoint
          + ApplicationConstants.MOCK_REQUEST_ID_PREFIX);
      server.put("description", "Mockup host");
      boolean mockupDefined = false;
      if (spec.containsKey("servers")) {
        List<LinkedHashMap<String, Object>> servers = JsonMapper.mapper()
            .convertValue(spec.get("servers"),
                new TypeReference<List<LinkedHashMap<String, Object>>>() {
                });
        for (LinkedHashMap<String, Object> stringObjectLinkedHashMap : servers) {
          if (stringObjectLinkedHashMap.containsKey("url")) {
            String url = (String) stringObjectLinkedHashMap.get("url");
            if (url.contains(mockEndpoint)) {
              mockupDefined = true;
              break;
            }
          }
        }
        if (!mockupDefined) {
          servers.add(server);
          spec.put("servers", servers);
        }
      } else {
        spec.put("servers", Collections.singletonList(server));
      }
      projectContractEntity.setMockEndpoint(mockEndpoint);
      SwaggerParseResult result = new OpenAPIParser().readContents(
          JsonMapper.mapper().writeValueAsString(spec), null,
          null);
      OpenAPI openApi = result.getOpenAPI();
      projectContractEntity.setProjectId(projectId);
      projectContractEntity.setOpenApiVersion(openApi.getOpenapi());
      projectContractEntity
          .setRawSpecs(
              JsonMapper.mapper().writeValueAsString(spec));
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
      /**
       * @TODO should rendering ref property from openapi components for easy integration with mock and
       * define as Component type like schemas or examples
       * OpenApiRefHelper.rendering$refInformation(projectContractEntities);
       */

    } catch (Exception e) {
      e.printStackTrace();
      log.error("invalid mock structure error: ", e);
      throw new ServiceException(ResponseCode.INVALID_MOCK_CONTRACT,
          e.getMessage());
    }
  }
}
