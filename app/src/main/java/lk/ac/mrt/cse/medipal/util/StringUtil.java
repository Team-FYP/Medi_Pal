package lk.ac.mrt.cse.medipal.util;

/**
 * Created by lakshan on 11/12/17.
 */

public class StringUtil {
    public static String getDateIn2LineFormat(String date){
        String[] substrings = date.split("-");
        if (substrings.length == 3) {
            String year = substrings[0];
            String month = substrings[1];
            String day = substrings[2];
            date = year + "\n" + month + "/" + day;
        }
        return date;
    }

    public static String getDoctorName (String doctorName){
        return "Dr. "+ doctorName;
    }


}
