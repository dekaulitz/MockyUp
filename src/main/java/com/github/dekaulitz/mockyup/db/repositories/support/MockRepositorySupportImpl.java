package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.db.entities.*;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.utils.Role;
import com.mongodb.client.result.UpdateResult;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MockRepositorySupportImpl implements MockRepositorySupport {
    @Autowired
    private final MongoTemplate mongoTemplate;

    public MockRepositorySupportImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * get mock detail with current access
     *
     * @param id                         {@link String} id from mock collection
     * @param authenticationProfileModel {@link AuthenticationProfileModel} user auth data
     * @return List<DtoMockupDetailVmodel>
     */
    @Override
    public List<DtoMockupDetailVmodel> getMockDetailWithCurrentAccess(String id, AuthenticationProfileModel authenticationProfileModel) {
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

    /**
     * get users mock that has access
     *
     * @param id {@link String} id from mock collection
     * @return List<DtoMockUserLookupVmodel>
     */
    @Override
    public List<DtoMockUserLookupVmodel> getUsersMock(String id) {
        Aggregation aggregation = Aggregation.newAggregation(
                //find the collection base on id
                Aggregation.match(Criteria.where("_id").is(id)),
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

    /**
     * paging mock data
     *
     * @param pageable                   {@link Pageable} Spring data pageable
     * @param q                          {@link String} query data like example q=firstname:fahmi mean field firstname with value fahmi
     * @param authenticationProfileModel {@link AuthenticationProfileModel} user auth data
     * @return MockEntitiesPage mockentities page
     */
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

    /**
     * get mock histories
     *
     * @param id {@link String}id from mock colelction
     * @return List<MockHistoryEntities>
     */
    @Override
    public List<MockHistoryEntities> getMockHistories(String id) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "_id")).addCriteria(Criteria.where("mockId").is(id))
                .fields().include("swagger").include("mockId").include("updatedDate").include("updatedBy");
        return mongoTemplate.find(query, MockHistoryEntities.class);
    }

    /**
     * @param id           {@link String} id from mock collection
     * @param userId       {@link String} id from user collection
     * @param mockEntities {@link MockEntities} mockentitites page
     * @return Object {@link UpdateResult} response mongo client
     * @throws NotFoundException
     */
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

    /**
     * registering user to mock
     *
     * @param id           {@link String} id from mock collection
     * @param accessVmodel {@link AddUserAccessVmodel}access data user that want to register
     * @param mockEntities {@link MockEntities} mock enttitis data for checking user entities is already exist for modification
     * @return Object {@link UpdateResult} response mongo client
     */
    @Override
    public Object registeringUserToMock(String id, AddUserAccessVmodel accessVmodel, MockEntities mockEntities) {
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

    /**
     * check user access permision on mock
     *
     * @param id     {@link String} id from mock collection
     * @param userId {@link String} id from user colelction
     * @return List<MockEntities>
     */
    @Override
    public MockEntities checkMockUserAccessPermission(String id, String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        query.addCriteria(Criteria.where("users").elemMatch(Criteria.where("userId").is(userId)));
        return mongoTemplate.findOne(query, MockEntities.class);
    }

    /**
     * injecting user root for the firstime
     *
     * @param user user root
     */
    @Override
    public void injectRootIntoAllMocks(UserEntities user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("users").is(null));
        List<MockEntities> mocksHasNoUsers = mongoTemplate.find(query, MockEntities.class);
        if (mocksHasNoUsers.size() > 0)
            mocksHasNoUsers.forEach(mockEntities -> {
                List<UserMocksEntities> userMocksEntitiesList = new ArrayList<>();
                userMocksEntitiesList.add(UserMocksEntities.builder()
                        .userId(user.getId())
                        .access(Role.MOCKS_READ_WRITE.toString())
                        .build());
                MockEntities updateMockenties = MockEntities.builder()
                        .id(mockEntities.getId())
                        .description(mockEntities.getDescription())
                        .title(mockEntities.getTitle())
                        .spec(mockEntities.getSpec())
                        .swagger(mockEntities.getSwagger())
                        .updatedDate(new Date())
                        .users(userMocksEntitiesList)
                        .updatedBy(MockCreatorEntities.builder().userId(user.getId()).username(user.getUsername()).build())
                        .build();
                mongoTemplate.save(updateMockenties);
            });
    }
}
