package com.github.dekaulitz.mockyup.server.db.tmp.repositories.support;

import com.github.dekaulitz.mockyup.server.configuration.jwt.JwtProfileModel;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.MockEntities;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.MockHistoryEntities;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.server.tmp.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.server.tmp.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.server.tmp.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.server.tmp.errors.handlers.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface MockRepositorySupport {

  /**
   * get mock detail with current access
   *
   * @param id              {@link String} id from mock collection
   * @param jwtProfileModel {@link JwtProfileModel} user auth data
   * @return List<DtoMockupDetailVmodel>
   */
  List<DtoMockupDetailVmodel> getMockDetailWithCurrentAccess(String id,
      JwtProfileModel jwtProfileModel);

  /**
   * get users mock that has access
   *
   * @param id {@link String} id from mock collection
   * @return List<DtoMockUserLookupVmodel>
   */
  List<DtoMockUserLookupVmodel> getUsersMock(String id);

  /**
   * paging mock data
   *
   * @param pageable                   {@link Pageable} Spring data pageable
   * @param q                          {@link String} query data like example q=firstname:fahmi mean
   *                                   field firstname with value fahmi
   * @param jwtProfileModel {@link JwtProfileModel} user auth data
   * @return MockEntitiesPage mockentities page
   */
  MockEntitiesPage paging(Pageable pageable, String q,
      JwtProfileModel jwtProfileModel);

  /**
   * get mock histories
   *
   * @param id {@link String}id from mock colelction
   * @return List<MockHistoryEntities>
   */
  List<MockHistoryEntities> getMockHistories(String id);

  /**
   * @param id           {@link String} id from mock collection
   * @param userId       {@link String} id from user collection
   * @param mockEntities {@link MockEntities} mockentitites page
   * @return Object {@link com.mongodb.client.result.UpdateResult} response mongo client
   * @throws NotFoundException
   */
  Object removeAccessUserOnMock(String id, String userId, MockEntities mockEntities)
      throws NotFoundException;

  /**
   * registering user to mock
   *
   * @param id           {@link String} id from mock collection
   * @param accessVmodel {@link AddUserAccessVmodel}access data user that want to register
   * @param mockEntities {@link MockEntities} mock enttitis data for checking user entities is
   *                     already exist for modification
   * @return Object {@link com.mongodb.client.result.UpdateResult} response mongo client
   */
  Object registeringUserToMock(String id, AddUserAccessVmodel accessVmodel,
      MockEntities mockEntities);

  /**
   * check user access permision on mock
   *
   * @param id     {@link String} id from mock collection
   * @param userId {@link String} id from user colelction
   * @return List<MockEntities>
   */
  MockEntities checkMockUserAccessPermission(String id, String userId);

  /**
   * injecting user root for the firstime
   *
   * @param user user root
   */
  void injectRootIntoAllMocks(UserEntities user);
}
