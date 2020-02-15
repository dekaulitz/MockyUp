package com.github.dekaulitz.mockyup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping(value = "/mocks/ui")
    public String loadClient() {
        return "index";
    }
}
