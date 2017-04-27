package io.github.hkust1516csefyp43.easymed.utility;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import io.github.hkust1516csefyp43.easymed.pojo.server_response.Patient;
import io.github.hkust1516csefyp43.easymed.utility.Const.BMI;
import io.github.hkust1516csefyp43.easymed.utility.Const.BMI.WeightForAgeStatus;


/**
 * Created by Louis on 5/11/15.
 */
public class Util {
  private static final String TAG = Util.class.getSimpleName();

  public final static int HOW_MANY_MS_IN_S = 1000;
  public final static int HOW_MANY_S_IN_MIN = 60;
  public final static int HOW_MANY_MIN_IN_HOUR = 60;
  public final static int HOW_MANY_HOUR_IN_DAY = 24;
  public final static int HOW_MANY_DAY_IN_WEEK = 7;
  public final static long MILLISECOND = 1;
  public final static long SECOND = HOW_MANY_MS_IN_S * MILLISECOND;
  public final static long MINUTE = HOW_MANY_S_IN_MIN * SECOND;
  public final static long HOUR = HOW_MANY_MIN_IN_HOUR * MINUTE;
  public final static long DAY = HOW_MANY_HOUR_IN_DAY * HOUR;
  public final static long WEEK = HOW_MANY_DAY_IN_WEEK * DAY;
  public final static long MONTH = (long) (30.4375 * DAY);
  public final static long YEAR = (long) (365.2422 * DAY);

  private String packageName;

  public static Locale getDefaultLocale() {
    return Locale.ENGLISH;
  }

  public static String lastSeenToString(Date d) {
    Calendar now = Calendar.getInstance();
    long diff = now.getTimeInMillis() - d.getTime();
    return toTimeAgo(diff);
  }

  public static double kgTolb(double kg) {
    return kg / 0.45359237;
  }

  public static double lbToKg(double lb) {
    return lb * 0.45359237;
  }

  public static double celsiusToFahrenheit(double c) {
    return (c * 1.8 + 32);
  }

  public static double fahrenheitToCelsius(double f) {
    return ((f - 32) / 1.8);
  }

