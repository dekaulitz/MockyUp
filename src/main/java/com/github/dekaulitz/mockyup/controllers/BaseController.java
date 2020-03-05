package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.models.helper.MockExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
        HttpHeaders httpHeaders= new HttpHeaders();
        if (mock.getResponse().getHeaders() != null) {
            for (Map.Entry headerMap : mock.getResponse().getHeaders().entrySet()) {
                httpHeaders.add(headerMap.getKey().toString(),headerMap.getValue().toString());
            }
        }
        return ResponseEntity.status(mock.getResponse().getHttpCode()).headers(httpHeaders).body(mock.getResponse().getResponse());
    }

}
