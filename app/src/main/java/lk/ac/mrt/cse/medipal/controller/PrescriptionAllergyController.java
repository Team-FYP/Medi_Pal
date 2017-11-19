package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.network.DataWriteResponse;
import lk.ac.mrt.cse.medipal.model.network.PrescriptionAllergy;
import lk.ac.mrt.cse.medipal.model.network.ShareRequest;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/19/17.
 */

public class PrescriptionAllergyController extends AbstractController{
    public void saveAllergy(Callback<DataWriteResponse> allergySaveCallBack, PrescriptionAllergy prescriptionAllergy) {
        medipalAPI.saveAllergy(prescriptionAllergy).enqueue(allergySaveCallBack);
    }
}
