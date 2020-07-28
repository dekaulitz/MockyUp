package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupHistoryVmodel;
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

    @Override
    public DtoMockupHistoryVmodel getMockHistoryByIdAndMockId(String mockId, String id) {
        Query queryHistory = new Query();
        queryHistory.addCriteria(Criteria.where("mockId").is(mockId).and("id").is(new ObjectId(id)));
        queryHistory.fields().include("mockId").include("swagger");

        return mongoTemplate.findOne(queryHistory, DtoMockupHistoryVmodel.class,
                "mockup_histories");
    }
}
