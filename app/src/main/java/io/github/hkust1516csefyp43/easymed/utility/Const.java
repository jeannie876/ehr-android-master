package io.github.hkust1516csefyp43.easymed.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Louis on 20/4/16.
 */
public class Const {
  //not inner class >> other
  public final static int SPLASH_DISPLAY_LENGTH = 4000;       //4 seconds of Splash Screen
  public final static int TOKEN_LENGTH = 16;                  //TODO make it programmable >> easier to scale up/down
  public final static String PG_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public final static SimpleDateFormat SDF_PG_DATE_TIME = new SimpleDateFormat(PG_DATE_TIME_PATTERN, Locale.US);
  public final static Gson GsonParserThatWorksWithPGTimestamp = new GsonBuilder().setDateFormat(PG_DATE_TIME_PATTERN).create();
  public final static String EMPTY_STRING = "wqpU5NYWMEw57bjRWZgxCwdSe8YC1FNTqE5GhKHmc6qnyzHZkR2ehkfvWuNdBZCd";    //if I find this in a string, it means it is empty (the possibility of sth having this name is super small)
  public static final int ACTION_TAKE_PICTURE = 0;
  public static final int ACTION_SELECT_PICTURE = 1;
  public static final int ACTION_REMOVE_PICTURE = 2;
  public static final long MAX_TIME_DIFF = 900000;      //15 minutes

  public class SignUp {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
  }

  public class NextStation {
    public static final int TRIAGE = 1;
    public static final int CONSULTATION = 2;
    public static final int PHARMACY = 3;
    public static final int FINISHED = 1;
  }

  public class PatientListPageId {
    public static final int POST_TRIAGE = 1;
    public static final int NOT_YET = 2;
    public static final int PRE_CONSULTATION = 3;
    public static final int POST_CONSULTATION = 4;
    public static final int PRE_PHARMACY = 5;
    public static final int POST_PHARMACY = 6;
    public static final int TRIAGE_SEARCH = 7;
    public static final int CONSULTATION_SEARCH = 8;
  }

  public class StaticPages {
    public static final int KEYWORDS = 1;
    public static final int GENDERS = 2;
    public static final int CLINICS = 3;
    public static final int SUITCASES = 4;
    public static final int COUNTRIES = 5;
    public static final int BLOOD_TYPES = 6;
  }

  public class BundleKey {
    public static final String WHICH_PATIENT_LIST_ID = "mMvUxFsUQF84Fkj4";
    public static final String READ_ONLY_PATIENT = "AhYeXQg86VDvbN1P";
    public static final String EDIT_PATIENT = "KWEpDV7VqbS3j7mw";
    public static final String ON_OR_OFF = "42pKVznKCefTT4Dy";
    public static final String VISIT_ID = "NxUkxT3JPN3c6etw";
    public static final String WHOLE_TRIAGE = "1JCPeR28xeV64hBF";
    public static final String IS_TRIAGE = "GpjBxP987NT5DBWu";
    public static final String WHOLE_VISIT = "c8p1Zm57ZbMm6kv1";
    public static final String WHOLE_CONSULTATION = "EmtnSNvzMU1FRqxp";
    public static final String WHICH_MV_PAGE = "6mx1eqGFCdmjCVua";
    public static final String NAME_SEARCH_NAME = "62FhsX47PSFGMrh5";
    public static final String CURRENT_USER = "dw1adDAKNTtZ36v4";
    public static final String DOCUMENT = "5dkAHg5NM16rCENZ";
    public static final String WHICH_DOCUMENT = "XWp2AGef4PjdAWp1";
    public static final String RELATED_DATA_CATEGORY = "89dVUqPq8gfntqjw";
    public static final String CONSULTATION_ID = "F9pQgwt4Nhym13AK";
    public static final String KEY_TITLE = "hwZc5d5c5TjqBnzK";
    public static final String KEY_PRE_FILL_ITEMS = "rKEBY8SPSuFeu89m";
    public static final String REMARK_DATA = "7jafPg5F2kxNBYRy";
    public static final String CHIEF_COMPLAINT_DATA = "h8NQHFfjSJ6MjKNd";
    public static final String MAYBE_EMPTY = "Wx3DNVZrG5w1FqgX";
    public static final String WHICH_STATIC_DATA = "UE1y13vaS5P4ThvF";
    public static final String IS_QR = "Wfm7PkzNxB4PpWQ1";
  }

