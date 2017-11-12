package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/12/17.
 */

public class PrescriptionController extends AbstractController{
    public void getPatientPrescriptionList(Callback<ListWrapper<Prescription>> prescriptionListCallBack, String nic) {
        medipalAPI.patientPrescriptionList(nic).enqueue(prescriptionListCallBack);
    }
}
