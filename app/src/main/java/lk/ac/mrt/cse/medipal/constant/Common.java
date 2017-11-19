package lk.ac.mrt.cse.medipal.constant;

/**
 * Created by Lakshan on 2017-05-30.
 */

public class Common {
    public static final String TRUE_TXT = "true";
    public static final String FALSE_TXT = "false";
    public static final String MALE_TXT = "Male";
    public static final String FEMALE_TXT = "Female";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TAB_INFORMATION = "Information";
    public static final String TAB_PRESCRITIONS = "Prescriptions";
    public static final String ERROR_NETWORK = "Network Failure. Check your connection";
    public static final String ERROR_OCCURED_TXT = "Error Occured: ";
    public static final String DOSAGE_TXT_VALUE = "%s Units %s";
    public static final String DURATION_TXT_VALUE = "%s days From %s";
    public static class Prescription {
        public static final String UNITS_TXT = "Unit(s)";
        public static final String DAYS_TXT = "days";
        public static final String WEEKS_TXT = "weeks";
        public static final String MONTHS_TXT = "months";
        public static final String USE_TIME_AFTER_MEAL = "After Meal";
        public static final String USE_TIME_BEFORE_MEAL = "Before Meal";
        public static final String TIMES_TXT = "Times/";
        public static final String FROM_TXT = "from";
        public static final String TXT_SHOW_ALTERNATIVES = "Show Alternatives";
        public static final String TXT_HIDE_ALTERNATIVES = "Hide Alternatives";
        public static final int DAYS_IN_WEEK = 7;
        public static final int DAYS_IN_MONTH = 30;

    }
    public static class URL {
        public static final String ICON_USER_MALE = "http://medipal.projects.mrt.ac.lk/images/profile/icon_user_male.png";
        public static final String ICON_USER_FEMALE = "http://medipal.projects.mrt.ac.lk/images/profile/icon_user_female.png";
        public static final String BASE_URL = "http://medipal.projects.mrt.ac.lk";
    }

    public static class AllergyTypes{
        public static final String MINOR = "Minor";
        public static final String MEDIUM = "Medium";
        public static final String MAJOR = "Major";

    }

    public static class Time{
        public static final String DAYS_AGO = "%d days ago";
        public static final String HOURS_AGO = "%d hours ago";
        public static final String MINS_AGO = "%d minutes ago";
        public static final String SECONDS_AGO = "%d seconds ago";
        public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    }

}
