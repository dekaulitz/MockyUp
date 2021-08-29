package com.github.dekaulitz.mockyup.server.controllers;

import com.github.dekaulitz.mockyup.server.configuration.jwt.JwtProfileModel;
import com.github.dekaulitz.mockyup.server.errors.handlers.DuplicateDataEntry;
import com.github.dekaulitz.mockyup.server.errors.handlers.InvalidMockException;
import com.github.dekaulitz.mockyup.server.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.server.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.server.errors.vmodels.ErrorVmodel;
import com.github.dekaulitz.mockyup.server.errors.vmodels.ResponseVmodel;
import com.github.dekaulitz.mockyup.server.utils.ConstantsRepository;
import com.github.dekaulitz.mockyup.server.utils.MockHelper;
import com.github.dekaulitz.mockyup.server.utils.ResponseCode;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * this is the base controller for handling response mock and error response
 */
public class BaseController {

  private static final String XML_MEDIA_TYPE = "application/xml";
  private static final String JSON_MEDIA_TYPE = "application/json";
  protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  /**
   * generate mock response
   *
   * @param mock Response mock
   * @return ResponseEntity
   */
  protected ResponseEntity<Object> generateMockResponseEntity(MockHelper mock) {
    HttpHeaders httpHeaders = new HttpHeaders();
    if (mock.getResponseProperty().getHeaders() != null) {
      for (Map.Entry headerMap : mock.getResponseProperty().getHeaders().entrySet()) {
        httpHeaders.add(headerMap.getKey().toString(), headerMap.getValue().toString());
      }
    }
    return ResponseEntity.status(mock.getResponseProperty().getHttpCode()).headers(httpHeaders)
        .body(mock.getResponseProperty().getResponse());
  }


  /**
   * handling error response with type of class exception
   *
   * @param ex      exception throwable
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  protected ResponseEntity<Object> handlingErrorResponse(Exception ex, HttpServletRequest request) {
    if (ex instanceof DuplicateDataEntry) {
      return this.responseHandling(((DuplicateDataEntry) ex).getErrorVmodel(), request);
    } else if (ex instanceof InvalidMockException) {
      return this.responseHandling(((InvalidMockException) ex).getErrorVmodel(), request);
    } else if (ex instanceof NotFoundException) {
      return this.responseHandling(((NotFoundException) ex).getErrorVmodel(), request);
    } else if (ex instanceof UnathorizedAccess) {
      return this.responseHandling(((UnathorizedAccess) ex).getErrorVmodel(), request);
    } else if (ex instanceof AccessDeniedException) {
      return ResponseEntity.status(ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode()).body(
          ResponseVmodel.builder()
              .responseMessage(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorMessage())
              .responseCode(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorCode())
              .requestId((String) request.getAttribute(ConstantsRepository.REQUEST_ID))
              .build());
    }
    //if there no exception that match with existing exception handler
    //will handling with global error model
    LOGGER.error("exception occured " + ex.getMessage(), ex);
    return ResponseEntity.status(ResponseCode.GLOBAL_ERROR_MESSAGE.getHttpCode()).body(
        ResponseVmodel.builder().responseMessage(ex.getMessage())
            .responseCode(ResponseCode.GLOBAL_ERROR_MESSAGE.getErrorCode())
            .requestId((String) request.getAttribute(ConstantsRepository.REQUEST_ID))
            .build());
  }

  /**
   * handling the error exception into json
   *
   * @param errorVmodel ErrorVmodel
   * @param request     HttpServletRequest for getting attribute from request
   * @return ResponseEntity<ResponseVmodel>
   */
  public ResponseEntity<Object> responseHandling(ErrorVmodel errorVmodel,
      HttpServletRequest request) {
    return ResponseEntity.status(errorVmodel.getHttpCode()).body(
        ResponseVmodel.builder().responseMessage(errorVmodel.getErrorMessage())
            .extraMessages(new ArrayList<>())
            .requestId((String) request.getAttribute(ConstantsRepository.REQUEST_ID))
            .responseCode(errorVmodel.getErrorCode()).build());
  }

  /**
   * rendering user auth from security context
   *
   * @return AuthenticationProfileModel
   */
  public JwtProfileModel getAuthenticationProfileModel() {
    return (JwtProfileModel) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
  }
}
