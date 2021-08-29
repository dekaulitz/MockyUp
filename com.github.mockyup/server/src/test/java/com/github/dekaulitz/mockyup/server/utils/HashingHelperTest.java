package com.github.dekaulitz.mockyup.server.utils;

import com.github.dekaulitz.mockyup.server.service.mockup.helper.HashingHelper;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;


class HashingHelperTest {

  @Test
  void verifyHash() {
    String password = "djancuk";
    String hashingPassword = HashingHelper.hashing(password);
    Assert.isTrue(!hashingPassword.isEmpty(), "hashing password is empty");
    Boolean isValid = HashingHelper.verifyHash(password, hashingPassword);
    Assert.isTrue(isValid, "hashing verify is not expected");
  }
}
