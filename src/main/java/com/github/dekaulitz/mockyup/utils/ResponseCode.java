package com.github.dekaulitz.mockyup.utils;

import com.github.dekaulitz.mockyup.infrastructure.errors.vmodels.ErrorVmodel;
import org.springframework.http.HttpStatus;

/**
 * all response code
 */
public class ResponseCode {

  public static ErrorVmodel TOKEN_INVALID = new ErrorVmodel(HttpStatus.UNAUTHORIZED, "MOCK010",
      "Token invalid!");
  public static ErrorVmodel REFRESH_TOKEN_REQUIRED = new ErrorVmodel(HttpStatus.FORBIDDEN,
      "MOCK011", "Refresh token required!");
  public static ErrorVmodel TOKEN_EXPIRED = new ErrorVmodel(HttpStatus.UNAUTHORIZED, "MOCK012",
      "Token expired please login!");
  public static ErrorVmodel INVALID_USERNAME_OR_PASSWORD = new ErrorVmodel(HttpStatus.UNAUTHORIZED,
      "MOCK014", "Invalid username or password!");
  public static ErrorVmodel INVALID_ACCESS_PERMISSION = new ErrorVmodel(HttpStatus.FORBIDDEN,
      "MOCK015", "Invalid access permissions, you dont have access to modify data!");


  public static ErrorVmodel MOCKUP_NOT_FOUND = new ErrorVmodel(HttpStatus.BAD_REQUEST, "MOCK020",
      "Mockup not found!");
  public static ErrorVmodel INVALID_MOCKUP_STRUCTURE = new ErrorVmodel(HttpStatus.BAD_REQUEST,
      "MOCK021", "Invalid mockup structure!");
  public static ErrorVmodel USER_NOT_FOUND = new ErrorVmodel(HttpStatus.BAD_REQUEST, "MOCK022",
      "User not found!");
  public static ErrorVmodel USER_ALREADY_EXIST = new ErrorVmodel(HttpStatus.BAD_REQUEST, "MOCK023",
      "User already exists!");
  public static ErrorVmodel INVALID_MOCK_REF = new ErrorVmodel(HttpStatus.BAD_REQUEST, "MOCK024",
      "Rereference mocks its not valid");
  public static ErrorVmodel INVALID_MOCK_HEADER = new ErrorVmodel(HttpStatus.BAD_REQUEST, "MOCK025",
      "Invalid mock header");
  public static ErrorVmodel INVALID_MOCK_DEFAULT = new ErrorVmodel(HttpStatus.BAD_REQUEST,
      "MOCK026", "Invalid mock default");
  public static ErrorVmodel VALIDATION_FAIL = new ErrorVmodel(HttpStatus.UNPROCESSABLE_ENTITY,
      "MOCK027", "Validation fail");
  public static ErrorVmodel INVALID_MOCKUP_RESPONSE_ISNULL = new ErrorVmodel(
      HttpStatus.UNPROCESSABLE_ENTITY, "MOCK028", "Invlid mock, response is null");


  public static ErrorVmodel GLOBAL_ERROR_MESSAGE = new ErrorVmodel(HttpStatus.INTERNAL_SERVER_ERROR,
      "MOCK001", "Something bad happen");
  public static ErrorVmodel GLOBAL_PAGE_NOT_FOUND = new ErrorVmodel(HttpStatus.NOT_FOUND, "MOCK002",
      "Page not found");


}
