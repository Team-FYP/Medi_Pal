package lk.ac.mrt.cse.medipal.model.network;

import lk.ac.mrt.cse.medipal.model.Patient;

/**
 * Created by lakshan on 11/2/17.
 */

public class LoginResponse <T>{
    boolean success;
    String message;
    T userData;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String message, T userData) {
        this.success = success;
        this.message = message;
        this.userData = userData;
    }

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getUserData() {
        return userData;
    }

    public void setUserData(T userData) {
        this.userData = userData;
    }
}
