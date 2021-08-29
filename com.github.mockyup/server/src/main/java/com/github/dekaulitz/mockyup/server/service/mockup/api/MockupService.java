package com.github.dekaulitz.mockyup.server.service.mockup.api;

import com.github.dekaulitz.mockyup.server.db.entities.v1.MockEntities;
import com.github.dekaulitz.mockyup.server.db.entities.v2.ProjectEntities;
import com.github.dekaulitz.mockyup.server.model.param.GetMockUpParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.model.response.MockupResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface MockupService {

  ProjectEntities createProject(CreateProjectRequest createProjectRequest);

  MockEntities getById(String id);

  List<MockupResponse> getAll(GetMockUpParam getMockUpParam);


}
