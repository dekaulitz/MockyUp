package com.github.dekaulitz.mockyup.server.controllers.filter;

import com.github.dekaulitz.mockyup.server.controllers.BaseController;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.errors.UnauthorizedException;
import com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants;
import com.github.dekaulitz.mockyup.server.model.constants.Language;
import com.github.dekaulitz.mockyup.server.model.constants.ResponseCode;
import com.github.dekaulitz.mockyup.server.model.dto.ErrorMessageModel;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import com.github.dekaulitz.mockyup.server.model.response.ResponseModel;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ErrorHandlerController extends BaseController implements ErrorController {

  @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Object> handleError(HttpServletRequest request) {
    MandatoryModel mandatoryModel = (MandatoryModel) request.getServletContext()
        .getAttribute(ApplicationConstants.MANDATORY);
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    //if the exception is part of HttpServletRequest should be extracted
    Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    if (exception == null) {
      exception = (Exception) request
          .getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR");
    }
    if (status.equals(HttpStatus.NOT_FOUND.value())) {
      ErrorMessageModel errorMessageModel = ResponseCode.PAGE_NOT_FOUND.getErrorMessageModel();
      return ResponseEntity.status(errorMessageModel.getHttpCode())
          .body(ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel,
              new ServiceException(ResponseCode.PAGE_NOT_FOUND)));
    }
    return this.handlingErrorResponse(exception, mandatoryModel);

  }

  private ResponseEntity<Object> handlingErrorResponse(Exception ex, MandatoryModel mandatoryModel) {
    ErrorMessageModel errorMessageModel = null;
    if (ex instanceof ServiceException) {
      errorMessageModel = ((ServiceException) ex).getErrorMessagResponseModelError();
    } else if (ex instanceof UnauthorizedException) {
      errorMessageModel = ((UnauthorizedException) ex).getErrorMessagResponseModelError();
    } else {
      errorMessageModel = ResponseCode.INTERNAL_SERVER_ERROR.getErrorMessageModel();
    }
    errorMessageModel.filterTranslation(Language.EN);
    return ResponseEntity.status(errorMessageModel.getHttpCode())
        .body(ResponseModel.initErrorResponse(errorMessageModel, mandatoryModel, ex));
  }
}
