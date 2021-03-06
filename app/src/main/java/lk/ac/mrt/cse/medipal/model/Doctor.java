package lk.ac.mrt.cse.medipal.model;


import lk.ac.mrt.cse.medipal.constant.Common;

/**
 * Created by chand on 2017-06-13.
 */

public class Doctor {
    private String registration_id;
    private String speciality;
    private String name;
    private String gender;
    private String email;
    private String mobile;
    private String password;
    private String image;
    private boolean history_shared;

    public Doctor(String registration_id, String speciality, String name, String gender, String email, String mobile, String password, String image) {
        this.registration_id = registration_id;
        this.speciality = speciality;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.image = image;
    }

    public Doctor() {
    }

    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        if (image == null){
            if (gender.equals(Common.MALE_TXT)) {
                image = Common.URL.ICON_USER_MALE;
            } else {
                image = Common.URL.ICON_USER_FEMALE;
            }
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHistory_shared() {
        return history_shared;
    }

    public void setHistory_shared(boolean history_shared) {
        this.history_shared = history_shared;
    }
}
