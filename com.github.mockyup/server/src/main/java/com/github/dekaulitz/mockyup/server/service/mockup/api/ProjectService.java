package com.github.dekaulitz.mockyup.server.service.mockup.api;

import com.github.dekaulitz.mockyup.server.db.entities.ProjectEntity;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {

  ProjectEntity getById(String id);

  List<ProjectEntity> getAll(GetProjectParam getProjectParam);

  ProjectEntity createProject(CreateProjectRequest createProjectRequest);
}
