package com.github.dekaulitz.mockyup.db.repositories;

import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockRepository extends MongoRepository<MockEntities, String> {
    @Query("{'users':?0}")
    public List<MockEntities> getMocksHasNoUsers(String users);
}
