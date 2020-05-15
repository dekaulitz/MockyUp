package com.github.dekaulitz.mockyup.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

public class Hash {

    public static String hashing(String password) {
        SecureRandom secureRandom = new SecureRandom();
        return BCrypt.hashpw(password, BCrypt.gensalt(10, secureRandom));
    }

    public static boolean verifyHash(String plainPasword, String hashPassword) {
        return BCrypt.checkpw(plainPasword, hashPassword);
    }
}
