package com.github.dekaulitz.mockyup.server.controllers;

import com.github.dekaulitz.mockyup.server.db.entities.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.db.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.server.domain.users.base.UserInterface;
import com.github.dekaulitz.mockyup.server.domain.users.vmodels.RegistrationResponseVmodel;
import com.github.dekaulitz.mockyup.server.domain.users.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.server.domain.users.vmodels.UpdateUserVmodel;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllers extends BaseController {

  @Autowired
  private final UserInterface userInterface;

  public UserControllers(UserInterface userInterface) {
    this.userInterface = userInterface;
  }

  /**
   * registering new user
   *
   * @param vmodel  new user payload data
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE')")
  @PostMapping(value = "/mocks/addUser", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> adduser(@Valid @RequestBody RegistrationVmodel vmodel,
      HttpServletRequest request) {
    try {
      UserEntities userEntities = this.userInterface
          .addUser(vmodel, this.getAuthenticationProfileModel());
      RegistrationResponseVmodel registrationResponseVmodel = RegistrationResponseVmodel
          .builder().accessList(userEntities.getAccessList())
          .id(userEntities.getId())
          .username(userEntities.getUsername()).build();
      return ResponseEntity.ok(registrationResponseVmodel);
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * delete user
   *
   * @param id      id from user collection
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE')")
  @DeleteMapping(value = "/mocks/users/{id}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteUser(@PathVariable String id, HttpServletRequest request) {
    try {
      this.userInterface.deleteUser(id, this.getAuthenticationProfileModel());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }

  }

  /**
   * user pagination
   *
   * @param pageable Spring data pageable
   * @param q        query data example q=name:fahmi => meaning field name with value fahmi
   * @param request  HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
  @GetMapping(value = "/mocks/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getUsersPagination(
      Pageable pageable,
      @RequestParam(value = "q", required = false) String q, HttpServletRequest request) {
    try {
      UserEntitiesPage pagingVmodel = this.userInterface.paging(pageable, q);
      return ResponseEntity.ok(pagingVmodel);
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * for getting all users that can be inserted into mock
   *
   * @param username username
   * @param request  HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
  @GetMapping(value = "/mocks/users/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getUserList(
      @RequestParam(value = "username", required = false) String username,
      HttpServletRequest request) {
    try {
      return ResponseEntity
          .ok(this.userInterface.listUsers(username, this.getAuthenticationProfileModel()));
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * for update user profile
   *
   * @param vmodel  update data user property
   * @param id      id from user collection
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE')")
  @PutMapping(value = "/mocks/users/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUserVmodel vmodel,
      @PathVariable String id, HttpServletRequest request) {
    try {
      return ResponseEntity.ok(this.userInterface.updateUser(vmodel, id));
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

  /**
   * get user detail
   *
   * @param id      id from user collection
   * @param request HttpServletRequest for getting attribute from request
   * @return ResponseEntity
   */
  @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
  @GetMapping(value = "/mocks/users/{id}/detail", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getUser(@PathVariable String id, HttpServletRequest request) {
    try {
      return ResponseEntity.ok(this.userInterface.getUserById(id));
    } catch (Exception e) {
      return this.handlingErrorResponse(e, request);
    }
  }

}
