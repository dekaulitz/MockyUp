package com.github.dekaulitz.mockyup.server.service.auth.helper;

import java.security.SecureRandom;
import org.mindrot.jbcrypt.BCrypt;

public class HashingHelper {

  public static String hashing(String password) {
    SecureRandom secureRandom = new SecureRandom();
    return BCrypt.hashpw(password, BCrypt.gensalt(10, secureRandom));
  }

  public static boolean verifyHash(String plainPasword, String hashPassword) {
    return BCrypt.checkpw(plainPasword, hashPassword);
  }
}
