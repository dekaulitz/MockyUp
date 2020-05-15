package com.github.dekaulitz.mockyup.repositories;

import com.github.dekaulitz.mockyup.entities.UserEntities;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntities, String> {
}
