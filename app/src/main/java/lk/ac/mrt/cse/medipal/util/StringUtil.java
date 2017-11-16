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

    public static String getPreNumericValue(String string){
        return string.replaceAll("\\D+","");
    }

    public static String getPostAlphebeticValue(String string){
        return string.replaceAll("[^A-Za-z]+", "");
    }
    public static String getFrequencyUnit(String frequency_txt){
        String[] parts =frequency_txt.split("/");
        if (parts.length > 1) {
            return parts[parts.length-1];
        }
        return frequency_txt;
    }

}
