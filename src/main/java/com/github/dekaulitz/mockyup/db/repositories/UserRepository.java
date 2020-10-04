package com.github.dekaulitz.mockyup.db.repositories;

import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.support.UserRepositorySupport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntities, String>,
    UserRepositorySupport {

  UserEntities findFirstByUsername(String userName);

  UserEntities findByIdAndUsername(String id, String username);

  boolean existsByUsername(String username);
}
