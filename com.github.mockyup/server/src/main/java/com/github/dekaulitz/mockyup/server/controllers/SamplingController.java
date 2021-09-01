package com.github.dekaulitz.mockyup.server.controllers;

import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectService;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SamplingController {

  @Autowired
  private ProjectContractService projectContractService;
  @Autowired
  private ProjectService projectService;
  @Autowired
  private ModelMapper modelMapper;

//  @RequestMapping(value = "/sampling", method = {RequestMethod.GET}
//  )
//  public ResponseEntity<Object> mockingPath(@Valid GetProjectContractParam getProjectContractParam) {
//    List<MockupResponse> mockEntitiesList = projectContractService.getAll(getProjectContractParam);
//    return ResponseEntity.ok(mockEntitiesList);
//  }

  @RequestMapping(value = "/projects", method = {RequestMethod.POST})
  public ResponseEntity<Object> createProject(
      @Valid @RequestBody CreateProjectRequest createProjectRequest) {
    return ResponseEntity.ok(projectService.createProject(createProjectRequest));
  }

  @RequestMapping(value = "/projects", method = {RequestMethod.GET})
  public ResponseEntity<Object> getALlProjects(@Valid GetProjectParam getProjectParam)
      throws ServiceException {
    return ResponseEntity.ok(projectService.getAll(getProjectParam));
  }

  @RequestMapping(value = "/sampling", method = {RequestMethod.POST})
  public ResponseEntity<Object> createContract(
      @Valid @RequestBody CreateProjectContractRequest createProjectContractRequest)
      throws ServiceException {
    return ResponseEntity.ok(projectContractService.createContract(createProjectContractRequest));
  }
  @RequestMapping(value = "/sampling", method = {RequestMethod.GET})
  public ResponseEntity<Object> createContract(
      @Valid GetProjectContractParam getProjectContractParam)
      throws ServiceException {
    return ResponseEntity.ok(projectContractService.getAll(getProjectContractParam));
  }
}
