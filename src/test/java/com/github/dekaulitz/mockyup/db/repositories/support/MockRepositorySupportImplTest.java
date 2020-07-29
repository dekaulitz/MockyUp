package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.MockHistoryEntities;
import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.entities.UserMocksEntities;
import com.github.dekaulitz.mockyup.db.repositories.MockHistoryRepository;
import com.github.dekaulitz.mockyup.db.repositories.MockRepository;
import com.github.dekaulitz.mockyup.db.repositories.UserRepository;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.helperTest.Helper;
import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.utils.Hash;
import com.github.dekaulitz.mockyup.utils.Role;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class MockRepositorySupportImplTest {

    @Autowired
    MockRepository mockRepository;

    @Autowired
    MockHistoryRepository mockHistoryRepository;

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        mockRepository.deleteAll();
    }

    @Test
    void getDetailMockUpIdByUserAccess() throws IOException {
        UserEntities userEntities = this.insertUserEntitis();

        List<UserMocksEntities> userMocksEntities = new ArrayList<>();
        userMocksEntities.add(UserMocksEntities.builder()
                .access(Role.MOCKS_READ_WRITE.name())
                .userId(userEntities.getId())
                .build());
        MockEntities mockEntities = Helper.getMockEntities();
        mockEntities.setId(null);
        mockEntities.setUsers(userMocksEntities);
        mockRepository.save(mockEntities);

        AuthenticationProfileModel authenticationProfileModel = new AuthenticationProfileModel();
        authenticationProfileModel.set_id(userEntities.getId());
        authenticationProfileModel.setUsername(userEntities.getUsername());
        List<DtoMockupDetailVmodel> detailMockUserAccess = mockRepository.getMockDetailWithCurrentAccess(mockEntities.getId(),
                authenticationProfileModel);

        Assert.isTrue(detailMockUserAccess.size() == 1, "size data is not expected");
        detailMockUserAccess.forEach(dtoMockupDetailVmodel -> {
            Assert.isTrue(dtoMockupDetailVmodel.getCurrentAccessUser().getAccess().equals(Role.MOCKS_READ_WRITE.name()), "access is not expected");
        });

    }

    @Test
    void getUsersMock() throws IOException {

        UserEntities userEntitiesHasMocksRW = Helper.getUserEntities();
        userEntitiesHasMocksRW.setId(null);
        userEntitiesHasMocksRW.setAccessList(Collections.singletonList(Role.MOCKS_READ_WRITE.name()));
        userRepository.save(userEntitiesHasMocksRW);

        UserEntities userEntitiesHasMocksR = Helper.getUserEntities();
        userEntitiesHasMocksR.setId(null);
        userEntitiesHasMocksR.setAccessList(Collections.singletonList(Role.MOCKS_READ.name()));
        userRepository.save(userEntitiesHasMocksR);

        List<UserMocksEntities> userMocksEntitiesList = new ArrayList<>();
        userMocksEntitiesList.add(UserMocksEntities.builder()
                .userId(userEntitiesHasMocksRW.getId())
                .access(Role.MOCKS_READ_WRITE.name())
                .build());
        userMocksEntitiesList.add(UserMocksEntities.builder()
                .userId(userEntitiesHasMocksR.getId())
                .access(Role.MOCKS_READ.name())
                .build());

        MockEntities mockEntities = Helper.getMockEntities();
        mockEntities.setId(null);
        mockEntities.setUsers(userMocksEntitiesList);
        mockRepository.save(mockEntities);

        List<DtoMockUserLookupVmodel> usermocks = mockRepository.getUsersMock(mockEntities.getId());
        Assert.isTrue(usermocks.get(0).getUsers().size() == 2, "size is not expected");
    }

    @Test
    void paging() throws IOException {
        UserEntities userhasAccess = Helper.getUserEntities();
        userhasAccess.setId(null);
        userhasAccess.setAccessList(Collections.singletonList(Role.MOCKS_READ_WRITE.name()));
        userRepository.save(userhasAccess);

        List<UserMocksEntities> userMocksEntitiesList = new ArrayList<>();
        userMocksEntitiesList.add(UserMocksEntities.builder()
                .userId(userhasAccess.getId())
                .access(Role.MOCKS_READ_WRITE.name())
                .build());
        MockEntities firstData = Helper.getMockEntities();
        firstData.setId(null);
        firstData.setTitle("firstData");
        firstData.setUsers(userMocksEntitiesList);
        mockRepository.save(firstData);

        MockEntities secondData = Helper.getMockEntities();
        secondData.setId(null);
        secondData.setTitle("secondData");
        secondData.setUsers(userMocksEntitiesList);
        mockRepository.save(secondData);

        MockEntities thirdData = Helper.getMockEntities();
        secondData.setId(null);
        secondData.setTitle("thirdData");
        mockRepository.save(thirdData);

        AuthenticationProfileModel authenticationProfileModel = new AuthenticationProfileModel();
        authenticationProfileModel.set_id(userhasAccess.getId());
        PageRequest pageRequest = PageRequest.of(0, 10);
        MockEntitiesPage mockEntitiesPage = mockRepository.paging(pageRequest, "title:a", authenticationProfileModel);
        Assert.isTrue(mockEntitiesPage.getRows().size() == 2, "size is not expected");
        pageRequest = PageRequest.of(0, 1);
        mockEntitiesPage = mockRepository.paging(pageRequest, "title:data", authenticationProfileModel);
        Assert.isTrue(mockEntitiesPage.getRows().size() == 1, "size is not expected");
    }

    @Test
    void getMockHistories() throws IOException {
        MockEntities mockEntities = Helper.getMockEntities();
        mockEntities.setId(null);
        mockRepository.save(mockEntities);

        MockHistoryEntities mockHistoryEntities = Helper.getMockHistoryEntities();
        mockHistoryEntities.setId(null);
        mockHistoryEntities.setMockId(mockEntities.getId());
        mockHistoryRepository.save(mockHistoryEntities);
        mockHistoryEntities.setId(null);
        mockHistoryRepository.save(mockHistoryEntities);

        List<MockHistoryEntities> mockHistory = mockRepository.getMockHistories(mockEntities.getId());
        Assert.isTrue(mockHistory.size() == 2, "size is not expected");


    }

    @Test
    void removeAccessUserOnMock() throws IOException {
        UserEntities userEntities = Helper.getUserEntities();
        userEntities.setId(null);
        userRepository.save(userEntities);

        MockEntities mockEntities = Helper.getMockEntities();
        mockEntities.setId(null);
        mockEntities.setUsers(Collections.singletonList(UserMocksEntities.builder()
                .access(Role.MOCKS_READ_WRITE.name())
                .userId(userEntities.getId())
                .build()));
        mockRepository.save(mockEntities);

        try {
            UpdateResult result = (UpdateResult) mockRepository.removeAccessUserOnMock(mockEntities.getId(), userEntities.getId(), mockEntities);
            Assert.isTrue(result.getModifiedCount() == 1, "data not expected");
            mockRepository.removeAccessUserOnMock(mockEntities.getId(), userEntities.getId(), mockEntities);
        } catch (NotFoundException e) {
            Assert.isTrue(e instanceof NotFoundException, "instance of is not expected");
        }
    }

    @Test
    void addUserToMock() throws IOException {
        UserEntities userEntities = Helper.getUserEntities();
        userEntities.setId(null);
        userRepository.save(userEntities);
        MockEntities mockEntities = Helper.getMockEntities();
        mockEntities.setId(null);
        mockRepository.save(mockEntities);
        UpdateResult addUserToMock = (UpdateResult) mockRepository.registeringUserToMock(mockEntities.getId(), AddUserAccessVmodel.builder()
                .userId(userEntities.getId())
                .access(Role.MOCKS_READ_WRITE.name())
                .build(), mockEntities);
        Assert.isTrue(addUserToMock.getModifiedCount() == 1, "modified is not expected");

        addUserToMock = (UpdateResult) mockRepository.registeringUserToMock(mockEntities.getId(), AddUserAccessVmodel.builder()
                .userId(userEntities.getId())
                .access(Role.MOCKS_READ.name())
                .build(), mockEntities);

        Assert.isTrue(addUserToMock.getModifiedCount() == 1, "modified is not expected");
    }

    @Test
    void findMockByIdAndUserId() throws IOException {
        UserEntities userEntities = Helper.getUserEntities();
        userEntities.setId(null);
        userRepository.save(userEntities);

        MockEntities mockEntities = Helper.getMockEntities();
        mockEntities.setId(null);
        mockEntities.setUsers(Collections.singletonList(UserMocksEntities.builder()
                .access(Role.MOCKS_READ_WRITE.name())
                .userId(userEntities.getId())
                .build()));
        mockRepository.save(mockEntities);

        List<MockEntities> mockEntitiesList = mockRepository.checkMockUserAccessPermission(mockEntities.getId(), userEntities.getId());
        Assert.isTrue(mockEntitiesList.size() == 1, "size is not expected");
    }

    @Test
    void injectRootIntoAllMocks() throws IOException {
        UserEntities userEntities = Helper.getUserEntities();
        userEntities.setId(null);
        userRepository.save(userEntities);
        MockEntities mockEntities = Helper.getMockEntities();
        mockEntities.setId(null);
        mockEntities.setUsers(null);
        mockRepository.save(mockEntities);
        mockRepository.injectRootIntoAllMocks(userEntities);
        MockEntities mockEntities1 = mockRepository.findById(mockEntities.getId()).get();
        Assert.isTrue(mockEntities1.getUsers().size() == 1, "users is not expected");
        mockEntities1.getUsers().forEach(userMocksEntities -> {
            Assert.isTrue(userMocksEntities.getUserId().equals(userEntities.getId()), "userid is not expected");
            Assert.isTrue(userMocksEntities.getAccess().equals(Role.MOCKS_READ_WRITE.name()), "access is not expected");
        });

    }


    public UserEntities insertUserEntitis() {
        List<String> accesList = new ArrayList<>();
        accesList.add(Role.MOCKS_READ_WRITE.name());
        accesList.add(Role.USERS_READ_WRITE.name());
        UserEntities userEntities = Helper.getUserEntities();

        userEntities.setId(null);
        userEntities.setUsername("fahmi");
        userEntities.setPassword(Hash.hashing("12345"));
        userEntities.setAccessList(accesList);
        return userRepository.save(userEntities);
    }
}