  public static double roundDouble (double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  public static String toTimeAgo(long dateMsDiff) {
    if (dateMsDiff < MINUTE)
      return "Just now";
    else if (dateMsDiff < 2 * MINUTE)
      return "A minute ago";
    else if (dateMsDiff < 50 * MINUTE)
      return (dateMsDiff / MINUTE + " minutes ago");
    else if (dateMsDiff < 80 * MINUTE)
      return "An hour ago";
    else if (dateMsDiff < 24 * HOUR)
      return (dateMsDiff / HOUR + " hours ago");
    else if (dateMsDiff < 36 * HOUR)
      return ("A day ago");
    else if (dateMsDiff < WEEK)
      return (dateMsDiff / DAY + " days ago");
    else if (dateMsDiff < 2 * WEEK)
      return "A week ago";
    else if (dateMsDiff < MONTH)
      return (dateMsDiff / WEEK + " weeks ago");
    else if (dateMsDiff < 2 * MONTH)
      return "A month ago";
    else if (dateMsDiff < YEAR)
      return (dateMsDiff / MONTH + " months ago");
    else if (dateMsDiff < 2 * YEAR)
      return "A year ago";
    else return (dateMsDiff / YEAR + " years ago");
  }

  /**
   * calculate age for PersonalDataFragment
   * @param year
   * @param month 0-11
   * @param day
   * @return int array of 3: year, month and week
   */
  public static int[] birthdayToAgeYMW(int year, int month, int day) {
    int[] ageArray = new int[3];  //Year Month Day/Week
    GregorianCalendar now = new GregorianCalendar();
    ageArray[0] = now.get(Calendar.YEAR) - year;
    ageArray[1] = now.get(Calendar.MONTH) - month;
    ageArray[2] = now.get(Calendar.DAY_OF_MONTH) - day;
    if (ageArray[2] < 0)
      ageArray[1]--;
    if (ageArray[1] < 0) {
      ageArray[0] --;
      ageArray[1] = 12 + ageArray[1];
    }
    if (ageArray[0] < 0)
      return null;        //error
    ageArray[2] = ageArray[2] / 7;
    return ageArray;
  }

  /**
   * @param year  of birthday (the normal 4 digit format that human uses)
   * @param month of birthday (1-12, not 0-11)
   * @param day   of birthday (1-31)
   * @return a string of age description:
   */
  public static String birthdayToAgeString(int year, int month, int day) {
    GregorianCalendar n = new GregorianCalendar();
    if (year < 1800 || month < 1 || month > 12 || day < 1 || day > 31 || new GregorianCalendar(year, month - 1, day).compareTo(n) != -1) {
      return null;
    } else {
      long now = new GregorianCalendar().getTimeInMillis();
      long bd = new GregorianCalendar(year, month - 1, day).getTimeInMillis();
      long millDiff = now - bd;
      if (millDiff < (48 * HOUR))
        return "1 day old";
      else if (millDiff < (MONTH))
        return (millDiff / DAY + " days old");
      else if (millDiff < (2 * MONTH))
        return "1 month old";
      else if (millDiff < (2 * YEAR))
        return (millDiff / MONTH + " months old");
      else
        return (millDiff / YEAR + " years old");
    }
  }

  public static GregorianCalendar ageToBirthday(@NonNull int year, @Nullable Integer month, @Nullable Integer week) {
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.add(Calendar.YEAR, -year);
    if (month != null)
      gregorianCalendar.add(Calendar.MONTH, -month);
    if (week != null)
      gregorianCalendar.add(Calendar.WEEK_OF_YEAR, -week);
    return gregorianCalendar;
  }

  /**
   * Extract text from drawable
   *
   * @param p
   * @return
   */
  public static String getTextDrawableText(Patient p) {
    if (p != null) {
      StringBuilder output = new StringBuilder();
      if (p.getLastName() != null) {
        output.append(p.getLastName().substring(0,1));
      }
      if (p.getFirstName() != null) {
        output.append(p.getFirstName().substring(0,1));
      }
      if (output.toString().length() < 1 || output.toString().length() > 2) {
        return "?";
      } else {
        return output.toString().toUpperCase(Locale.ENGLISH);
      }
    }
    return "?";
  }

  /**
   * @param s >> the name
   * @return true if the whole name is english + number + space
   */
  public static boolean isValidEnglishChar(String s) {
    for (char c : s.toCharArray()) {
      if ((c != ' ') && (c < '0' || c > 'z' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a')))
        return false;
    }
    return true;
  }

  private static int charToInt(Character c) {
    if (c == null)                      //null >> 1
      return 1;
    int ascii = (int) c;
    if (c == 32)                        //space >> 37
      return 37;
    if (c >= 'a' && c <= 'z')           //
      return ascii - 86;
    if (c >= '0' && c <= '9')
      return ascii - 47;
    else
      return 0;
  }

  /**
   * Round number
   *
   * @param num    is the number you want to round
   * @param length of decimal places you want (e.g. 2 >> #.##)
   */
  public static String roundNumber(double num, int length) {
    String format = "#";
    if (length > 0)
      format += ".";
    for (int i = 0; i < length; i++) {
      format += "#";
    }
    DecimalFormat df = new DecimalFormat(format);
    df.setRoundingMode(RoundingMode.CEILING);
    return df.format(num);
  }

  /**
   * Check if weight vs age is normal
   *
   * @param ageMonths 0-60 (inclusive) i.e. newborn to 5 years old
   * @param weight    of child in kh
   * @param isMale    is true if is male
   * @return Overweight, Normal, Underweight or Super underweight
   */
  public static WeightForAgeStatus getWeightForAgeStatus(int ageMonths, double weight, boolean isMale) {
    if (ageMonths > 60)
      return null;
    double[] list = new double[5];
    if (isMale) { //male
      for (int i = 0; i < 5; i++) {
        list[i] = BMI.weightForAgeBoyUnder5[i][ageMonths];
      }
    } else {    //female
      for (int i = 0; i < 5; i++) {
        list[i] = BMI.weightForAgeGirlUnder5[i][ageMonths];
      }
    }
    int j = 4;
    int position = -1;
    while (j >= 0 && position == -1) {
      if (weight >= list[j])
        position = j;
      else
        j--;
    }
    switch (position) {
      case 0:             //super underweight
        return WeightForAgeStatus.TOO_UNDERWEIGHT;
      case 1:             //underweight
        return WeightForAgeStatus.UNDERWEIGHT;
      case 2:             //normal
      case 3:             //normal
        return WeightForAgeStatus.NORMAL;
      case 4:             //overweight
        return WeightForAgeStatus.OVERWEIGHT;
      default:
        return WeightForAgeStatus.NORMAL;
    }
  }

  public static Date dateForPutClock() {
    GregorianCalendar gc = new GregorianCalendar();
    TimeZone timeZone = gc.getTimeZone();
    Date d = new Date();
    d.setTime(d.getTime()-timeZone.getRawOffset());
    return d;
  }

  public static Calendar DateToCalendar(Date date){
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }

  public static String dateInStringOrToday(Date date) {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    GregorianCalendar today = new GregorianCalendar();
    if (gc.get(Calendar.YEAR) == today.get(Calendar.YEAR) && gc.get(Calendar.MONTH) == today.get(Calendar.MONTH) && gc.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
      return "Today";
    } else {
      return dateInString(gc);
    }
  }

  private static String dateInString(GregorianCalendar gc) {
    return "" + gc.get(Calendar.YEAR) + "-" + (gc.get(Calendar.MONTH) + 1) + "-" + gc.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Give me a GregorianCalendar and I give you a string
   * @param gregorianCalendar
   * @return
   */
  public static String GCInStringForSync(GregorianCalendar gregorianCalendar) {
    return dateInString(gregorianCalendar) + " " + gregorianCalendar.get(Calendar.HOUR_OF_DAY) + ":" + gregorianCalendar.get(Calendar.MINUTE) + ":" + gregorianCalendar.get(Calendar.SECOND);
  }

  public static String dateInString(Date date) {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    return dateInString(gc);
  }

  public static String todayString() {
    GregorianCalendar gc = new GregorianCalendar();
    return dateInString(gc);
  }

  /**
   * Clone from java.util.TimeZone
   * @param builder
   * @param count
   * @param value
   */
  private static void appendNumber(StringBuilder builder, int count, int value) {
    String string = Integer.toString(value);
    for (int i = 0; i < count - string.length(); i++) {
      builder.append('0');
    }
    builder.append(string);
  }

  /**
   * Clone from java.util.TimeZone
   * @param includeGmt
   * @param includeMinuteSeparator
   * @param offsetMillis
   * @return
   */
  public static String createGmtOffsetString(boolean includeGmt, boolean includeMinuteSeparator, int offsetMillis) {
    int offsetMinutes = offsetMillis / 60000;
    char sign = '+';
    if (offsetMinutes < 0) {
      sign = '-';
      offsetMinutes = -offsetMinutes;
    }
    StringBuilder builder = new StringBuilder(9);
    if (includeGmt) {
      builder.append("GMT");
    }
    builder.append(sign);
    appendNumber(builder, 2, offsetMinutes / 60);
    if (includeMinuteSeparator) {
      builder.append(':');
    }
    appendNumber(builder, 2, offsetMinutes % 60);
    return builder.toString();
  }

  public static String todayStringWithTimeZone() {
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    TimeZone timeZone = gregorianCalendar.getTimeZone();
    String date = todayString() + "T00:00:00.000";
    date = date + createGmtOffsetString(false, false, timeZone.getOffset(gregorianCalendar.getTimeInMillis()));
    return date;
  }

  public static String displayNameBuilder(@Nullable String lastName, @NonNull String firstName) {
    StringBuilder name = new StringBuilder();
    if (lastName != null) {
      name.append(lastName);
      name.append(" ");
    }
    name.append(firstName);
    return name.toString();
  }

  /**
   * Get start date so that you can pass to API to get by date range
   * @param year
   * @param month
   * @return example: "2016-3-1T00:00:00.000+0800"
   */
  public static String getMonthStartDateStringWithTimeZone(int year, int month) {
    GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, 1);
    return MessageFormat.format("{0}-{1}-1T00:00:00.000{2}", String.valueOf(year), String.valueOf(month + 1), createGmtOffsetString(false, false, gregorianCalendar.getTimeZone().getOffset(gregorianCalendar.getTimeInMillis())));
  }

  /**
   * Get end date so that you can pass to API to get by date range
   * @param year
   * @param month
   * @return example: "2016-3-31T23:59:59.999+0800"
   */
  public static String getMonthEndDateStringWithTimeZone(int year, int month) {
    GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, 1);
    gregorianCalendar.add(Calendar.MONTH, 1);
    gregorianCalendar.add(Calendar.MILLISECOND, -1);
    return (String.valueOf(gregorianCalendar.get(Calendar.YEAR)) + '-' + String.valueOf(gregorianCalendar.get(Calendar.MONTH) + 1) + '-' + String.valueOf(gregorianCalendar.get(Calendar.DAY_OF_MONTH)) + 'T' + String.valueOf(gregorianCalendar.get(Calendar.HOUR_OF_DAY)) + ':' + String.valueOf(gregorianCalendar.get(Calendar.MINUTE)) + ':' + String.valueOf(gregorianCalendar.get(Calendar.SECOND)) + '.' + String.valueOf(gregorianCalendar.get(Calendar.MILLISECOND)) + createGmtOffsetString(false, false, gregorianCalendar.getTimeZone().getOffset(gregorianCalendar.getTimeInMillis())));
  }

  public static boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      return true;
    }
    return false;
  }

  /* Checks if external storage is available to at least read */
  public static boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      return true;
    }
    return false;
  }

}