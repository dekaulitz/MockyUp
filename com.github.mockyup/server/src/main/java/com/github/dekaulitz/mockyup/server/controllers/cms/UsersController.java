package com.github.dekaulitz.mockyup.server.controllers.cms;

import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.COUNT;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.USERS;
import static com.github.dekaulitz.mockyup.server.model.constants.ApplicationConstants.V1;

import com.github.dekaulitz.mockyup.server.controllers.BaseController;
import com.github.dekaulitz.mockyup.server.errors.ServiceException;
import com.github.dekaulitz.mockyup.server.facade.UsersFacade;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import com.github.dekaulitz.mockyup.server.model.request.user.CreateUserRequest;
import com.github.dekaulitz.mockyup.server.model.request.user.UpdateUserRequest;
import com.github.dekaulitz.mockyup.server.model.response.ResponseModel;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(V1)
public class UsersController extends BaseController {

  @Autowired
  private UsersFacade usersFacade;

  @PreAuthorize("hasAuthority('USERS_READ_WRITE')")
  @PostMapping(value = USERS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserRequest createUserRequest,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.usersFacade.createUser(createUserRequest),
            mandatoryModel));
  }

  @PreAuthorize("hasAuthority('USERS_READ_WRITE')")
  @PutMapping(value = USERS + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateUser(@PathVariable String id,
      @RequestBody @Valid UpdateUserRequest updateUserRequest,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {

    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.usersFacade.updateUser(id, updateUserRequest),
            mandatoryModel));
  }

  @PreAuthorize("hasAuthority('USERS_READ_WRITE')")
  @DeleteMapping(value = USERS + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateUser(@PathVariable String id,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {
    this.usersFacade.getUserService().deleteById(id);
    return ResponseEntity.ok(ResponseModel.initSuccessResponse(null, mandatoryModel));
  }


  @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
  @GetMapping(value = USERS, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAll(@ModelAttribute MandatoryModel mandatoryModel,
      @Valid GetUserParam getUserParam) throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.usersFacade.allUsers(getUserParam), mandatoryModel));
  }

  @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
  @GetMapping(value = USERS + COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getCount(@ModelAttribute MandatoryModel mandatoryModel,
      @Valid GetUserParam getUserParam) throws ServiceException {
    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.usersFacade.getCount(getUserParam), mandatoryModel));
  }

  @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
  @GetMapping(value = USERS + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getUserById(@PathVariable String id,
      @ModelAttribute MandatoryModel mandatoryModel)
      throws ServiceException {

    return ResponseEntity.ok(
        ResponseModel.initSuccessResponse(this.usersFacade.getUserDetail(id), mandatoryModel));
  }
}
