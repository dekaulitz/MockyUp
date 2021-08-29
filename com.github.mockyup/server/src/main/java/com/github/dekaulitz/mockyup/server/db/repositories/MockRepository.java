package com.github.dekaulitz.mockyup.server.db.repositories;

import com.github.dekaulitz.mockyup.server.db.entities.v1.MockEntities;
import com.github.dekaulitz.mockyup.server.db.repositories.support.MockRepositorySupport;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MockRepository extends MongoRepository<MockEntities, String>,
    MockRepositorySupport {

  @Query("{'users':?0}")
  List<MockEntities> getMocksHasNoUsers(String users);
}
