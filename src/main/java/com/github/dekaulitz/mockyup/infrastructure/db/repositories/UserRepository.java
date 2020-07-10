package com.github.dekaulitz.mockyup.infrastructure.db.repositories;

import com.github.dekaulitz.mockyup.infrastructure.db.entities.UserEntities;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntities, String> {
    UserEntities findFirstByUsername(String userName);

    UserEntities findByIdAndUsername(String id, String username);

    boolean existsByUsername(String username);
}
