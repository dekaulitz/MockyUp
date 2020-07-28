package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.MockHistoryEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MockRepositorySupport {
    List<DtoMockupDetailVmodel> getDetailMockUpIdByUserAccess(String id, AuthenticationProfileModel authenticationProfileModel);

    List<DtoMockUserLookupVmodel> getUsersMock(String mockId);

    Object addUserAccessOnMock(String id, AddUserAccessVmodel vmodel, AuthenticationProfileModel authenticationProfileModel);

    MockEntitiesPage paging(Pageable pageable, String q, AuthenticationProfileModel authenticationProfileModel);

    List<MockHistoryEntities> getMockHistories(String id);

    MockEntities getUserMocks(String id);

    Object removeAccessUserOnMock(String id, String userId, MockEntities mockEntities) throws NotFoundException;

    Object addUserToMock(String id, AddUserAccessVmodel accessVmodel, MockEntities mockEntities);

    List<MockEntities> findMockByIdAndUserId(String id, String userId);

}
