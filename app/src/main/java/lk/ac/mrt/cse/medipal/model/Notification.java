package lk.ac.mrt.cse.medipal.model;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lk.ac.mrt.cse.medipal.constant.Common;

public abstract class Notification implements Comparable {
    private int  notification_id;
    private Patient patient;
    private Doctor doctor;
    private String message;
    private String status;
    private String time;
    SimpleDateFormat simpleDateFormat;

    public Notification(Patient patient, Doctor doctor, String message, String status, String time) {
        this.patient = patient;
        this.doctor = doctor;
        this.message = message;
        this.status = status;
        this.time = time;
    }

    public Notification() {
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        try {
            Notification notification = (Notification) o;
            int result = 0;
            simpleDateFormat = new SimpleDateFormat(Common.Time.DATE_FORMAT, Locale.ENGLISH);
            Date date1 = simpleDateFormat.parse(notification.getTime());
            Date date2 = simpleDateFormat.parse(this.time);
            long different = date1.getTime() - date2.getTime();
            if (different > 0) {
                return 1;
            } else if (different < 0){
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }
}
