package com.github.dekaulitz.mockyup.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;


class HashTest {

    @Test
    void verifyHash() {
        String password = "djancuk";
        String hashingPassword = Hash.hashing(password);
        Assert.isTrue(!hashingPassword.isEmpty(), "hashing password is empty");
        Boolean isValid = Hash.verifyHash(password, hashingPassword);
        Assert.isTrue(isValid, "hashing verify is not expected");
    }
}
