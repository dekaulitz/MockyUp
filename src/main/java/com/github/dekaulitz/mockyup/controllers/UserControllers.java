package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.entities.UserEntities;
import com.github.dekaulitz.mockyup.errorhandlers.DuplicateDataEntry;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.errorhandlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.models.UserModel;
import com.github.dekaulitz.mockyup.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.vmodels.RegistrationResponseVmodel;
import com.github.dekaulitz.mockyup.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.vmodels.UserLoginVmodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
public class UserControllers extends BaseController {
    @Autowired
    private final UserModel userModel;

    public UserControllers(LogsMapper logsMapper, UserModel userModel) {
        super(logsMapper);
        this.userModel = userModel;
    }

    @PostMapping(value = "/mocks/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestBody UserLoginVmodel vmodel) {
        try {
            return ResponseEntity.ok(this.userModel.doLogin(vmodel));
        } catch (UnathorizedAccess unathorizedAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(unathorizedAccess.getMessage());
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/mocks/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public void logOut() {
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
        } catch (DuplicateDataEntry duplicateDataEntry) {
            return ResponseEntity.badRequest().body(duplicateDataEntry.getMessage());
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
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
    @GetMapping(value = "/mocks/users/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserList(@RequestParam(value = "q", required = false) String q) {
        try {
            return ResponseEntity.ok(this.userModel.listUsers(q));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE')")
    @PutMapping(value = "/mocks/users/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUsers(@RequestBody UpdateUserVmodel vmodel, @PathVariable String id) {
        try {
            return ResponseEntity.ok(this.userModel.updateUser(vmodel, id));
        } catch (DuplicateDataEntry duplicateDataEntry) {
            return new ResponseEntity<>(duplicateDataEntry.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasAnyAuthority('USERS_READ_WRITE','USERS_READ')")
    @GetMapping(value = "/mocks/users/{id}/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(this.userModel.getUserById(id));
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
