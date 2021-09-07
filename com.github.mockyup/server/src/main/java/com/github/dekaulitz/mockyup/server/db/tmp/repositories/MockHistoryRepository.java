package com.github.dekaulitz.mockyup.server.db.tmp.repositories;

import com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1.MockHistoryEntities;
import com.github.dekaulitz.mockyup.server.db.tmp.repositories.support.MockHistoryRepositorySupport;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockHistoryRepository extends MongoRepository<MockHistoryEntities, String>,
    MockHistoryRepositorySupport {

  List<MockHistoryEntities> findByMockId(String id);

  void deleteAllByMockId(String mockId);
}