  public class CacheKey {
    public static final String CURRENT_CLINIC = "j77RR5CGZQtYvnx1";
    public static final String MY_NOTIFICATIONS = "17PvcUhpCGA8GF1q";
    public static final String CURRENT_USER_INFO = "vYTGy6Mhz3QkMMwy";
    public static final String CURRENT_ACCESS_TOKEN = "E46dv8atAN9W8C6Z";
    public static final String CURRENT_REFRESH_TOKEN = "31FdTekBXpMyXJp2";
    public static final String SYNC_LAST_PULL_FROM_CLOUD = "Dd9fmzhQ2KySfhEK";
    public static final String SYNC_LAST_PUSH_TO_CLOUD = "7eCxzzds6SFxvn8T";
    public static final String SYNC_LAST_PULL_FROM_LOCAL = "7ygbdwupRRYfKSak";
    public static final String SYNC_LAST_PUSH_TO_LOCAL = "dSaVS4UZWDjA8fCs";
    public static final String QUERIES_FROM_CLOUD = "QUWHrSrNzzr3ke2c";
    public static final String QUERIES_FROM_LOCAL = "4VRKhNFW8KnJTaSs";
    public static final String DOCUMENT_TYPES = "cc1wBYa7vmHtGVe3";
    public static final String GENDERS = "96VrgNXBWHQtJkfc";
    public static final String MEDICATIONS = "jusGJldnJUo129F7";
  }

  public static class Database {
    public static final String API_VERSION = "2";
//    public static final String CLOUD_API_BASE_URL_121 = "https://one2one-easymed.herokuapp.com/v" + API_VERSION + "/";
//    public static final String LOCAL_API_BASE_URL_121 = "http://192.168.0.193:3000/v" + API_VERSION + "/";
    public static final String CLOUD_API_BASE_URL_121_dev = "https://ehr-api.herokuapp.com/v" + API_VERSION + "/";
    public static final String LOCAL_API_BASE_URL_121_dev = "http://192.168.0.194:3000/v" + API_VERSION + "/";

    public static String currentAPI = CLOUD_API_BASE_URL_121_dev;
    public static void setCurrentAPI(String api) {
      currentAPI = api;
    }
    public static String getCurrentAPI() {
      return currentAPI;
    }
    public static final int LOCAL = 0;
    public static final int CLOUD = 1;
    public static int currentServerType = CLOUD;
  }

  public class RelatedDataCategory{
    public static final int SCREENING = 1;
    public static final int ALLERGY = 2;
    public static final int DIAGNOSIS = 3;
    public static final int ADVICE = 4;
    public static final int FOLLOW_UP = 5;
    public static final int DRUG_HISTORY = 6;
    public static final int EDUCATION = 7;
    public static final int INVESTIGATION = 8;

  }

