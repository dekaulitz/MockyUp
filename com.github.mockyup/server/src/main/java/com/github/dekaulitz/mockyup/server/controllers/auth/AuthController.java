package com.github.dekaulitz.mockyup.server.controllers.auth;

import com.github.dekaulitz.mockyup.server.controllers.BaseController;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.facade.AuthFacade;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.request.IssuerRequestModel;
import com.github.dekaulitz.mockyup.server.model.request.UserLoginRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController extends BaseController {

  @Autowired
  private AuthFacade authFacade;
//  @Autowired
//  private ServletRequest servletRequest;


  @PostMapping(value = "/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> login(@Valid @RequestBody UserLoginRequest userLoginRequest,
      HttpServletRequest request) throws ServiceException {
    return ResponseEntity.ok(this.authFacade.login(userLoginRequest));
  }

  @GetMapping(value = "/v1/test", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> sampling(@ModelAttribute IssuerRequestModel issuerRequestModel,
      @ModelAttribute AuthProfileModel authProfileModel)
      throws ServiceException {
//    ServletContext context = servletRequest.getServletContext();
    log.info("{}", issuerRequestModel);
    log.info("{}", authProfileModel);
    return ResponseEntity.ok("ok");
  }


}
