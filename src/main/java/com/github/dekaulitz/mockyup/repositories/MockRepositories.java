package com.github.dekaulitz.mockyup.repositories;

import com.github.dekaulitz.mockyup.entities.MockEntities;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockRepositories extends MongoRepository<MockEntities, String> {


}