  public static class BMI {
    /**
     * BOY
     * http://www.who.int/childgrowth/standards/WFA_boys_0_5_zscores.pdf?ua=1
     * -3, -2, Median, +2, +3 (+-1 does not matter)
     */
    public static double[][] weightForAgeBoyUnder5 = new double[][]{
        {2.1, 2.9, 3.8, 4.4, 4.9, 5.3, 5.7, 5.9, 6.2, 6.4, 6.6, 6.8, 6.9, 7.1, 7.2, 7.4, 7.5, 7.7, 7.8, 8.0, 8.1, 8.2, 8.4, 8.5, 8.6, 8.8, 8.9, 9.0, 9.1, 9.2, 9.4, 9.5, 9.6, 9.7, 9.8, 9.9, 10.0, 10.1, 10.2, 10.3, 10.4, 10.5, 10.6, 10.7, 10.8, 10.9, 11.0, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6, 11.7, 11.8, 11.9, 12.0, 12.1, 12.2, 12.3, 12.4},
        {2.5, 3.4, 4.3, 5.0, 5.6, 6.0, 6.4, 6.7, 6.9, 7.1, 7.4, 7.6, 7.7, 7.9, 8.1, 8.3, 8.4, 8.6, 8.8, 8.9, 9.1, 9.2, 9.4, 9.5, 9.7, 9.8, 10.0, 10.1, 10.2, 10.4, 10.5, 10.7, 10.8, 10.9, 11.0, 11.2, 11.3, 11.4, 11.5, 11.6, 11.8, 11.9, 12.0, 12.1, 12.2, 12.4, 12.5, 12.6, 12.7, 12, 8, 12.9, 13.1, 13.2, 13.3, 13.4, 13.5, 13.6, 13.7, 13.8, 14.0, 14.1},
        {3.3, 4.5, 5.6, 6.4, 7.0, 7.5, 7.9, 8.3, 8.6, 8.9, 9.2, 9.4, 9.6, 9.9, 10.1, 10.3, 10.5, 10.7, 10.9, 11.1, 11.3, 11.5, 11.8, 12.0, 12.2, 12.4, 12.5, 12.7, 12.9, 13.1, 13.3, 13.5, 13.7, 13.8, 14.0, 14.2, 14.3, 14.5, 14.7, 14.8, 15.0, 15.2, 15.3, 15.5, 15.7, 15.8, 16.0, 16.2, 16.3, 16.5, 16.7, 16.8, 17.0, 17.2, 17.3, 17.5, 17.7, 17.8, 18.0, 18.2, 18.3},
        {4.4, 5.8, 7.1, 8.0, 8.7, 9.3, 9.8, 10.3, 10.7, 11.0, 11.4, 11.7, 12.0, 12.3, 12.6, 12.8, 13.1, 13.4, 13.7, 13.9, 14.2, 14.5, 14.7, 15.0, 15.3, 15.5, 15.8, 16.1, 16.3, 16.6, 16.9, 17.1, 17.4, 17.6, 17.8, 18.1, 18.3, 18.6, 18.8, 19.0, 19.3, 19.5, 19.7, 20.0, 20.2, 20.5, 20.7, 20.9, 21.2, 21.4, 21.7, 21.9, 22.2, 22.4, 22.7, 22.9, 23.2, 23.4, 23.7, 23.9, 24.2},
        {5.0, 6.6, 8.0, 9.0, 9.7, 10.4, 10.9, 11.4, 11.9, 12.3, 12.7, 13.0, 13.3, 13.7, 14.0, 14.3, 14.6, 14.9, 15.3, 15.6, 15.9, 16.2, 16.5, 16.8, 17.1, 17.5, 17.8, 18.1, 18.4, 18.7, 19.0, 19.3, 19.6, 19.9, 20.2, 20.4, 20.7, 21.0, 21.3, 21.6, 21.9, 22.1, 22.4, 22.7, 23.0, 23.3, 23.6, 23.9, 24.2, 24.5, 24.8, 25.1, 25.4, 25.7, 26.0, 26.3, 26.6, 26.9, 27.2, 27.6, 27.9}
    };

