package com.github.dekaulitz.mockyup.infrastructure.errors.handlers;

import com.github.dekaulitz.mockyup.utils.ResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class DuplicateDataEntryTest {

  @Test
  void getErrorVmodel() {
    try {
      Exception exception = new Exception("bad");
      throw new DuplicateDataEntry(ResponseCode.USER_ALREADY_EXIST, exception);
    } catch (DuplicateDataEntry duplicateDataEntry) {
      Assert.isTrue(duplicateDataEntry.getErrorVmodel().getErrorMessage().equals("bad"),
          "exception not expected");
      Assert.isTrue(duplicateDataEntry.getErrorVmodel().getErrorCode()
          .equals(ResponseCode.USER_ALREADY_EXIST.getErrorCode()), "error code not expected");
      Assert.isTrue(duplicateDataEntry.getErrorVmodel().getHttpCode().value()
          == ResponseCode.USER_ALREADY_EXIST.getHttpCode().value(), "http code not expected");
    }
  }
}
