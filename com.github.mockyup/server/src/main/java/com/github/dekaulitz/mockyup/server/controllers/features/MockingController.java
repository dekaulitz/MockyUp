package com.github.dekaulitz.mockyup.server.controllers.features;


import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.facade.MockFeatureFacade;
import com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestAttributeModel;
import com.github.dekaulitz.mockyup.server.model.dto.MockRequestModel;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.mockup.MockingMatchingResponseContentEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.mockup.MockingMatchingResponseEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.constants.OpenApiContentType;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockingController {


  @Autowired
  private MockFeatureFacade mockFeatureFacade;

  /**
   * @param body optional depend on the contract that registered
   * @return ResponseEntity
   */
  @RequestMapping(value = "/mocking-path/**", method = {RequestMethod.OPTIONS,
      RequestMethod.DELETE,
      RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PATCH,
      RequestMethod.PUT,
      RequestMethod.TRACE}
  )
  public ResponseEntity<Object> mockingPath(
      @RequestBody(required = false) String body,
      @ModelAttribute MockRequestAttributeModel mockRequestAttributeModel) throws ServiceException {
    mockRequestAttributeModel.setBody(body);
    MockRequestModel mockRequestModel = this.mockFeatureFacade.getMockingRequest(
        mockRequestAttributeModel);
    return generateMockResponseEntity(mockRequestModel, mockRequestAttributeModel.getContentType());
  }


  @ModelAttribute
  private MockRequestAttributeModel getModelRequestAttributeModel(HttpServletRequest servletRequest)
      throws ServiceException {
    String endpoint = servletRequest.getRequestURI();
    if (!endpoint.contains(ApplicationConstants.MOCK_REQUEST_ID_PREFIX)) {
      throw new ServiceException(ResponseCode.INVALID_MOCK_CONTRACT, "invalid path");
    }
    String[] endpoints = endpoint
        .split(servletRequest.getContextPath() + ApplicationConstants.MOCK_REQUEST_PREFIX);
    if (endpoints.length < 1) {
      throw new ServiceException(ResponseCode.INVALID_MOCK_CONTRACT);
    }
    String[] paths = endpoints[1].split(ApplicationConstants.MOCK_REQUEST_ID_PREFIX);
    if (paths.length != 2) {
      throw new ServiceException(ResponseCode.INVALID_MOCK_CONTRACT);
    }
    Map<String, String> headers = Collections.list(servletRequest.getHeaderNames())
        .stream()
        .collect(Collectors.toMap(name -> name, servletRequest::getHeader));
    Map<String, String[]> parameters = servletRequest.getParameterMap();
    return MockRequestAttributeModel.builder().headers(headers).mockEndpoint(paths[0])
        .requestPath(paths[1]).requestMethod(servletRequest.getMethod())
        .parameters(parameters).contentType(servletRequest.getContentType()).build();
  }

  private ResponseEntity<Object> generateMockResponseEntity(MockRequestModel mock,
      String contentType) {
    MockingMatchingResponseEmbedded response = mock.getResponse();
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
