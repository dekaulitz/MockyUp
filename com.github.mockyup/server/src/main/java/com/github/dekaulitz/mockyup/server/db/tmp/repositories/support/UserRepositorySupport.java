package com.github.dekaulitz.mockyup.server.db.tmp.repositories.support;

import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.paging.UserEntitiesPage;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserRepositorySupport {

  /**
   * @param pageable Spring data pageable
   * @param q        query data like example q=firstname:fahmi mean field firstname with value
   *                 fahmi
   * @return UserEntitiesPage
   */
  UserEntitiesPage paging(Pageable pageable, String q);

  /**
   * geting all users base on username
   *
   * @param username      parameter that will searching on user collection
   * @param excludeUserId user id that not included for query
   * @return List<UserEntities>
   */
  List<UserEntities> getUserListByUserName(String username, String excludeUserId);
}
