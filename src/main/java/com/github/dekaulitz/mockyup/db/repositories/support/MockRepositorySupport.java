package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.MockHistoryEntities;
import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MockRepositorySupport {
    /**
     * get mock detail with current access
     *
     * @param id                         id from mock collection
     * @param authenticationProfileModel user auth data
     * @return List<DtoMockupDetailVmodel>
     */
    List<DtoMockupDetailVmodel> getMockDetailWithCurrentAccess(String id, AuthenticationProfileModel authenticationProfileModel);

    /**
     * get users mock that has access
     *
     * @param id id from mock collection
     * @return List<DtoMockUserLookupVmodel>
     */
    List<DtoMockUserLookupVmodel> getUsersMock(String id);

    /**
     * paging mock data
     *
     * @param pageable                   Spring data pageable
     * @param q                          query data like example q=firstname:fahmi mean field firstname with value fahmi
     * @param authenticationProfileModel user auth data
     * @return
     */
    MockEntitiesPage paging(Pageable pageable, String q, AuthenticationProfileModel authenticationProfileModel);

    /**
     * get mock histories
     *
     * @param id id from mock colelction
     * @return List<MockHistoryEntities>
     */
    List<MockHistoryEntities> getMockHistories(String id);

    /**
     * @param id
     * @param userId
     * @param mockEntities
     * @return
     * @throws NotFoundException
     */
    Object removeAccessUserOnMock(String id, String userId, MockEntities mockEntities) throws NotFoundException;

    /**
     * registering user to mock
     *
     * @param id           id from mock collection
     * @param accessVmodel access data user that want to register
     * @param mockEntities mockentities
     * @return Object
     */
    Object registeringUserToMock(String id, AddUserAccessVmodel accessVmodel, MockEntities mockEntities);

    /**
     * check user access permision on mock
     *
     * @param id     id from mock collection
     * @param userId id from user colelction
     * @return List<MockEntities>
     */
    List<MockEntities> checkMockUserAccessPermission(String id, String userId);

    /**
     * injecting user root for the firstime
     *
     * @param user user root
     */
    void injectRootIntoAllMocks(UserEntities user);
}
