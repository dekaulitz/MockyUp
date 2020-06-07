package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.errorhandlers.*;
import com.github.dekaulitz.mockyup.models.helper.MockExample;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.vmodels.ResponseVmodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class BaseController {
    protected final LogsMapper logsMapper;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public BaseController(LogsMapper logsMapper) {
        this.logsMapper = logsMapper;
    }

    /**
     * @param mock
     * @return
     * @desc generate mock response base mock data
     */
    protected ResponseEntity generateMockResponseEntity(MockExample mock) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (mock.getResponse().getHeaders() != null) {
            for (Map.Entry headerMap : mock.getResponse().getHeaders().entrySet()) {
                httpHeaders.add(headerMap.getKey().toString(), headerMap.getValue().toString());
            }
        }
        return ResponseEntity.status(mock.getResponse().getHttpCode()).headers(httpHeaders).body(mock.getResponse().getResponse());
    }


    protected ResponseEntity<Object> handlingErrorResponse(Exception ex) {
        if (ex instanceof DuplicateDataEntry) {
            return this.responseHandling(((DuplicateDataEntry) ex).getErrorModel());
        } else if (ex instanceof InvalidMockException) {
            return this.responseHandling(((InvalidMockException) ex).getErrorModel());
        } else if (ex instanceof NotFoundException) {
            return this.responseHandling(((NotFoundException) ex).getErrorModel());
        } else if (ex instanceof UnathorizedAccess) {
            return this.responseHandling(((UnathorizedAccess) ex).getErrorModel());
        }
        return ResponseEntity.status(ResponseCode.GLOBAL_ERROR_MESSAGE.getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(ex.getMessage())
                        .responseCode(ResponseCode.GLOBAL_ERROR_MESSAGE.getErrorCode()).build());
    }


    private ResponseEntity<Object> responseHandling(ErrorModel errorModel) {
        return ResponseEntity.status(errorModel.getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(errorModel.getErrorMessage())
                        .responseCode(errorModel.getErrorCode()).build());
    }
}
