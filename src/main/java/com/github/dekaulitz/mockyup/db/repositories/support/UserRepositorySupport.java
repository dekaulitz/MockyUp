package com.github.dekaulitz.mockyup.db.repositories.support;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositorySupport {
    UserEntitiesPage paging(Pageable pageable, String q);

    List<UserEntities> getUserListByUserName(String username, String excludeUserId);
}
