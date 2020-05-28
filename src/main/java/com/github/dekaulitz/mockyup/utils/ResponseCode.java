package com.github.dekaulitz.mockyup.utils;

import com.github.dekaulitz.mockyup.errorhandlers.ErrorModel;
import org.springframework.http.HttpStatus;

public class ResponseCode {

    public static ErrorModel TOKEN_INVALID = new ErrorModel(HttpStatus.UNAUTHORIZED, "MOCK010", "token invalid");
    public static ErrorModel REFRESH_TOKEN_REQUIRED = new ErrorModel(HttpStatus.FORBIDDEN, "MOCK011", "refresh token required");
    public static ErrorModel TOKEN_EXPIRED = new ErrorModel(HttpStatus.FORBIDDEN, "MOCK012", "token expired please login");
    public static ErrorModel TOKEN_NOT_FOUND = new ErrorModel(HttpStatus.FORBIDDEN, "MOCK013", "token expired please login");
    public static ErrorModel INVALID_USERNAME_OR_PASSWORD = new ErrorModel(HttpStatus.UNAUTHORIZED, "MOCK014", "invalid username or password");


    public static ErrorModel MOCKUP_NOT_FOUND = new ErrorModel(HttpStatus.BAD_REQUEST, "MOCK020", "mockup not found");
    public static ErrorModel INVALID_MOCKUP_STRUCTURE = new ErrorModel(HttpStatus.BAD_REQUEST, "MOCK021", "invalid mockup structure");


    public static ErrorModel GLOBAL_ERROR_MESSAGE = new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR, "MOCK001", "something bad happen");


}
