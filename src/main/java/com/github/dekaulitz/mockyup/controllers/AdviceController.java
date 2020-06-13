package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.utils.ConstantsRepository;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.vmodels.ResponseVmodel;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@Log4j2
public class AdviceController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleInvalidMockException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorMessage())
                        .responseCode(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorCode()).build());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleInvalidMockException(Exception ex, HttpServletRequest request) {
        log.error("error occured : ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseVmodel.builder().responseMessage(ex.getMessage()).requestId((String) request.getAttribute(ConstantsRepository.REQUEST_ID))
                        .responseCode(ResponseCode.GLOBAL_ERROR_MESSAGE.getErrorCode()).build());
    }
}
