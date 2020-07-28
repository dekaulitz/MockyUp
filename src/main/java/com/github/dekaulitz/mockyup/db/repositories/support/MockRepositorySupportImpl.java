package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.MockHistoryEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MockRepositorySupportImpl implements MockRepositorySupport {
    @Autowired
    private final MongoTemplate mongoTemplate;

    public MockRepositorySupportImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<DtoMockupDetailVmodel> getDetailMockUpIdByUserAccess(String id, AuthenticationProfileModel authenticationProfileModel) {
        Aggregation aggregation = Aggregation.newAggregation(
                //find by criteria
                Aggregation.match(Criteria.where("_id").is(id)),
                //extract users array as list
                Aggregation.unwind("users"),
                //set project field
                Aggregation.project().and("title").as("title").and("swagger").as("swagger")
                        .and("description").as("description").and("updatedBy").as("updatedBy")
                        //convert $users.userId to objectId for querying the user id
                        .and("updatedDate").as("updatedDate").and("users.access").as("users.access")
                        .andExpression("toObjectId('$users.userId')").as("users.userId"),
                //join collection with user entities
                LookupOperation.newLookup().from("userEntities").localField("users.userId").foreignField("_id").as("userDetails"),
                //extract the collection base on userdetails
                Aggregation.unwind("userDetails"),
                //join collection with user entities for get current user login access
                LookupOperation.newLookup().from("userEntities")
                        .localField("users.userId")
                        .foreignField("_id")
                        .as("currentAccessUser"),
                //extract the collection base on currentAccessUser
                Aggregation.unwind("currentAccessUser"),
                //finding currentAccessUser base on  user login
                Aggregation.match(Criteria.where("currentAccessUser._id").is(new ObjectId(authenticationProfileModel.get_id()))),
                //set up project property
                aggregationOperationContext -> new Document("$project",
                        new Document("users._id", "$userDetails._id")
                                .append("users.username", "$userDetails.username")
                                .append("users.access", "$users.access")
                                .append("currentAccessUser.username", "$currentAccessUser.username")
                                .append("currentAccessUser.access", "$users.access")
                                .append("title", "$title")
                                .append("description", "$description")
                                .append("swagger", "$swagger")
                                .append("updatedBy", "$updatedBy")
                                .append("dateUpdated", "$updatedDate")
                ),
                //grouping all finding
                aggregationOperationContext -> new Document("$group",
                        new Document("_id", "$_id")
                                .append("title", new Document("$first", "$title"))
                                .append("description", new Document("$first", "$description"))
                                .append("spec", new Document("$first", "$swagger"))
                                .append("updatedBy", new Document("$first", "$updatedBy"))
                                .append("dateUpdated", new Document("$first", "$dateUpdated"))
                                .append("currentAccessUser", new Document("$first", "$currentAccessUser"))

                ));
        return mongoTemplate.aggregate(aggregation, "mockup", DtoMockupDetailVmodel.class).getMappedResults();
    }

    @Override
    public List<DtoMockUserLookupVmodel> getUsersMock(String mockId) {
        Aggregation aggregation = Aggregation.newAggregation(
                //find the collection base on id
                Aggregation.match(Criteria.where("_id").is(mockId)),
                //extract the collection base on users
                Aggregation.unwind("users"),
                //setup project fields and convert users.userId to object id
                Aggregation.project().and("updatedDate").as("updatedDate").and("users.access")
                        .as("users.access").andExpression("toObjectId('$users.userId')").as("users.userId"),
                //join collection with user entities
                LookupOperation.newLookup().from("userEntities").localField("users.userId").foreignField("_id").as("userDetails"),
                //extract the collection
                Aggregation.unwind("userDetails"),
                //setup project property
                aggregationOperationContext -> new Document("$project",
                        new Document("users._id", "$userDetails._id")
                                .append("users.username", "$userDetails.username")
                                .append("users.access", "$users.access")
                                .append("users.updatedDate", "$updatedDate")),
                //grouping all component
                aggregationOperationContext -> new Document("$group",
                        new Document("_id", "$_id")
                                .append("users", new Document("$push", "$users")))

        );
        return mongoTemplate.aggregate(aggregation, "mockup", DtoMockUserLookupVmodel.class).getMappedResults();
    }

    @Override
    public Object addUserAccessOnMock(String id, AddUserAccessVmodel vmodel, AuthenticationProfileModel authenticationProfileModel) {
        return null;
    }

    @Override
    public MockEntitiesPage paging(Pageable pageable, String q, AuthenticationProfileModel authenticationProfileModel) {
        MockEntitiesPage basePage = new MockEntitiesPage();
        basePage.setConnection(mongoTemplate);
        //add search query param
        basePage.addCriteria(q);
        //selected field
        basePage.getQuery().fields().include("_id").include("title").include("description").include("users").include("updatedDate");
        //add additional criteria or custom criteria
        basePage.addAdditionalCriteria(Criteria.where("users").elemMatch(Criteria.where("userId").is(authenticationProfileModel.get_id())));
        basePage.setPageable(pageable).build(MockEntities.class);
        return basePage;
    }

    @Override
    public List<MockHistoryEntities> getMockHistories(String id) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "_id")).addCriteria(Criteria.where("mockId").is(id))
                .fields().include("swagger").include("mockId").include("updatedDate").include("updatedBy");
        return mongoTemplate.find(query, MockHistoryEntities.class);
    }

    @Override
    public MockEntities getUserMocks(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id)).fields().include("_id").include("users");
        return mongoTemplate.findOne(query, MockEntities.class);
    }

    @Override
    public Object removeAccessUserOnMock(String id, String userId, MockEntities mockEntities) throws NotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        AtomicBoolean addUserExist = new AtomicBoolean(false);
        mockEntities.getUsers().forEach(userMocksEntities -> {
            if (userMocksEntities.getUserId().equals(userId)) {
                addUserExist.set(true);
            }
        });
        if (!addUserExist.get()) {
            throw new NotFoundException(ResponseCode.USER_NOT_FOUND);
        }
        Update update = new Update();
        update.pull("users", new Document("userId", userId));
        return mongoTemplate.updateFirst(query, update, MockEntities.class);
    }

    @Override
    public Object addUserToMock(String id, AddUserAccessVmodel accessVmodel, MockEntities mockEntities) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        AtomicBoolean addUserExist = new AtomicBoolean(false);
        mockEntities.getUsers().forEach(userMocksEntities -> {
            if (userMocksEntities.getUserId().equals(accessVmodel.getUserId())) {
                addUserExist.set(true);
            }
        });
        Update update = new Update();
        if (addUserExist.get()) {
            query.addCriteria(Criteria.where("users").elemMatch(Criteria.where("userId").is(accessVmodel.getUserId())));
            update.set("users.$.access", accessVmodel.getAccess());
        } else
            update.push("users", new Document("userId", accessVmodel.getUserId()).append("access", accessVmodel.getAccess()));
        return mongoTemplate.updateFirst(query, update, MockEntities.class);
    }

    @Override
    public List<MockEntities> findMockByIdAndUserId(String id, String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        query.addCriteria(Criteria.where("users").elemMatch(Criteria.where("userId").is(userId)));
        return mongoTemplate.find(query, MockEntities.class);
    }
}
