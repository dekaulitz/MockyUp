package com.github.dekaulitz.mockyup.server.service.common.helper.constants;

import com.github.dekaulitz.mockyup.server.model.dto.ErrorMessageModel;
import lombok.Getter;
import lombok.Setter;

public enum ResponseCode {
  INVALID_USER_LOGIN, DATA_NOT_FOUND, INVALID_MOCK_CONTRACT, MOCK_NOT_FOUND, UNSUPPORTED_MOCK_TYPE,
  TOKEN_NOT_VALID, REFRESH_TOKEN_REQUIRED, UNAUTHORIZED_ACCESS, INTERNAL_SERVER_ERROR, BAD_REQUEST, PAGE_NOT_FOUND,
  USER_DISABLED;


  @Getter
  @Setter
  private ErrorMessageModel errorMessageModel;

  ResponseCode(ErrorMessageModel value) {
    this.errorMessageModel = value;
  }

  ResponseCode() {
  }


}
