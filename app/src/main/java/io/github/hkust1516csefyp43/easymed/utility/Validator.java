package io.github.hkust1516csefyp43.easymed.utility;

import java.util.regex.Pattern;

/**
 * Created by Louis on 20/4/16.
 */
public class Validator {

  /**
   * Validate if the email is at least possible (Because we can't do actual email sign up TAT)
   * @param email
   * @return true if it is valid
   */
  public static boolean email(String email) {
    return Pattern.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", email);
  }

  /**
   * Currently it checks if the token is a string with length 16 compose of a-z, A-Z or 0-9
   * @param token
   * @return true if it is a valid token
   */
  public static boolean token(String token) {
    return Pattern.matches("\\b\\w{16}\\b", token);
  }
}
