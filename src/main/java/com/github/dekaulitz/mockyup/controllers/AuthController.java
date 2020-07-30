package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.base.controller.BaseController;
import com.github.dekaulitz.mockyup.domain.auth.base.AuthInterface;
import com.github.dekaulitz.mockyup.domain.auth.vmodels.DoAuthVmodel;
import com.github.dekaulitz.mockyup.utils.JwtManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AuthController extends BaseController {
    @Autowired
    private final AuthInterface authInterfaceModel;

    public AuthController(AuthInterface authInterfaceModel) {
        this.authInterfaceModel = authInterfaceModel;
    }

    /**
     * @param vmodel  auth payload user authentication data
     * @param request HttpServletRequest for getting attribute from request
     * @return ResponseEntity
     */
    @PostMapping(value = "/mocks/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@Valid @RequestBody DoAuthVmodel vmodel, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(this.authInterfaceModel.generateAccessToken(vmodel.getUsername(), vmodel.getPassword()));
        } catch (Exception unathorizedAccess) {
            return this.handlingErrorResponse(unathorizedAccess, request);
        }
    }

    /**
     * every token that generated has limited time need to refresh if the token expired
     *
     * @param request HttpServletRequest for getting attribute from request
     * @return ResponseEntity
     */
    @GetMapping(value = "/mocks/auth/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        try {
            String headerAuth = request.getHeader("Authorization");
            String authorization = JwtManager.getAuthorizationHeader(headerAuth);
            return ResponseEntity.ok(this.authInterfaceModel.refreshingToken(authorization));
        } catch (Exception e) {
            return this.handlingErrorResponse(e, request);
        }
    }

}
