package com.github.dekaulitz.mockyup.server.controllers;

import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.service.mockup.api.MockingService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectService;
import com.github.dekaulitz.mockyup.server.utils.MockHelper;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SamplingController {

  @Autowired
  private ProjectContractService projectContractService;
  @Autowired
  private ProjectService projectService;
  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private MockingService mockingService;

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

  /**
   * @param path path on mock swagger pathitem that want to get mocking the response
   * @param id   mock id from  mock collection
   * @param body optional depend on the contract that registered
   * @return ResponseEntity
   */
  @RequestMapping(value = "/test-mocking/mocking/**", method = {RequestMethod.OPTIONS,
      RequestMethod.DELETE,
      RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PATCH,
      RequestMethod.PUT,
      RequestMethod.TRACE}
  )
  public ResponseEntity<Object> mockingPath(
      @RequestBody(required = false) String body,
      HttpServletRequest request) throws ServiceException {
    String path = request.getRequestURI()
        .split(request.getContextPath() + "/test-mocking/mocking/")[1];
    String[] id = path.split("/path");
    this.mockingService.getMockMocking(request, null, null, body);
    return ResponseEntity.ok().build();
  }
}
