package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.base.controller.BaseController;
import com.github.dekaulitz.mockyup.domain.users.base.UserInterface;
import com.github.dekaulitz.mockyup.domain.users.vmodels.RegistrationResponseVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.infrastructure.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.infrastructure.db.repositories.paging.UserEntitiesPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class UserControllers extends BaseController {
    @Autowired
    private final UserInterface userInterface;

    public UserControllers(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE')")
    @PostMapping(value = "/mocks/addUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> adduser(@Valid @RequestBody RegistrationVmodel vmodel, HttpServletRequest request) {
        try {
            UserEntities userEntities = this.userInterface.addUser(vmodel, this.getAuthenticationProfileModel());
            RegistrationResponseVmodel registrationResponseVmodel = RegistrationResponseVmodel
                    .builder().accessList(userEntities.getAccessList())
                    .id(userEntities.getId())
                    .username(userEntities.getUsername()).build();
            return ResponseEntity.ok(registrationResponseVmodel);
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE')")
    @DeleteMapping(value = "/mocks/user/{id}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteUser(@PathVariable String id, HttpServletRequest request) {
        try {
            this.userInterface.deleteUser(id, this.getAuthenticationProfileModel());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }

    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
    @GetMapping(value = "/mocks/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsersPagination(
            Pageable pageable,
            @RequestParam(value = "q", required = false) String q, HttpServletRequest request) {
        try {
            UserEntitiesPage pagingVmodel = this.userInterface.paging(pageable, q);
            return ResponseEntity.ok(pagingVmodel);
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
    @GetMapping(value = "/mocks/users/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserList(@RequestParam(value = "username", required = false) String username, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(this.userInterface.listUsers(username, this.getAuthenticationProfileModel()));
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE')")
    @PutMapping(value = "/mocks/users/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUsers(@Valid @RequestBody UpdateUserVmodel vmodel, @PathVariable String id, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(this.userInterface.updateUser(vmodel, id));
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }


    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
    @GetMapping(value = "/mocks/users/{id}/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable String id, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(this.userInterface.getUserById(id));
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

}
