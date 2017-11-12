package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/12/17.
 */

public class PrescriptionDrugController extends AbstractController{
    public void getPatientCurrentMedList(Callback<ListWrapper<PrescriptionDrug>> currentDrugListCallBack, String nic) {
        medipalAPI.patientCurrentMed(nic).enqueue(currentDrugListCallBack);
    }
}
