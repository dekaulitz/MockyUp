package com.github.dekaulitz.mockyup.infrastructure.errors.handlers;

import com.github.dekaulitz.mockyup.utils.ResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class NotFoundExceptionTest {

  @Test
  void getErrorVmodel() {
    try {
      Exception exception = new Exception("bad");
      throw new NotFoundException(ResponseCode.USER_ALREADY_EXIST, exception);
    } catch (NotFoundException e) {
      Assert.isTrue(e.getErrorVmodel().getErrorMessage().equals("bad"), "exception not expected");
      Assert.isTrue(
          e.getErrorVmodel().getErrorCode().equals(ResponseCode.USER_ALREADY_EXIST.getErrorCode()),
          "error code not expected");
      Assert.isTrue(
          e.getErrorVmodel().getHttpCode().value() == ResponseCode.USER_ALREADY_EXIST.getHttpCode()
              .value(), "http code not expected");

    }
  }
}
