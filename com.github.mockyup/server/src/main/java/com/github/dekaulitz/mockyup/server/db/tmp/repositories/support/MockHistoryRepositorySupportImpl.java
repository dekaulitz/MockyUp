package com.github.dekaulitz.mockyup.server.db.tmp.repositories.support;

import com.github.dekaulitz.mockyup.server.tmp.domain.mocks.vmodels.DtoMockupHistoryVmodel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MockHistoryRepositorySupportImpl implements MockHistoryRepositorySupport {

  @Autowired
  private final MongoTemplate mongoTemplate;

  public MockHistoryRepositorySupportImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  /**
   * get all mock history
   *
   * @param mockId    {@link String} id from mock colelction
   * @param historyId {@link String} id from history collection
   * @return DtoMockupHistoryVmodel
   */
  @Override
  public DtoMockupHistoryVmodel getMockHistoryByIdAndMockId(String mockId, String historyId) {
    Query queryHistory = new Query();
    queryHistory
        .addCriteria(Criteria.where("mockId").is(mockId).and("id").is(new ObjectId(historyId)));
    queryHistory.fields().include("mockId").include("swagger");

    return mongoTemplate.findOne(queryHistory, DtoMockupHistoryVmodel.class,
        "mockup_histories");
  }
}
