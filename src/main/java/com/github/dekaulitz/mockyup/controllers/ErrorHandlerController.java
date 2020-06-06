package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.errorhandlers.UnathorizedAccess;
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
public class ErrorHandlerController implements ErrorController {

    public ErrorHandlerController() {
    }

    //@TODO should enhance the response error handler
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
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (exception instanceof UnathorizedAccess) {
                UnathorizedAccess unathorizedAccess = (UnathorizedAccess) exception;
                return ResponseEntity.status(unathorizedAccess.getErrorModel().getHttpCode()).body(
                        ResponseVmodel.builder().responseMessage(unathorizedAccess.getMessage())
                                .responseCode(unathorizedAccess.getErrorModel().getErrorCode()).build());
            }
            return ResponseEntity.status(statusCode).body(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
