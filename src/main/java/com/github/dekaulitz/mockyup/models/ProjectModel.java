package com.github.dekaulitz.mockyup.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.entities.ProjectEntities;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.repositories.ProjectRepository;
import com.github.dekaulitz.mockyup.repositories.paging.ProjectEntitiesPage;
import com.github.dekaulitz.mockyup.vmodels.ProjectVmodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectModel extends BaseModel<ProjectEntities, ProjectVmodel> {

    @Autowired
    private final ProjectRepository repository;
    @Autowired
    private final MongoTemplate mongoTemplate;

    public ProjectModel(ProjectRepository mockRepository, MongoTemplate mongoTemplate) {
        this.repository = mockRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ProjectEntities> all() {
        return this.repository.findAll();
    }

    @Override
    public ProjectEntities getById(String id) throws NotFoundException {
        Optional<ProjectEntities> entity = this.repository.findById(id);
        if (!entity.isPresent()) {
            throw new NotFoundException("project not found");
        }
        return entity.get();
    }

    @Override
    public ProjectEntities save(ProjectVmodel view) {
        new ProjectEntities();
        Transform<ProjectEntities, ProjectVmodel> modelTransform = (a) -> ProjectEntities.builder()
                .description(a.getDescription())
                .name(a.getName())
                .build();
        ProjectEntities p = this.transform(view, modelTransform);
        return this.repository.save(p);
    }

    @Override
    public ProjectEntities updateByID(String id, ProjectVmodel view) throws NotFoundException, JsonProcessingException {
        Optional<ProjectEntities> projectEntities = this.repository.findById(id);
        if (!projectEntities.isPresent()) {
            throw new NotFoundException("project not found");
        }
        new ProjectEntities();
        Transform<ProjectEntities, ProjectVmodel> modelTransform = (a) -> ProjectEntities.builder()
                .description(a.getDescription())
                .name(a.getName())
                .id(id)
                .build();
        return this.repository.save(this.transform(view, modelTransform));
    }

    @Override
    public ProjectEntitiesPage paging(Pageable pageable, String q) {
        ProjectEntitiesPage basePage = new ProjectEntitiesPage();
        basePage.setConnection(mongoTemplate);
        basePage.addCriteria(q);
        basePage.setPageable(pageable).build(ProjectEntities.class);
        return basePage;
    }

    @Override
    public void deleteById(String id) throws NotFoundException {
        Optional<ProjectEntities> projectEntities = this.repository.findById(id);
        if (!projectEntities.isPresent()) {
            throw new NotFoundException("mocks not found");
        }
        repository.deleteById(projectEntities.get().getId());
    }
}
