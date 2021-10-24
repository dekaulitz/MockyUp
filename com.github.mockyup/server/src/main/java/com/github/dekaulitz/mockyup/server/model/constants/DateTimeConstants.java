package com.github.dekaulitz.mockyup.server.model.constants;

public class DateTimeConstants {

  /**
   * Standard format for display Date without Dash
   */
  public static final String DATE_FORMAT_WITHOUT_DASH = "dd MM yyyy";
  public static final String DATE_FORMAT_CONCATED = "ddMMyyyy";
  /**
   * Standard format for Date QueryParam
   */
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  /**
   * Standard format for DateTime QueryParam
   */
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  /**
   * Standard format for Time QueryParam
   */
  public static final String TIME_FORMAT = "HH:mm";
  /**
   * Standard format for displaying Time. Example: 00:00:00.
   */
  public static final String TIME_FORMAT_WITH_SECOND = "HH:mm:ss";
  /**
   * Standard format for displaying Date. Example: Monday, 6 March 2016.
   */
  public static final String DATE_DISPLAY_FORMAT = "EEEE, dd MMMM yyyy";
  /**
   * Standard format for displaying Date. Example: 6 March 2016.
   */
  public static final String DATE_DISPLAY_FORMAT_WITHOUT_DAY = "dd MMMM yyyy";
  /**
   * Standard format for displaying Date. Example: 6 Oct 2016.
   */
  public static final String DATE_FORMAT_WITH_THREE_DIGIT_MONTH = "dd MMM yyyy";
  /**
   * Standard format for displaying Date and Time. Example: 11:00, Monday, 6 March 2016.
   */
  public static final String DATE_TIME_DISPLAY_FORMAT = "HH:mm, EEEE, dd MMMM yyyy";
  /**
   * Standard format for displaying Date and Time. Example: Monday, 6 March 2016, 11:00.
   */
  public static final String DATE_TIME_DISPLAY_FORMAT2 = "EEEE, dd MMMM yyyy HH:mm";
  /**
   * Standard format for displaying Date, for customer voucher and email. Example: Mon, 6 Oct 2016.
   */
  public static final String DATE_FORMAT_WITH_FULL_TEXT = "EEEE, dd MMMM yyyy";
  /**
   * Standard format for displaying Date. Example: Mon, 6 Oct 2016.
   */
  public static final String DATE_FORMAT_WITH_THREE_DIGIT_DAY = "E, dd MMM yyyy";
  /**
   * Standard format for displaying Date and Time. Example: Mon, 6 Oct 2016 00:00:00.
   */
  public static final String DATE_TIME_WITH_THREE_DIGIT_DAY =
      DATE_FORMAT_WITH_THREE_DIGIT_DAY + " " + TIME_FORMAT_WITH_SECOND;
  /**
   * Standard format for displaying Date. Example: Mon, 6 Oct.
   */
  public static final String DATE_FORMAT_WITH_THREE_DIGIT_DAY_WITHOUT_YEAR = "E, dd MMM";

  private DateTimeConstants() {
  }
}
