package com.github.dekaulitz.mockyup.db.repositories;

import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.repositories.support.MockRepositorySupport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockRepository extends MongoRepository<MockEntities, String>, MockRepositorySupport {
    @Query("{'users':?0}")
    List<MockEntities> getMocksHasNoUsers(String users);
}
