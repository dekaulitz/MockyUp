package com.github.dekaulitz.mockyup.repositories;

import com.github.dekaulitz.mockyup.entities.MockHistoryEntities;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockHistoryRepository extends MongoRepository<MockHistoryEntities, String> {

    public List<MockHistoryEntities> findByMockId(String id);
}
