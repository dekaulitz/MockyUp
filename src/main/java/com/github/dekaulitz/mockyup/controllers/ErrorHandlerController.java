package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.vmodels.ResponseVmodel;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorHandlerController extends BaseController implements ErrorController {

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status.equals(HttpStatus.NOT_FOUND.value())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseVmodel.builder()
                    .responseCode(ResponseCode.GLOBAL_PAGE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCode.GLOBAL_PAGE_NOT_FOUND.getErrorMessage()).build());
        }
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (exception == null) {
            exception = (Exception) request.getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR");
        }
        return this.handlingErrorResponse(exception, request);

    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
