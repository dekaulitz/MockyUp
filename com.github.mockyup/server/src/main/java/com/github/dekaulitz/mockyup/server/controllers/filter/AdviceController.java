package com.github.dekaulitz.mockyup.server.controllers.filter;

import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.errors.UnauthorizedException;
import com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants;
import com.github.dekaulitz.mockyup.server.model.constants.Language;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.ErrorMessageModel;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import com.github.dekaulitz.mockyup.server.model.response.ResponseModel;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Response advisor for handling error
 */
@ControllerAdvice
@Log4j2
public class AdviceController extends ResponseEntityExceptionHandler {

  @Autowired
  private HttpServletRequest servletRequest;

  //handling if security configuration throw some error
  @ExceptionHandler(AuthenticationException.class)
  public final ResponseEntity<Object> handleAuthorization(UnauthorizedException ex,
      HttpServletRequest request) {
    MandatoryModel mandatoryModel = (MandatoryModel) request.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ErrorMessageModel errorMessageModel = ex.getErrorMessagResponseModelError()
        .filterTranslation(Language.EN);
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel, ex));
  }

  //handling if security configuration throw some error
  @ExceptionHandler(InsufficientAuthenticationException.class)
  public final ResponseEntity<Object> handleAuthorization(InsufficientAuthenticationException ex,
      HttpServletRequest request) {
    MandatoryModel mandatoryModel = (MandatoryModel) request.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ErrorMessageModel errorMessageModel = ResponseCode.UNAUTHORIZED_ACCESS.getErrorMessageModel();
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel, ex));
  } //handling if security configuration throw some error

  @ExceptionHandler(AccessDeniedException.class)
  public final ResponseEntity<Object> handleAuthorization(AccessDeniedException ex,
      HttpServletRequest request) {
    MandatoryModel mandatoryModel = (MandatoryModel) request.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ErrorMessageModel errorMessageModel = ResponseCode.UNAUTHORIZED_ACCESS.getErrorMessageModel();
    errorMessageModel.setDetailMessage(ex.getMessage());
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel, ex));
  }

  //handling if security configuration throw some error
  @ExceptionHandler(ServiceException.class)
  public final ResponseEntity<Object> handleServiceException(ServiceException ex,
      HttpServletRequest request) {
    MandatoryModel mandatoryModel = (MandatoryModel) request.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ex.printStackTrace();
    ErrorMessageModel errorMessageModel = ex.getErrorMessagResponseModelError()
        .filterTranslation(Language.EN);
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel, ex));
  }

  @ExceptionHandler(UnauthorizedException.class)
  public final ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex,
      HttpServletRequest request) {
    MandatoryModel mandatoryModel = (MandatoryModel) request.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ErrorMessageModel errorMessageModel = ex.getErrorMessagResponseModelError()
        .filterTranslation(Language.EN);
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel, ex));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public final ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex,
      HttpServletRequest request) {
    MandatoryModel mandatoryModel = (MandatoryModel) request.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ErrorMessageModel errorMessageModel = ResponseCode.BAD_REQUEST.getErrorMessageModel();
    errorMessageModel.setDetailMessage(ex.getMessage());
    ResponseModel responseModel = ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel,
        ex);
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(responseModel);
  }

  @ExceptionHandler(IllegalStateException.class)
  public final ResponseEntity<Object> handleIllegalArgument(IllegalStateException ex,
      HttpServletRequest request) {
    MandatoryModel mandatoryModel = (MandatoryModel) request.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ErrorMessageModel errorMessageModel = ResponseCode.BAD_REQUEST.getErrorMessageModel();
    errorMessageModel.setDetailMessage(ex.getMessage());
    ResponseModel responseModel = ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel,
        ex);
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(responseModel);
  }


  //
//  //centralize the exception when exception throwing without catch in service dispatcher
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleInvalidMockException(Exception ex,
      HttpServletRequest request) {
    ex.printStackTrace();
    log.error("error occurred : ", ex);
    ErrorMessageModel errorMessageModel = ResponseCode.INTERNAL_SERVER_ERROR.getErrorMessageModel()
        .filterTranslation(Language.EN);
    MandatoryModel mandatoryModel = (MandatoryModel) request.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ResponseModel responseModel = ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel,
        ex);
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(responseModel);
  }

  //
//handling when user do request with invalid method
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ErrorMessageModel errorMessageModel = ResponseCode.BAD_REQUEST.getErrorMessageModel()
        .filterTranslation(Language.EN);
    MandatoryModel mandatoryModel = (MandatoryModel) servletRequest.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ResponseModel responseModel = ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel,
        ex);
    responseModel.getError().setExtraMessage(ex.getAllErrors());
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(responseModel);
  }


  /**
   * Customize the response for HttpMessageNotReadableException.
   * <p>This method delegates to {@link #handleExceptionInternal}.
   *
   * @param ex      the exception
   * @param headers the headers to be written to the response
   * @param status  the selected response status
   * @param request the current request
   * @return a {@code ResponseEntity} instance
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ErrorMessageModel errorMessageModel = ResponseCode.BAD_REQUEST.getErrorMessageModel()
        .filterTranslation(Language.EN);
    MandatoryModel mandatoryModel = (MandatoryModel) servletRequest.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    ResponseModel responseModel = ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel,
        ex);
    responseModel.getError().setExtraMessage("Please check your request, your request is not readable!");
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(responseModel);
  }

}
