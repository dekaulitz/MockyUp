package com.github.dekaulitz.mockyup.server.service.common.helper.constants;

import com.github.dekaulitz.mockyup.server.model.embeddable.Message;
import lombok.Getter;
import lombok.Setter;

public enum ResponseCode {
  INVALID_USER_LOGIN, DATA_NOT_FOUND, INVALID_MOCK_CONTRACT, MOCK_NOT_FOUND, UNSUPPORTED_MOCK_TYPE, TOKEN_NOT_VALID, REFRESH_TOKEN_REQUIRED, UNAUTHORIZED_ACCESS;


  @Getter
  @Setter
  private Message value;

  ResponseCode(Message value) {
    this.value = value;
  }

  ResponseCode() {
  }


}
