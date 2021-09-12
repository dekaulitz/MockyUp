package com.github.dekaulitz.mockyup.server.model.constants;

public class ApplicationConstants {

  public final static String MANDATORY = "mandatory";
  public final static String X_REQUEST_ID = "x-request-id";
  public final static String X_REQUEST_TIME = "x-request-time";
  public final static String MOCK_REQUEST_PREFIX = "/mocking-path/";
  public final static String MOCK_REQUEST_ID_PREFIX = "/path";

  // api path
  public final static String V1 = "/v1";
  public final static String REGEX_PATH = "/***";
  public final static String LOGIN = "/login";
  public final static String LOGOUT = "/logout";
  public final static String USERS = "/users";
  public final static String PROJECTS = "/projects";
  public final static String PROJECT_CONTRACTS = "/project-contracts";

  private ApplicationConstants() {
  }
}
