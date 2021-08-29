package com.github.dekaulitz.mockyup.server.db.repositories;

import com.github.dekaulitz.mockyup.server.db.entities.v1.MockHistoryEntities;
import com.github.dekaulitz.mockyup.server.db.repositories.support.MockHistoryRepositorySupport;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockHistoryRepository extends MongoRepository<MockHistoryEntities, String>,
    MockHistoryRepositorySupport {

  List<MockHistoryEntities> findByMockId(String id);

  void deleteAllByMockId(String mockId);
}
