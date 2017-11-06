package lk.ac.mrt.cse.medipal.model.network;

import lk.ac.mrt.cse.medipal.model.Patient;

/**
 * Created by lakshan on 11/6/17.
 */

public class PatientLoginResponse {
    private boolean success;
    private String message;
    private Patient userData;

    public PatientLoginResponse() {
    }

    public PatientLoginResponse(boolean success, String message, Patient userData) {
        this.success = success;
        this.message = message;
        this.userData = userData;
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

    public Patient getUserData() {
        return userData;
    }

    public void setUserData(Patient userData) {
        this.userData = userData;
    }
}
