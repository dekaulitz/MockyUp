package com.github.dekaulitz.mockyup.server.service.mockup.impl;

import com.github.dekaulitz.mockyup.server.db.entities.v1.MockEntities;
import com.github.dekaulitz.mockyup.server.db.entities.v2.ProjectEntities;
import com.github.dekaulitz.mockyup.server.db.query.MockUpQuery;
import com.github.dekaulitz.mockyup.server.model.constants.CacheConstants;
import com.github.dekaulitz.mockyup.server.model.constants.DatabaseCollections;
import com.github.dekaulitz.mockyup.server.model.param.GetMockUpParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.response.MockupResponse;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.MockupService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockupServiceImpl implements MockupService {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private CacheService cacheService;

  @Override
  public ProjectEntities createProject(CreateProjectRequest createProjectRequest) {
    return null;
  }

  @Override
  public MockEntities getById(String id) {
    final String cacheKey = CacheConstants.MOCKUP_PREFIX + id;
    MockUpQuery mockUpQuery = new MockUpQuery();
    mockUpQuery.id(id);
    MockEntities mockEntities = cacheService.findCacheByKey(cacheKey, MockEntities.class);
    if (mockEntities == null) {
      mockEntities = mongoTemplate.findOne(mockUpQuery.getQuery(), MockEntities.class);
      if (mockEntities != null) {
        cacheService.createCache(CacheConstants.MOCKUP_PREFIX + id, mockEntities,
            CacheConstants.ONE_HOUR_IN_SECONDS);
      }
    }
    return mockEntities;
  }

  @Override
  public List<MockupResponse> getAll(GetMockUpParam getMockUpParam) {
    String cacheKey = CacheConstants.MOCKUP_PREFIX + getMockUpParam.toStringSkipNulls();
    MockUpQuery mockUpQuery = new MockUpQuery();
    mockUpQuery.buildQuery(getMockUpParam);
    List<MockupResponse> mockupResponses = cacheService
        .findCacheListByKey(cacheKey, MockupResponse.class);
    if (CollectionUtils.isEmpty(mockupResponses)) {
      mockupResponses = mongoTemplate
          .find(mockUpQuery.getQueryWithPaging(), MockupResponse.class,
              DatabaseCollections.MOCKUP_COLLECTIONS);
      if (CollectionUtils.isNotEmpty(mockupResponses)) {
        cacheService.createCache(CacheConstants.MOCKUP_PREFIX + getMockUpParam.toStringSkipNulls(),
            mockupResponses, CacheConstants.ONE_HOUR_IN_SECONDS);
      }
    }
    return mockupResponses;
  }
}
