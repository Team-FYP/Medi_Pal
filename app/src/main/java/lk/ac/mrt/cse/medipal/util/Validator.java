package lk.ac.mrt.cse.medipal.util;

/**
 * Created by lakshan on 11/2/17.
 */

public class Validator {

    public static boolean isNumeric(String s){
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidMobile(CharSequence target) {
        String number = target.toString();
        if(isNumeric(number) && number.matches("07[0-9]{8}")) {
            return true;
        }
        return false;
    }

    public static boolean isValidNIC(CharSequence target) {
        int length = target.length();
        if (length > 12 || length < 10 || Validator.isNumeric(target.toString().substring(length - 1, length)) || !Validator.isNumeric(target.toString().substring(0, length-1).toString())){
            return false;
        }
        return true;
    }

    public static boolean isValidRegID(CharSequence target) {
        int length = target.length();
        if (length > 6 || !Validator.isNumeric(target.toString())){
            return false;
        }
        return true;
    }

    public static boolean isValidPhone(CharSequence target) {
        String number = target.toString();
        if(isNumeric(number) && number.matches("0[0-9]{9}")) {
            return true;
        }
        return false;
    }
}
