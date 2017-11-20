package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.DataWriteResponse;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/12/17.
 */

public class PrescriptionController extends AbstractController{
    public void getPatientPrescriptionList(Callback<ListWrapper<Prescription>> prescriptionListCallBack, String nic) {
        medipalAPI.patientPrescriptionList(nic).enqueue(prescriptionListCallBack);
    }

    public void getLastPrescriptionOfDisease(Callback<Prescription> lastPrescriptionCallBack, String nic, String disease_id) {
        medipalAPI.lastPrescriptionofDisease(nic, disease_id).enqueue(lastPrescriptionCallBack);
    }

    public void savePrescription(Callback<DataWriteResponse> prescriptionSavingCallBack, Prescription prescription) {
        medipalAPI.savePrescription(prescription).enqueue(prescriptionSavingCallBack);
    }

    public void levelUpPrescription(Callback<ListWrapper<Drug>> doctorListCallBack, String nic, String disease_name) {
        medipalAPI.levelUpPrescription(nic, disease_name).enqueue(doctorListCallBack);
    }

    public void levelDownPrescription(Callback<ListWrapper<Drug>> doctorListCallBack, String nic, String disease_name) {
        medipalAPI.levelDownPrescription(nic, disease_name).enqueue(doctorListCallBack);
    }

}
