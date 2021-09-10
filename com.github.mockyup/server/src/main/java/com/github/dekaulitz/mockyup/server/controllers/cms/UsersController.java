package com.github.dekaulitz.mockyup.server.controllers.cms;

import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.USERS;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.V1;

import com.github.dekaulitz.mockyup.server.controllers.BaseController;
import com.github.dekaulitz.mockyup.server.db.entities.UserEntity;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.facade.CmsFacade;
import com.github.dekaulitz.mockyup.server.model.dto.Mandatory;
import com.github.dekaulitz.mockyup.server.model.request.CreateUserRequest;
import com.github.dekaulitz.mockyup.server.model.response.ResponseModel;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(V1)
public class UsersController extends BaseController {

  @Autowired
  private CmsFacade cmsFacade;

  @PreAuthorize("hasAuthority('USERS_READ_WRITE')")
  @PostMapping(value = USERS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createUsers(@Valid @RequestBody CreateUserRequest createUserRequest,
      @ModelAttribute Mandatory mandatory)
      throws ServiceException {
    UserEntity userEntity = this.cmsFacade.createUser(createUserRequest);
    return ResponseEntity.ok(ResponseModel.initSuccessResponse(userEntity, mandatory));
  }
}
