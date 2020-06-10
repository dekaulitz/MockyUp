package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.models.UserModel;
import com.github.dekaulitz.mockyup.vmodels.RegistrationResponseVmodel;
import com.github.dekaulitz.mockyup.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.vmodels.UpdateUserVmodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserControllers extends BaseController {
    @Autowired
    private final UserModel userModel;

    public UserControllers(LogsMapper logsMapper, UserModel userModel) {
        super(logsMapper);
        this.userModel = userModel;
    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE')")
    @PostMapping(value = "/mocks/addUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> adduser(@RequestBody RegistrationVmodel vmodel) {
        try {
            AuthenticationProfileModel authenticationProfileModel = (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserEntities userEntities = this.userModel.addUser(vmodel, authenticationProfileModel);
            RegistrationResponseVmodel registrationResponseVmodel = RegistrationResponseVmodel
                    .builder().accessList(userEntities.getAccessList())
                    .id(userEntities.getId())
                    .username(userEntities.getUsername()).build();
            return ResponseEntity.ok(registrationResponseVmodel);
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }

    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
    @GetMapping(value = "/mocks/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsersPagination(
            Pageable pageable,
            @RequestParam(value = "q", required = false) String q) {
        try {
            UserEntitiesPage pagingVmodel = this.userModel.paging(pageable, q);
            return ResponseEntity.ok(pagingVmodel);
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
    @GetMapping(value = "/mocks/users/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserList(@RequestParam(value = "username", required = false) String username) {
        try {
            AuthenticationProfileModel authenticationProfileModel = (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok(this.userModel.listUsers(username, authenticationProfileModel));
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE')")
    @PutMapping(value = "/mocks/users/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUsers(@RequestBody UpdateUserVmodel vmodel, @PathVariable String id) {
        try {
            AuthenticationProfileModel authenticationProfileModel = (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok(this.userModel.updateUser(vmodel, id));
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }


    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
    @GetMapping(value = "/mocks/users/{id}/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(this.userModel.getUserById(id));
        } catch (Exception e) {
            return this.handlingErrorResponse(e);
        }
    }

}
