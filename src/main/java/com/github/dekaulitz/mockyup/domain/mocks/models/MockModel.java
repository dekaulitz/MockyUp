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

    public MockModel(MockRepository mockRepository, MockHistoryRepository mockHistoryRepository) {
        this.mockRepository = mockRepository;
        this.mockHistoryRepository = mockHistoryRepository;
    }

    /**
     * get mock detail
     *
     * @param id                         {@link String} id from mock collection
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile
     * @return MockEntities mock data structure
     * @throws NotFoundException when id not found from mock collection
     */
    @Override
    public MockEntities getById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        //check access permission base on users with auth profile
        AtomicReference<Boolean> hasAccessToSee = new AtomicReference<>(false);
        mockEntities.get().getUsers().forEach(userMocksEntities -> {
            if (userMocksEntities.getUserId().equals(authenticationProfileModel.get_id())) {
                hasAccessToSee.set(true);
            }
        });
        if (!hasAccessToSee.get()) throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);
        return mockEntities.get();
    }

    /**
     * @param view                       {@link MockVmodel}
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile
     * @return MockEntities mock data
     * @throws InvalidMockException when if something bad happen when injecting view into mock data
     */
    @Override
    public MockEntities save(MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws InvalidMockException {
        try {
            MockEntities mockEntities = new MockEntities();
            //parsing view and authenticationProfileModel into mockentities
            this.setSaveMockEntity(view, mockEntities, authenticationProfileModel);
            mockEntities.setUpdatedBy(MockCreatorEntities.builder().userId(authenticationProfileModel.get_id())
                    .username(authenticationProfileModel.getUsername()).build());
            mockEntities.setUpdatedDate(new Date());
            return this.mockRepository.save(mockEntities);
        } catch (Exception e) {
            throw new InvalidMockException(ResponseCode.INVALID_MOCKUP_STRUCTURE, e);
        }
    }

    /**
     * @param id                         {@link String} id from mock colelction
     * @param view                       {@link MockVmodel} mock data that will be update
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile for validating the user access
     * @return MockEntities mock data
     * @throws NotFoundException    when the mock not found
     * @throws InvalidMockException when something band happen when injecting when mock data into mock entities
     */
    @Override
    public MockEntities updateByID(String id, MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException, InvalidMockException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        //check access for modification base on mockEntities
        this.checkAccessModification(mockEntities.get(), authenticationProfileModel);

        this.saveMockToHistory(mockEntities.get());
        this.setUpdateMockEntity(view, mockEntities.get());
        mockEntities.get().setUpdatedBy(MockCreatorEntities.builder().userId(authenticationProfileModel.get_id())
                .username(authenticationProfileModel.getUsername()).build());
        mockEntities.get().setUpdatedDate(new Date());
        mockRepository.save(mockEntities.get());
        return mockEntities.get();
    }

    /**
     * @param id                         {@link String} id from mock collection
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile for validating user access
     * @throws NotFoundException when mock not found will throw the exception
     */
    @Override
    public void deleteById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        //check access for modification base on mockEntities
        this.checkAccessModification(mockEntities.get(), authenticationProfileModel);
        mockRepository.deleteById(mockEntities.get().getId());
        mockHistoryRepository.deleteAllByMockId(mockEntities.get().getId());
    }

    /**
     * @param pageable                   {@link Pageable} spring data pagiable
     * @param q                          {@link String}query data example q=name:fahmi => meaning field name with value fahmi
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile for getting the list of mocks that has access
     * @return MockEntitiesPage
     */
    @Override
    public MockEntitiesPage paging(Pageable pageable, String q, AuthenticationProfileModel authenticationProfileModel) {
        return this.mockRepository.paging(pageable, q, authenticationProfileModel);
    }

    /**
     * get mock history base on mock id
     *
     * @param id {@link String} id from mock collection
     * @return List<MockHistoryEntities> list mock history base on id from mock collection
     */
    @Override
    public List<MockHistoryEntities> getMockHistories(String id) {
        return this.mockRepository.getMockHistories(id);
    }

    /**
     * get all user that has access for mocks
     *
     * @param mockId id from mock collection
     * @return List<DtoMockUserLookupVmodel>
     */
    @Override
    public List<DtoMockUserLookupVmodel> getUsersListOfMocks(String mockId) {
        return this.mockRepository.getUsersMock(mockId);
    }

    /**
     * @param id                         {@link String}id from mock collection
     * @param authenticationProfileModel {@link AuthenticationProfileModel} get detail from auth profile
     * @return List<DtoMockupDetailVmodel> mock detail with current access
     */
    @Override
    public List<DtoMockupDetailVmodel> getDetailMockUpIdByUserAccess(String id, AuthenticationProfileModel authenticationProfileModel) {
        return this.mockRepository.getMockDetailWithCurrentAccess(id, authenticationProfileModel);
    }

    /**
     * for mocking the mock response
     *
     * @param request {@link HttpServletRequest} request from servlet for geting header request attributes
     * @param path    {@link String} the path that will expect the mock response
     * @param id      {@link String} id from mock collection
     * @param body    {@link String} if the contract need some body payload request
     * @return MockHelper {@link MockHelper} the response from mock response
     * @throws NotFoundException            when the id is not found will throw the exception
     * @throws JsonProcessingException      when the contract is not valid
     * @throws UnsupportedEncodingException when the contract is not valid structure
     * @throws InvalidMockException         when the contract is not valid with the request
     */
    @Override
    public MockHelper getMockMocking(HttpServletRequest request, String path, String id, String body) throws NotFoundException, JsonProcessingException, UnsupportedEncodingException, InvalidMockException {
        //find the collection base on mock id
        Optional<MockEntities> mockEntities = this.mockRepository.findById(id);
        if (!mockEntities.isPresent())
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);

        //extract the request path of mocks [full_url]?path=/[contract_path_that_want_to_mock]
        String[] originalPathUri = path.split("\\?");

        //extract the the path [contract_path_that_want_to_mock]
        //will checking per segment for mapping with [swagger_path_contract]
        String[] paths = originalPathUri[0].split("/");

        //parsing mock spec into OpenAPI
        OpenAPI openAPI = JsonMapper.mapper().readValue(mockEntities.get().getSpec(), OpenAPI.class);

        //first check with the path if they exist will rendering the response for the firstime
        PathItem item = openAPI.getPaths().get(path);
        if (item != null) {
            String[] openAPIPaths = path.split("/");
            return renderingMockResponse(item, request, body, openAPIPaths, paths, openAPI.getComponents());
        }

        //will validate paths into OpenAPI pathItems it will validate and mapping per segment
        return validatePathWithOpenApiPaths(request, body, openAPI, paths);
    }

    /**
     * add user to mock
     *
     * @param id                         {@link String} id from mock collection
     * @param vmodel                     {@link AddUserAccessVmodel} user access
     * @param authenticationProfileModel {@link AuthenticationProfileModel} current auth profile access
     * @return Object {@link com.mongodb.client.result.UpdateResult} response mongo client
     * @throws NotFoundException when the mock was not found
     */
    @Override
    public Object addUserToMock(String id, AddUserAccessVmodel vmodel, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        //check access for modification base on mockEntities
        this.checkAccessModification(mockEntities.get(), authenticationProfileModel);
        return this.mockRepository.registeringUserToMock(id, vmodel, mockEntities.get());
    }

    /**
     * remove user from mock
     *
     * @param id                         {@link String} id from mock collection
     * @param userId                     {@link String} id from
     * @param authenticationProfileModel {@link AuthenticationProfileModel} user auth data
     * @return Object the response is command mongodb
     * @throws NotFoundException if the id of mock collection was not found
     */
    @Override
    public Object removeAccessUserOnMock(String id, String userId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        Optional<MockEntities> mockEntities = mockRepository.findById(id);
        if (!mockEntities.isPresent()) {
            throw new NotFoundException(ResponseCode.MOCKUP_NOT_FOUND);
        }
        //check access for modification base on mockEntities
        this.checkAccessModification(mockEntities.get(), authenticationProfileModel);
        return this.mockRepository.removeAccessUserOnMock(id, userId, mockEntities.get());
    }

    /**
     * get spec mock history
     *
     * @param id                         {@link String} id from mock collection
     * @param historyId                  {@link String} id from mock hisotory collection
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile
     * @return DtoMockupHistoryVmodel swagger contract
     * @throws NotFoundException when id of mock collection was not found or history id is not found
     */
    @Override
    public DtoMockupHistoryVmodel geMockHistoryId(String id, String historyId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException {
        //check access permission user on mock
        MockEntities mockEntitiesList = this.mockRepository.checkMockUserAccessPermission(id, authenticationProfileModel.get_id());
        if (mockEntitiesList == null)
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
    private void checkAccessModification(MockEntities mockEntities, AuthenticationProfileModel authenticationProfileModel) {
        mockEntities.getUsers().forEach(userMocksEntities -> {
            if (userMocksEntities.getUserId().equals(authenticationProfileModel.get_id())
                    && !userMocksEntities.getAccess().equals(Role.MOCKS_READ_WRITE.name())) {
                throw new UnathorizedAccess(ResponseCode.INVALID_ACCESS_PERMISSION);
            }
        });
    }

    /**
     * @param request            {@link HttpServletRequest} request from servlet for geting header request attributes
     * @param body               {@link String} if the contract need some body payload request
     * @param openAPI            {@link OpenAPI}
     * @param extractPathRequest {@link String[]}  extract result from [contract_path_that_want_to_mock]
     * @return MockHelper
     * @throws UnsupportedEncodingException
     * @throws InvalidMockException
     * @throws NotFoundException
     * @throws JsonProcessingException
     */
    private MockHelper validatePathWithOpenApiPaths(HttpServletRequest request, String body, OpenAPI openAPI, String[] extractPathRequest)
            throws UnsupportedEncodingException, InvalidMockException, NotFoundException, JsonProcessingException {
        //iterate all pathitem from openApi
        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            //get path route from pathItem from openapi path
            String s = entry.getKey();
            //get pathitem object
            PathItem pathItem = entry.getValue();

            //extracting the openapi path route that will mapped with extract request path
            String[] openAPIPaths = s.split("/");

            //add the limitation for avoiding bounding array index
            String[] regexPath = new String[extractPathRequest.length];

            //validate length segment from [contract_path_that_want_to_mock] with openapi path route
            //the aim for avoid unecessary unmatch openapi route path checking
            if (openAPIPaths.length == extractPathRequest.length) {

                // mapping segment per segment from openapi pathroute extractiong with [contract_path_that_want_to_mock] extraction
                for (int i = 0; i < openAPIPaths.length; i++) {
                    //checking if  openapi api path route is paramater
                    if (!openAPIPaths[i].equals(extractPathRequest[i])) {
                        if (openAPIPaths[i].contains("*")) {
                            regexPath[i] = openAPIPaths[i];
                        }
                    } else {
                        //this is for checking wildcard with path parameter
                        if (!extractPathRequest[i].contains("*") && openAPIPaths[i].equals(extractPathRequest[i])) {
                            regexPath[i] = openAPIPaths[i];
                        }
                    }
                }
                //check if current openapi path equal with [contract_path_that_want_to_mock] and break the loop
                if (s.equals(String.join("/", regexPath)))
                    return renderingMockResponse(pathItem, request, body, openAPIPaths, extractPathRequest, openAPI.getComponents());
            }
        }
        return null;
    }
}
