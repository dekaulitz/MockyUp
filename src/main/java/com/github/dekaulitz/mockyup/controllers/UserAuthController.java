package com.github.dekaulitz.mockyup.controllers;

import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.errorhandlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.models.UserModel;
import com.github.dekaulitz.mockyup.utils.JwtManager;
import com.github.dekaulitz.mockyup.vmodels.UserLoginVmodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
public class UserAuthController extends BaseController {
    @Autowired
    private final UserModel userModel;

    public UserAuthController(LogsMapper logsMapper, UserModel userModel) {
        super(logsMapper);
        this.userModel = userModel;
    }


    @PostMapping(value = "/mocks/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestBody UserLoginVmodel vmodel) {
        try {
            return ResponseEntity.ok(this.userModel.doLogin(vmodel));
        } catch (UnathorizedAccess unathorizedAccess) {
            return this.handlingErrorResponse(unathorizedAccess);
        } catch (UnsupportedEncodingException e) {
            return this.handlingErrorResponse(e);
        }
    }

    @GetMapping(value = "/mocks/auth/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> logOut(HttpServletRequest request) {
        try {
            String authorization = JwtManager.getAuthorizationHeader(request);
            return ResponseEntity.ok(this.userModel.refreshToken(authorization));
        } catch (UnsupportedEncodingException e) {
            return this.handlingErrorResponse(e);
        }
    }

}
