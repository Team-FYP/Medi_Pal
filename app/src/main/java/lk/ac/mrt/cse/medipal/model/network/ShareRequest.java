package lk.ac.mrt.cse.medipal.model.network;

import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.util.StringUtil;

/**
 * Created by lakshan on 11/18/17.
 */

public class ShareRequest {
    private String patient_id;
    private String doctor_id;
    private String status;

    public ShareRequest() {
    }

    public ShareRequest(String patient_id, String doctor_id, String status) {
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.status = status;
    }

    public ShareRequest(String patient_id, String doctor_id) {
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
