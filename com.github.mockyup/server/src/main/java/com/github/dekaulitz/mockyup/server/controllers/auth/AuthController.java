package com.github.dekaulitz.mockyup.server.controllers.auth;

import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.LOGIN;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.LOGOUT;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.V1;

import com.github.dekaulitz.mockyup.server.controllers.BaseController;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.facade.AuthFacade;
import com.github.dekaulitz.mockyup.server.model.dto.AuthProfileModel;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import com.github.dekaulitz.mockyup.server.model.request.auth.UserLoginRequest;
import com.github.dekaulitz.mockyup.server.model.response.auth.AuthResponseModel;
import com.github.dekaulitz.mockyup.server.model.response.ResponseModel;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(V1)
public class AuthController extends BaseController {

  @Autowired
  private AuthFacade authFacade;

  @PostMapping(value = LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> login(@Valid @RequestBody UserLoginRequest userLoginRequest,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    AuthProfileModel authProfileModel = this.authFacade.login(userLoginRequest, mandatoryModel);
    return ResponseEntity.ok(ResponseModel.initSuccessResponse(AuthResponseModel.builder()
        .access(authProfileModel.getAccess())
        .accessToken(authProfileModel.getToken())
        .username(authProfileModel.getUsername())
        .build(), mandatoryModel));
  }

  @PutMapping(value = LOGOUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> logOut(
      @RequestHeader String authorization, @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    this.authFacade.logout(authorization);
    return ResponseEntity.ok(ResponseModel.initSuccessResponse(null, mandatoryModel));
  }
}
