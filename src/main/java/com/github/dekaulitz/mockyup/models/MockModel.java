package com.github.dekaulitz.mockyup.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.db.entities.MockCreatorEntities;
import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.MockHistoryEntities;
import com.github.dekaulitz.mockyup.db.repositories.MockHistoryRepository;
import com.github.dekaulitz.mockyup.db.repositories.MockRepository;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.errorhandlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.utils.Role;
import com.github.dekaulitz.mockyup.vmodels.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
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
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MockModel extends BaseMockModel<MockEntities, MockVmodel> {

    @Autowired
    private final MockRepository mockRepository;

    @Autowired
    private final MockHistoryRepository mockHistoryRepository;

    @Autowired
    private final MongoTemplate mongoTemplate;

    public MockModel(MockRepository mockRepository, MockHistoryRepository mockHistoryRepository, MongoTemplate mongoTemplate) {
        this.mockRepository = mockRepository;
        this.mockHistoryRepository = mockHistoryRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<MockEntities> all() {
        return this.mockRepository.findAll();
    }

    @Override
    public MockEntities getById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        AtomicReference<Boolean> hasAccessToSee = new AtomicReference<>(false);
        mockEntities.get().getUsers().forEach(userMocksEntities -> {
            if (userMocksEntities.getUserId().equals(authenticationProfileModel.get_id())) {
                hasAccessToSee.set(true);
            }
        });
        if (!hasAccessToSee.get()) throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);
        return mockEntities.get();
    }

    @Override
    public MockEntities save(MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws InvalidMockException {
        try {
            MockEntities mockEntities = new MockEntities();
            this.setSaveMockEntity(view, mockEntities, authenticationProfileModel);
            mockEntities.setUpdatedBy(MockCreatorEntities.builder().userId(authenticationProfileModel.get_id()).username(authenticationProfileModel.getUsername()).build());
            mockEntities.setUpdatedDate(new Date());
            return this.mockRepository.save(mockEntities);
        } catch (Exception e) {
            throw new InvalidMockException(ResponseCode.INVALID_MOCKUP_STRUCTURE, e);
        }
    }

    @Override
    public MockEntities updateByID(String id, MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException, InvalidMockException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        this.checkAccessModificationMocks(mockEntities.get(), authenticationProfileModel);
        this.saveMockToHistory(mockEntities.get());
        this.setUpdateMockEntity(view, mockEntities.get(), authenticationProfileModel);
        mockEntities.get().setUpdatedBy(MockCreatorEntities.builder().userId(authenticationProfileModel.get_id()).username(authenticationProfileModel.getUsername()).build());
        mockEntities.get().setUpdatedDate(new Date());
        mockRepository.save(mockEntities.get());
        return mockEntities.get();
    }

    @Override
    public void deleteById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        this.checkAccessModificationMocks(mockEntities.get(), authenticationProfileModel);
        mockRepository.deleteById(mockEntities.get().getId());
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

    public List<MockHistoryEntities> getMockHistories(String id) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "_id")).addCriteria(Criteria.where("mockId").is(id)).fields().include("swagger").include("mockId").include("updatedDate").include("updatedBy");
        return mongoTemplate.find(query, MockHistoryEntities.class);
    }


    public MockEntities getUserMocks(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id)).fields().include("_id").include("users");
        return mongoTemplate.findOne(query, MockEntities.class);
    }

    public List<DtoMockLookupVmodel> getUsersListOfMocks(String mockId) {
        Aggregation aggregation = Aggregation.newAggregation(
                //find the collection base on id
                Aggregation.match(Criteria.where("_id").is(mockId)),
                //extract the collection base on users
                Aggregation.unwind("users"),
                //setup project fields and convert users.userId to object id
                Aggregation.project().and("updatedDate").as("updatedDate").and("users.access").as("users.access").andExpression("toObjectId('$users.userId')").as("users.userId"),
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
        return mongoTemplate.aggregate(aggregation, "mockup", DtoMockLookupVmodel.class).getMappedResults();
    }

    public List<DtoMockupDetailVmodel> getDetailMockUpIdByUserAccess(String id, AuthenticationProfileModel authenticationProfileModel) {
        Aggregation aggregation = Aggregation.newAggregation(
                //find by criteria
                Aggregation.match(Criteria.where("_id").is(id)),
                //extract users array as list
                Aggregation.unwind("users"),
                //set project field
                Aggregation.project().and("title").as("title").and("swagger").as("swagger").and("description").as("description").and("updatedBy").as("updatedBy")
                        //convert $users.userId to objectId for querying the user id
                        .and("updatedDate").as("updatedDate").and("users.access").as("users.access").andExpression("toObjectId('$users.userId')").as("users.userId"),
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

    public MockHelper getMockMocking(HttpServletRequest request, String path, String id, String body) throws NotFoundException, JsonProcessingException, UnsupportedEncodingException, InvalidMockException {
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent())
            throw new NotFoundException("data not found");
        String[] originalPathUri = path.split("\\?");
        String[] paths = originalPathUri[0].split("/");
        OpenAPI openAPI = JsonMapper.mapper().readValue(mockEntities.get().getSpec(), OpenAPI.class);
        //pathitem is exist or without path parameter clean url
        PathItem item = openAPI.getPaths().get(path);
        if (item != null) {
            String[] openAPIPaths = path.split("/");
            return getMockResponse(item, request, body, openAPIPaths, paths, openAPI.getComponents());
        }
        //path item with path parameter we should check every endpoint that related with data
        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String s = entry.getKey();
            PathItem pathItem = entry.getValue();
            String[] openAPIPaths = s.split("/");
            String[] regexPath = new String[paths.length];
            // every targeting mocks uri should has same length
            if (openAPIPaths.length == paths.length) {
                // converting request path to original for searching data
                for (int i = 0; i < openAPIPaths.length; i++) {
                    //checking path parameter
                    if (!openAPIPaths[i].equals(paths[i])) {
                        if (openAPIPaths[i].contains("*")) {
                            regexPath[i] = openAPIPaths[i];
                        }
                    } else {
                        //this is for checking wildcard with path parameter
                        if (!paths[i].contains("*") && openAPIPaths[i].equals(paths[i])) {
                            regexPath[i] = openAPIPaths[i];
                        }
                    }
                }
                //check if current openapi path equal with regexpath (path)
                if (s.equals(String.join("/", regexPath)))
                    return getMockResponse(pathItem, request, body, openAPIPaths, paths, openAPI.getComponents());
            }
        }
        return null;
    }

    public Object addUserAccessOnMock(String id, AddUserAccessVmodel vmodel, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        this.checkAccessModificationMocks(mockEntities.get(), authenticationProfileModel);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        AtomicBoolean addUserExist = new AtomicBoolean(false);
        mockEntities.get().getUsers().forEach(userMocksEntities -> {
            if (userMocksEntities.getUserId().equals(vmodel.getUserId())) {
                addUserExist.set(true);
            }
        });
        Update update = new Update();
        if (addUserExist.get()) {
            query.addCriteria(Criteria.where("users").elemMatch(Criteria.where("userId").is(vmodel.getUserId())));
            update.set("users.$.access", vmodel.getAccess());
        } else
            update.push("users", new Document("userId", vmodel.getUserId()).append("access", vmodel.getAccess()));
        return mongoTemplate.updateFirst(query, update, MockEntities.class);
    }

    public Object removeAccessUserOnMock(String id, String userId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        this.checkAccessModificationMocks(mockEntities.get(), authenticationProfileModel);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        AtomicBoolean addUserExist = new AtomicBoolean(false);
        mockEntities.get().getUsers().forEach(userMocksEntities -> {
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
     * @param id
     * @param historyId
     * @param authenticationProfileModel
     * @return
     * @throws NotFoundException
     * @
     */
    public DtoMockupHistoryVmodel geMockHistoryId(String id, String historyId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        query.addCriteria(Criteria.where("users").elemMatch(Criteria.where("userId").is(authenticationProfileModel.get_id())));
        List<MockEntities> mockEntitiesList = mongoTemplate.find(query, MockEntities.class);
        if (mockEntitiesList.isEmpty())
            throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);
        Query queryHistory = new Query();
        queryHistory.addCriteria(Criteria.where("mockId").is(id).and("id").is(new ObjectId(historyId)));
        queryHistory.fields().include("mockId").include("swagger");
        List<DtoMockupHistoryVmodel> mockHistoryEntitiesList = mongoTemplate.find(queryHistory, DtoMockupHistoryVmodel.class, "mockup_histories");
        if (mockHistoryEntitiesList.isEmpty())
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        return mockHistoryEntitiesList.get(0);
    }

    /**
     * @param mockEntities
     * @desc save to mock history
     */
    private void saveMockToHistory(MockEntities mockEntities) {
        MockHistoryEntities mockHistoryEntities = new MockHistoryEntities();
        mockHistoryEntities.setMockId(mockEntities.getId());
        mockHistoryEntities.setUpdatedDate(mockEntities.getUpdatedDate());
        mockHistoryEntities.setSpec(mockEntities.getSpec());
        mockHistoryEntities.setTitle(mockEntities.getTitle());
        mockHistoryEntities.setDescription(mockEntities.getDescription());
        mockHistoryEntities.setSwagger(mockEntities.getSwagger());
        mockHistoryEntities.setUsers(mockEntities.getUsers());
        mockHistoryEntities.setUpdatedBy(mockEntities.getUpdatedBy());
        mockHistoryEntities.setUpdatedDate(mockEntities.getUpdatedDate());
        this.mockHistoryRepository.save(mockHistoryEntities);

    }

    /**
     * @param mockEntities
     * @param authenticationProfileModel
     * @desc check current user login has access to modified the mocks
     */
    private void checkAccessModificationMocks(MockEntities mockEntities, AuthenticationProfileModel authenticationProfileModel) {
        mockEntities.getUsers().forEach(userMocksEntities -> {
            if (userMocksEntities.getUserId().equals(authenticationProfileModel.get_id()) && !userMocksEntities.getAccess().equals(Role.MOCKS_READ_WRITE.name())) {
                throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);
            }
        });
    }


}
