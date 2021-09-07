package com.github.dekaulitz.mockyup.server.db.tmp.repositories;

import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.support.UserRepositorySupport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntities, String>,
    UserRepositorySupport {

  UserEntities findFirstByUsername(String userName);

  UserEntities findByIdAndUsername(String id, String username);

  boolean existsByUsername(String username);
}