    /**
     * GIRL
     * http://www.who.int/childgrowth/standards/WFA_girls_0_5_zscores.pdf?ua=1
     */
    public static double[][] weightForAgeGirlUnder5 = new double[][]{
        {2.0, 2.7, 3.4, 4.0, 4.4, 4.8, 5.1, 5.3, 5.6, 5.8, 5.9, 6.1, 6.3, 6.4, 6.6, 6.7, 6.9, 7.0, 7.2, 7.3, 7.5, 7.6, 7.8, 7.9, 8.1, 8.2, 8.4, 8.5, 8.6, 8.8, 8.9, 9.0, 9.1, 9.3, 9.4, 9.5, 9.6, 9.7, 9.8, 9.9, 10.1, 10.2, 10.3, 10.4, 10.5, 10.6, 10.7, 10.8, 10.9, 11.0, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6, 11.7, 11.8, 11.9, 12.0, 12.1},
        {2.4, 3.2, 3.9, 4.5, 5.0, 5.4, 5.7, 6.0, 6.3, 6.5, 6.7, 6.9, 7.0, 7.2, 7.4, 7.6, 7.7, 7.9, 8.1, 8.2, 8.4, 8.6, 8.7, 8.9, 9.0, 9.2, 9.4, 9.5, 9.7, 9.8, 10.0, 10.1, 10.3, 10.4, 10.5, 10.7, 10.8, 10.9, 11.1, 11.2, 11.3, 11.5, 11.6, 11.7, 11.8, 12.0, 12.1, 12.2, 12.3, 12.4, 12.6, 12.7, 12.8, 12.9, 13.0, 13.2, 13.3, 13.4, 13.5, 13.6, 13.7},
        {3.2, 4.2, 5.1, 5.8, 6.4, 6.9, 7.3, 7.6, 7.9, 8.2, 8.5, 8.7, 8.9, 9.2, 9.4, 9.6, 9.8, 10.0, 10.2, 10.4, 10.6, 10.9, 11.1, 11.3, 11.5, 11.7, 11.9, 12.1, 12.3, 12.5, 12.7, 12.8, 13.1, 13.3, 13.5, 13.7, 13.9, 14.0, 14.2, 14.4, 14.6, 14.8, 15.0, 15.2, 15.3, 15.5, 15.7, 15.9, 16.1, 16.3, 16.4, 16.6, 16.8, 17.0, 17.2, 17.3, 17.5, 17.7, 17.9, 18.0, 18.2},
        {4.2, 5.5, 6.6, 7.5, 8.2, 8.8, 9.3, 9.8, 10.2, 10.5, 10.9, 11.2, 11.5, 11.8, 12.1, 12.4, 12.6, 12.9, 13.2, 13.5, 13.7, 14.0, 14.3, 14.6, 14.8, 15.1, 15.4, 15.7, 16.0, 16.2, 16.5, 16.8, 17.1, 17.3, 17.6, 17.9, 18.1, 18.4, 18.7, 19.0, 19.2, 19.5, 19.8, 20.1, 20.4, 20.7, 20.9, 21.2, 21.5, 21.8, 22.1, 22.4, 22.6, 22.9, 23.2, 23.5, 23.8, 24.1, 24.4, 24.6, 24.9},
        {4.8, 6.2, 7.5, 8.5, 9.3, 10.0, 10.6, 11.1, 11.6, 12.0, 12.4, 12.8, 13.1, 13.5, 13.8, 14.1, 14.5, 14.8, 15.1, 15.4, 15.7, 16.0, 16.4, 16.7, 17.0, 17.3, 17.7, 18.0, 18.3, 18.7, 19.0, 19.3, 19.6, 20.0, 20.3, 20.6, 20.9, 21.3, 21.6, 22.0, 22.3, 22.7, 23.0, 23.4, 23.7, 24.1, 24.5, 24.8, 25.2, 25.5, 25.9, 26.3, 26.6, 27.0, 27.4, 27.7, 28.1, 28.5, 28.8, 29.2, 20.5}
    };

    public enum WeightForAgeStatus {
      OVERWEIGHT, NORMAL, UNDERWEIGHT, TOO_UNDERWEIGHT
    }
  }
}
