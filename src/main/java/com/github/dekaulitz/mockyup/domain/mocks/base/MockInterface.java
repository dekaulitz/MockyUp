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
     * @param id
     * @param authenticationProfileModel
     * @return
     * @throws NotFoundException
     */
    MockEntities getById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    /**
     * @param view
     * @param authenticationProfileModel
     * @return
     * @throws InvalidMockException
     */
    MockEntities save(MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws InvalidMockException;

    /**
     * @param id
     * @param view
     * @param authenticationProfileModel
     * @return
     * @throws NotFoundException
     * @throws InvalidMockException
     */
    MockEntities updateByID(String id, MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException, InvalidMockException;

    /**
     * @param id
     * @param authenticationProfileModel
     * @throws NotFoundException
     */
    void deleteById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    /**
     * @param pageable
     * @param q
     * @param authenticationProfileModel
     * @return
     */
    MockEntitiesPage paging(Pageable pageable, String q, AuthenticationProfileModel authenticationProfileModel);

    /**
     * @param id
     * @return
     */
    List<MockHistoryEntities> getMockHistories(String id);

    /**
     * @param mockId
     * @return
     */
    List<DtoMockUserLookupVmodel> getUsersListOfMocks(String mockId);

    /**
     * @param id
     * @param authenticationProfileModel
     * @return
     */
    List<DtoMockupDetailVmodel> getDetailMockUpIdByUserAccess(String id, AuthenticationProfileModel authenticationProfileModel);

    /**
     * @param request
     * @param path
     * @param id
     * @param body
     * @return
     * @throws NotFoundException
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     * @throws InvalidMockException
     */
    MockHelper getMockMocking(HttpServletRequest request, String path, String id, String body) throws NotFoundException, JsonProcessingException, UnsupportedEncodingException, InvalidMockException;

    /**
     * @param id
     * @param vmodel
     * @param authenticationProfileModel
     * @return
     * @throws NotFoundException
     */
    Object addUserAccessOnMock(String id, AddUserAccessVmodel vmodel, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    /**
     * @param id
     * @param userId
     * @param authenticationProfileModel
     * @return
     * @throws NotFoundException
     */
    Object removeAccessUserOnMock(String id, String userId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    /**
     * @param id
     * @param historyId
     * @param authenticationProfileModel
     * @return
     * @throws NotFoundException
     */
    DtoMockupHistoryVmodel geMockHistoryId(String id, String historyId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;
}
