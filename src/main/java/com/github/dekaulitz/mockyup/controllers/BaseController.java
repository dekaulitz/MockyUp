package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.errorhandlers.DuplicateDataEntry;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.errorhandlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.vmodels.ErrorVmodel;
import com.github.dekaulitz.mockyup.vmodels.ResponseVmodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class BaseController {
    protected final LogsMapper logsMapper;
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected AuthenticationProfileModel authenticationProfileModel;

    public BaseController(LogsMapper logsMapper) {
        this.logsMapper = logsMapper;
    }

    /**
     * @param mock
     * @return
     * @desc generate mock response base mock data
     */
    protected ResponseEntity generateMockResponseEntity(MockHelper mock) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (mock.getResponse().getHeaders() != null) {
            for (Map.Entry headerMap : mock.getResponse().getHeaders().entrySet()) {
                httpHeaders.add(headerMap.getKey().toString(), headerMap.getValue().toString());
            }
        }
        return ResponseEntity.status(mock.getResponse().getHttpCode()).headers(httpHeaders).body(mock.getResponse().getResponse());
    }


    protected ResponseEntity<Object> handlingErrorResponse(Exception ex) {
        LOGGER.error(ex.getMessage());
        if (ex instanceof DuplicateDataEntry) {
            return this.responseHandling(((DuplicateDataEntry) ex).getErrorVmodel());
        } else if (ex instanceof InvalidMockException) {
            return this.responseHandling(((InvalidMockException) ex).getErrorVmodel());
        } else if (ex instanceof NotFoundException) {
            return this.responseHandling(((NotFoundException) ex).getErrorVmodel());
        } else if (ex instanceof UnathorizedAccess) {
            return this.responseHandling(((UnathorizedAccess) ex).getErrorVmodel());
        }
        LOGGER.error("exception occured", ex);
        return ResponseEntity.status(ResponseCode.GLOBAL_ERROR_MESSAGE.getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(ex.getMessage())
                        .responseCode(ResponseCode.GLOBAL_ERROR_MESSAGE.getErrorCode()).build());
    }


    private ResponseEntity<Object> responseHandling(ErrorVmodel errorVmodel) {
        return ResponseEntity.status(errorVmodel.getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(errorVmodel.getErrorMessage())
                        .responseCode(errorVmodel.getErrorCode()).build());
    }

    public AuthenticationProfileModel getAuthenticationProfileModel() {
        return (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
