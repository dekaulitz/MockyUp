package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.vmodels.ResponseVmodel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


public class AdviceController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidMockException.class)
    public final ResponseEntity<Object> handleInvalidMockException(InvalidMockException ex, WebRequest request) {
        if (ex.getErrorModel() == null) return handlingNullErrorModel(ex);
        return ResponseEntity.status(ex.getErrorModel().getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(ex.getMessage())
                        .responseCode(ex.getErrorModel().getErrorCode()).build());
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        if (ex.getErrorModel() == null) return handlingNullErrorModel(ex);
        return ResponseEntity.status(ex.getErrorModel().getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(ex.getMessage())
                        .responseCode(ex.getErrorModel().getErrorCode()).build());
    }

    /**
     * @param ex
     * @return
     * @desc handling error model when null
     */
    public ResponseEntity<Object> handlingNullErrorModel(Exception ex) {
        return ResponseEntity.status(ResponseCode.GLOBAL_ERROR_MESSAGE.getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(ex.getMessage())
                        .responseCode(ResponseCode.GLOBAL_ERROR_MESSAGE.getErrorCode()).build());

    }
}
