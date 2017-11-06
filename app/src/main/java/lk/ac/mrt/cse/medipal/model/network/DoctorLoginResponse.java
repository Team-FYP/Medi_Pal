package lk.ac.mrt.cse.medipal.model.network;

import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;

/**
 * Created by lakshan on 11/2/17.
 */

public class DoctorLoginResponse {
    private boolean success;
    private String message;
    private Doctor userData;

    public DoctorLoginResponse() {
    }

    public DoctorLoginResponse(boolean success, String message, Doctor userData) {
        this.success = success;
        this.message = message;
        this.userData = userData;
    }

    public DoctorLoginResponse(boolean success, String message) {
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

    public Doctor getUserData() {
        return userData;
    }

    public void setUserData(Doctor userData) {
        this.userData = userData;
    }
}
