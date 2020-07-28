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

    MockEntities getById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    MockEntities save(MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws InvalidMockException;

    MockEntities updateByID(String id, MockVmodel view, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException, InvalidMockException;

    void deleteById(String id, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    MockEntitiesPage paging(Pageable pageable, String q, AuthenticationProfileModel authenticationProfileModel);

    List<MockHistoryEntities> getMockHistories(String id);

    List<DtoMockUserLookupVmodel> getUsersListOfMocks(String mockId);

    List<DtoMockupDetailVmodel> getDetailMockUpIdByUserAccess(String id, AuthenticationProfileModel authenticationProfileModel);

    MockHelper getMockMocking(HttpServletRequest request, String path, String id, String body) throws NotFoundException, JsonProcessingException, UnsupportedEncodingException, InvalidMockException;

    Object addUserAccessOnMock(String id, AddUserAccessVmodel vmodel, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    Object removeAccessUserOnMock(String id, String userId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;

    DtoMockupHistoryVmodel geMockHistoryId(String id, String historyId, AuthenticationProfileModel authenticationProfileModel) throws NotFoundException;
}
