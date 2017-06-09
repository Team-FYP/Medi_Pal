package lk.ac.mrt.cse.medipal.model;

/**
 * Created by Lakshan on 2017-06-09.
 */

public class Notification {
    Patient patient;
    String message;

    public Notification() {
    }

    public Notification(String message) {
        this.message = message;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
