package com.github.dekaulitz.mockyup.server.controllers;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingResponseContentEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingResponseEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.model.constants.MockRequest;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestModel;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectContractRequest;
import com.github.dekaulitz.mockyup.server.model.request.CreateProjectRequest;
import com.github.dekaulitz.mockyup.server.service.mockup.api.MockingService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectContractService;
import com.github.dekaulitz.mockyup.server.service.mockup.api.ProjectService;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
        .split(request.getContextPath() + MockRequest.MOCK_REQUEST_PREFIX)[1];
    String[] paths = path.split(MockRequest.MOCK_REQUEST_ID_PREFIX);
    Map<String, String> headers = Collections.list(request.getHeaderNames())
        .stream()
        .collect(Collectors.toMap(name -> name, request::getHeader));
    Map<String, String[]> parameters = request.getParameterMap();
    String contractId = paths[0];
    String requestPath = paths[1];
    MockRequestModel mockRequestModel = this.mockingService
        .mockingRequest(contractId, requestPath, body, request.getMethod(), headers, parameters,
            request.getContentType());
    return generateMockResponseEntity(mockRequestModel, request.getContentType());
  }


  private ResponseEntity<Object> generateMockResponseEntity(MockRequestModel mock,
      String contentType) {
    MockingMatchingResponseEmbedded response = mock
        .getResponse();
    HttpHeaders httpHeaders = new HttpHeaders();
    if (response.getHeaders() != null) {
      response.getHeaders().forEach((s, o) -> {
        httpHeaders.add(s, o.toString());
      });
    }
    Object responseBody = null;
    for (Entry<OpenApiContentType, MockingMatchingResponseContentEmbedded> entry : response
        .getContent().entrySet()) {
      OpenApiContentType openApiContentType = entry.getKey();
      MockingMatchingResponseContentEmbedded mockingMatchingResponseContentEmbedded = entry
          .getValue();
      if (openApiContentType.getValue().equalsIgnoreCase(contentType)) {
        responseBody = mockingMatchingResponseContentEmbedded.getValue();
        break;
      }
    }
    return ResponseEntity.status(response.getStatusCode()).headers(httpHeaders)
        .body(responseBody);
  }
}
