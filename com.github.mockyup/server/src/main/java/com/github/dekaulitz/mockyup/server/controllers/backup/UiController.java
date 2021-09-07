package com.github.dekaulitz.mockyup.server.controllers.backup;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UiController extends BaseController {

  /**
   * entry point for ui
   *
   * @return String
   */
  @GetMapping(value = "/")
  public String loadUi() {
    return "index";
  }

  /**
   * history path handler for handling spa
   **/
  @RequestMapping(value = "/**/{[path:[^\\.]*}")
  public String redirect() {
    // Forward to home page so that route is preserved.
    return "forward:/";
  }
}
