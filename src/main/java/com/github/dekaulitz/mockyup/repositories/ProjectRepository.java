package com.github.dekaulitz.mockyup.repositories;

import com.github.dekaulitz.mockyup.entities.ProjectEntities;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<ProjectEntities, String> {
}
