package com.github.dekaulitz.mockyup.domain.mocks.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.MockHistoryEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupHistoryVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.InvalidMockException;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface MockInterface {
    /**
     * get mock detail
     *
     * @param id                         {@link String} id from mock collection
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile
     * @return MockEntities mock data structure
     * @throws NotFoundException when id not found from mock collection
     */
    MockEntities getById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    /**
     * @param view                       {@link MockVmodel}
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile
     * @return MockEntities mock data
     * @throws InvalidMockException when if something bad happen when injecting view into mock data
     */
    MockEntities save(MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws InvalidMockException;

    /**
     * @param id                         {@link String} id from mock colelction
     * @param view                       {@link MockVmodel} mock data that will be update
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile for validating the user access
     * @return MockEntities mock data
     * @throws NotFoundException    when the mock not found
     * @throws InvalidMockException when something band happen when injecting when mock data into mock entities
     */
    MockEntities updateByID(String id, MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException, InvalidMockException;

    /**
     * @param id                         {@link String} id from mock collection
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile for validating user access
     * @throws NotFoundException when mock not found will throw the exception
     */
    void deleteById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    /**
     * @param pageable                   {@link Pageable} spring data pagiable
     * @param q                          {@link String}query data example q=name:fahmi => meaning field name with value fahmi
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile for getting the list of mocks that has access
     * @return MockEntitiesPage
     */
    MockEntitiesPage paging(Pageable pageable, String q, AuthenticationProfileModel authenticationProfileModel);

    /**
     * get mock history base on mock id
     *
     * @param id {@link String} id from mock collection
     * @return List<MockHistoryEntities> list mock history base on id from mock collection
     */
    List<MockHistoryEntities> getMockHistories(String id);

    /**
     * get all user that has access for mocks
     *
     * @param mockId id from mock collection
     * @return List<DtoMockUserLookupVmodel>
     */
    List<DtoMockUserLookupVmodel> getUsersListOfMocks(String mockId);

    /**
     * @param id                         {@link String}id from mock collection
     * @param authenticationProfileModel {@link AuthenticationProfileModel} get detail from auth profile
     * @return List<DtoMockupDetailVmodel> mock detail with current access
     */
    List<DtoMockupDetailVmodel> getDetailMockUpIdByUserAccess(String id, AuthenticationProfileModel authenticationProfileModel);

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
    MockHelper getMockMocking(HttpServletRequest request, String path, String id, String body) throws NotFoundException, JsonProcessingException, UnsupportedEncodingException, InvalidMockException;

    /**
     * add user to mock
     *
     * @param id                         {@link String} id from mock collection
     * @param vmodel                     {@link AddUserAccessVmodel} user access
     * @param authenticationProfileModel {@link AuthenticationProfileModel} current auth profile access
     * @return Object {@link com.mongodb.client.result.UpdateResult} response mongo client
     * @throws NotFoundException when the mock was not found
     */
    Object addUserToMock(String id, AddUserAccessVmodel vmodel, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    /**
     * remove user from mock
     *
     * @param id                         {@link String} id from mock collection
     * @param userId                     {@link String} id from
     * @param authenticationProfileModel {@link AuthenticationProfileModel} user auth data
     * @return Object {@link com.mongodb.client.result.UpdateResult} response mongo client
     * @throws NotFoundException if the id of mock collection was not found
     */
    Object removeAccessUserOnMock(String id, String userId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    /**
     * get spec mock history
     *
     * @param id                         {@link String} id from mock collection
     * @param historyId                  {@link String} id from mock hisotory collection
     * @param authenticationProfileModel {@link AuthenticationProfileModel} auth profile
     * @return DtoMockupHistoryVmodel swagger contract
     * @throws NotFoundException when id of mock collection was not found or history id is not found
     */
    DtoMockupHistoryVmodel geMockHistoryId(String id, String historyId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;
}
