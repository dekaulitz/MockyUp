package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController extends BaseController {

    public UiController(LogsMapper logsMapper) {
        super(logsMapper);
    }

    @GetMapping(value = "/")
    public String loadUi() {
        return "index";
    }


}
