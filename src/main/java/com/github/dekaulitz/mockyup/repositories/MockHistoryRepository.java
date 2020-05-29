package com.github.dekaulitz.mockyup.repositories;

import com.github.dekaulitz.mockyup.entities.MockHistoryEntities;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockHistoryRepository extends MongoRepository<MockHistoryEntities, String> {
}
