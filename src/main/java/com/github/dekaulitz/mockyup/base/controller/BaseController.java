package com.github.dekaulitz.mockyup.base.controller;

import com.github.dekaulitz.mockyup.infrastructure.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.DuplicateDataEntry;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.InvalidMockException;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.NotFoundException;
import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.infrastructure.errors.vmodels.ErrorVmodel;
import com.github.dekaulitz.mockyup.infrastructure.errors.vmodels.ResponseVmodel;
import com.github.dekaulitz.mockyup.utils.ConstantsRepository;
import com.github.dekaulitz.mockyup.utils.MockHelper;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseController {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * @param mock
     * @return
     * @desc generate mock response base mock data
     */
    protected ResponseEntity<Object> generateMockResponseEntity(MockHelper mock) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (mock.getResponse().getHeaders() != null) {
            for (Map.Entry headerMap : mock.getResponse().getHeaders().entrySet()) {
                httpHeaders.add(headerMap.getKey().toString(), headerMap.getValue().toString());
            }
        }
        return ResponseEntity.status(mock.getResponse().getHttpCode()).headers(httpHeaders).body(mock.getResponse().getResponse());
    }


    protected ResponseEntity<Object> handlingErrorResponse(Exception ex, HttpServletRequest request) {
        if (ex instanceof DuplicateDataEntry) {
            return this.responseHandling(((DuplicateDataEntry) ex).getErrorVmodel(), request);
        } else if (ex instanceof InvalidMockException) {
            return this.responseHandling(((InvalidMockException) ex).getErrorVmodel(), request);
        } else if (ex instanceof NotFoundException) {
            return this.responseHandling(((NotFoundException) ex).getErrorVmodel(), request);
        } else if (ex instanceof UnathorizedAccess) {
            return this.responseHandling(((UnathorizedAccess) ex).getErrorVmodel(), request);
        } else if (ex instanceof AccessDeniedException) {
            return ResponseEntity.status(ResponseCode.INVALID_ACCESS_PERMISSION.getHttpCode()).body(
                    ResponseVmodel.builder().responseMessage(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorMessage())
                            .responseCode(ResponseCode.INVALID_ACCESS_PERMISSION.getErrorCode())
                            .requestId((String) request.getAttribute(ConstantsRepository.REQUEST_ID))
                            .build());
        }
        LOGGER.error("exception occured " + ex.getMessage(), ex);
        return ResponseEntity.status(ResponseCode.GLOBAL_ERROR_MESSAGE.getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(ex.getMessage())
                        .responseCode(ResponseCode.GLOBAL_ERROR_MESSAGE.getErrorCode())
                        .requestId((String) request.getAttribute(ConstantsRepository.REQUEST_ID))
                        .build());
    }


    private ResponseEntity<Object> responseHandling(ErrorVmodel errorVmodel, HttpServletRequest request) {
        return ResponseEntity.status(errorVmodel.getHttpCode()).body(
                ResponseVmodel.builder().responseMessage(errorVmodel.getErrorMessage())
                        .requestId((String) request.getAttribute(ConstantsRepository.REQUEST_ID))
                        .responseCode(errorVmodel.getErrorCode()).build());
    }

    public AuthenticationProfileModel getAuthenticationProfileModel() {
        return (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
