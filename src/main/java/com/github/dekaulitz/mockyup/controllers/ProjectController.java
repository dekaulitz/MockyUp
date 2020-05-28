package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.entities.ProjectEntities;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.models.ProjectModel;
import com.github.dekaulitz.mockyup.repositories.paging.ProjectEntitiesPage;
import com.github.dekaulitz.mockyup.vmodels.ProjectVmodel;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ProjectController extends BaseController {
    private final ProjectModel projectModel;

    public ProjectController(LogsMapper logsMapper, ProjectModel projectModel) {
        super(logsMapper);
        this.projectModel = projectModel;
    }

    public ResponseEntity<String> addProject() throws IOException {
        return null;
    }

    @GetMapping(value = "/project/list")
    public ResponseEntity<List<ProjectEntities>> getProjects() {
        List<ProjectEntities> projects = this.projectModel.all();
        return ResponseEntity.ok(projects);
    }

    @GetMapping(value = "/project/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProjectPage(
            Pageable pageable,
            @RequestParam(value = "q", required = false) String q) {
        try {
            ProjectEntitiesPage pagingVmodel = this.projectModel.paging(pageable, q);
            return ResponseEntity.ok(pagingVmodel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/project/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getProjectById(@PathVariable String id) {
        try {
            ProjectEntities projectEntities = this.projectModel.getById(id);
            ProjectVmodel projectVmodel = ProjectVmodel.builder().id(projectEntities.getId()).name(projectEntities.getName()).description(projectEntities.getDescription()).build();
            return ResponseEntity.ok(projectVmodel);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("no project found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/project/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteProjectById(@PathVariable String id) {
        try {
            this.projectModel.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("no project found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "/project/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProjectById(@PathVariable String id, @RequestBody ProjectVmodel body) {
        try {
            ProjectEntities projectEntities = this.projectModel.updateByID(id, body);
            ProjectVmodel projectVmodel = ProjectVmodel.builder().id(projectEntities.getId())
                    .name(projectEntities.getName()).description(projectEntities.getDescription()).build();
            return ResponseEntity.ok(projectVmodel);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("no project found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
