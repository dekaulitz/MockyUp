package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.models.helper.MockExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * @param mock
     * @return
     * @desc generate mock response base mock data
     */
    protected ResponseEntity<Object> generateMockResponseEntity(MockExample mock) {
        return new ResponseEntity<>(mock.getResponse().getResponse(), HttpStatus.valueOf(mock.getResponse().getHttpCode()));
    }
}
