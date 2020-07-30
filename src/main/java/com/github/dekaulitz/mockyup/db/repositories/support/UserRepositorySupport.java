package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositorySupport {
    /**
     * @param pageable Spring data pageable
     * @param q        query data like example q=firstname:fahmi mean field firstname with value fahmi
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
