package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.base.controller.BaseController;
import com.github.dekaulitz.mockyup.infrastructure.errors.vmodels.ResponseVmodel;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorHandlerController extends BaseController implements ErrorController {

  /**
   * centralized error on that not catched with Advisor controller
   *
   * @param request HttpServletRequest for getting attribute from request
   * @return
   */
  @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Object> handleError(HttpServletRequest request) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (status.equals(HttpStatus.NOT_FOUND.value())) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseVmodel.builder()
          .responseCode(ResponseCode.GLOBAL_PAGE_NOT_FOUND.getErrorCode())
          .responseMessage(ResponseCode.GLOBAL_PAGE_NOT_FOUND.getErrorMessage()).build());
    }
    //if the exception is part of HttpServletRequest should be extracted
    Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    if (exception == null) {
      exception = (Exception) request
          .getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR");
    }
    return this.handlingErrorResponse(exception, request);

  }

  /**
   * entrypoint for error handling when the error is not catched with Advisor controller
   *
   * @return String
   */
  @Override
  public String getErrorPath() {
    return "/error";
  }
}
