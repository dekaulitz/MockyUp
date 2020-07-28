package com.github.dekaulitz.mockyup.base.controller;

import com.github.dekaulitz.mockyup.infrastructure.errors.vmodels.ResponseVmodel;
import com.github.dekaulitz.mockyup.utils.ConstantsRepository;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage()).collect(Collectors.toList());
        return ResponseEntity.status(ResponseCode.VALIDATION_FAIL.getHttpCode()).body(
                ResponseVmodel.builder().extraMessages(errors).responseMessage(ResponseCode.VALIDATION_FAIL.getErrorMessage())
                        .responseCode(ResponseCode.VALIDATION_FAIL.getErrorCode()).build());
    }
}
