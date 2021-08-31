package com.github.dekaulitz.mockyup.server.service.mockup.api;

import com.github.dekaulitz.mockyup.server.db.entities.v2.ProjectEntities;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {

  ProjectEntities getById(String id);

  List<ProjectEntities> getAll(GetProjectParam getProjectParam);

  ProjectEntities createProject(CreateProjectRequest createProjectRequest);
}
