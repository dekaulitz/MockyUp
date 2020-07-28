package com.github.dekaulitz.mockyup.domain.mocks.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.db.entities.MockCreatorEntities;
import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.MockHistoryEntities;
import com.github.dekaulitz.mockyup.db.repositories.MockHistoryRepository;
import com.github.dekaulitz.mockyup.db.repositories.MockRepository;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.base.BaseMockModel;
import com.github.dekaulitz.mockyup.domain.mocks.base.MockInterface;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupHistoryVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.InvalidMockException;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.utils.Role;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MockModel extends BaseMockModel implements MockInterface {

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
            mockEntities.setUpdatedBy(MockCreatorEntities.builder().userId(authenticationProfileModel.get_id())
                    .username(authenticationProfileModel.getUsername()).build());
            mockEntities.setUpdatedDate(new Date());
            return this.mockRepository.save(mockEntities);
        } catch (Exception e) {
            throw new InvalidMockException(ResponseCode.INVALID_MOCKUP_STRUCTURE, e);
        }
    }

    @Override
    public MockEntities updateByID(String id, MockVmodel view, AuthenticationProfileModel authenticationProfileModel)
            throws NotFoundException, InvalidMockException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        this.checkAccessModificationMocks(mockEntities.get(), authenticationProfileModel);
        this.saveMockToHistory(mockEntities.get());
        this.setUpdateMockEntity(view, mockEntities.get());
        mockEntities.get().setUpdatedBy(MockCreatorEntities.builder().userId(authenticationProfileModel.get_id())
                .username(authenticationProfileModel.getUsername()).build());
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
        mockHistoryRepository.deleteAllByMockId(mockEntities.get().getId());
    }

    @Override
    public MockEntitiesPage paging(Pageable pageable, String q, AuthenticationProfileModel authenticationProfileModel) {
        return this.mockRepository.paging(pageable, q, authenticationProfileModel);
    }

    @Override
    public List<MockHistoryEntities> getMockHistories(String id) {
        return this.mockRepository.getMockHistories(id);
    }


    @Override
    public List<DtoMockUserLookupVmodel> getUsersListOfMocks(String mockId) {
        return this.mockRepository.getUsersMock(mockId);
    }

    @Override
    public List<DtoMockupDetailVmodel> getDetailMockUpIdByUserAccess(String id, AuthenticationProfileModel authenticationProfileModel) {
        return this.mockRepository.getDetailMockUpIdByUserAccess(id, authenticationProfileModel);
    }

    @Override
    public MockHelper getMockMocking(HttpServletRequest request, String path, String id, String body) throws NotFoundException,
            JsonProcessingException, UnsupportedEncodingException, InvalidMockException {
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent())
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
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
        return checkOpenApiPathWithPath(request, body, openAPI, paths);
    }

    @Override
    public Object addUserAccessOnMock(String id, AddUserAccessVmodel vmodel, AuthenticationProfileModel authenticationProfileModel)
            throws NotFoundException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        this.checkAccessModificationMocks(mockEntities.get(), authenticationProfileModel);
        return this.mockRepository.addUserToMock(id, vmodel, mockEntities.get());
    }

    @Override
    public Object removeAccessUserOnMock(String id, String userId, AuthenticationProfileModel authenticationProfileModel)
            throws NotFoundException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        this.checkAccessModificationMocks(mockEntities.get(), authenticationProfileModel);
        return this.mockRepository.removeAccessUserOnMock(id, userId, mockEntities.get());
    }

    /**
     * @param id
     * @param historyId
     * @param authenticationProfileModel
     * @return
     * @throws NotFoundException
     * @
     */
    @Override
    public DtoMockupHistoryVmodel geMockHistoryId(String id, String historyId, AuthenticationProfileModel authenticationProfileModel)
            throws NotFoundException {
        List<MockEntities> mockEntitiesList = this.mockRepository.findMockByIdAndUserId(id, authenticationProfileModel.get_id());
        if (mockEntitiesList.isEmpty())
            throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);

        DtoMockupHistoryVmodel mockHistoryEntitiesList = this.mockHistoryRepository.getMockHistoryByIdAndMockId(id, historyId);
        if (mockHistoryEntitiesList == null)
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);

        return mockHistoryEntitiesList;
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
            if (userMocksEntities.getUserId().equals(authenticationProfileModel.get_id())
                    && !userMocksEntities.getAccess().equals(Role.MOCKS_READ_WRITE.name())) {
                throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);
            }
        });
    }

    private MockHelper checkOpenApiPathWithPath(HttpServletRequest request, String body, OpenAPI openAPI, String[] paths)
            throws UnsupportedEncodingException, InvalidMockException, NotFoundException, JsonProcessingException {
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
                //check if current openapi path equal with regexpath (path) and break the loop
                if (s.equals(String.join("/", regexPath)))
                    return getMockResponse(pathItem, request, body, openAPIPaths, paths, openAPI.getComponents());
            }
        }
        return null;
    }
}
