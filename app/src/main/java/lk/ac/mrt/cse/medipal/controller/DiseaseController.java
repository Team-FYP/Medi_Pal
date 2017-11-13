package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.Disease;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/13/17.
 */

public class DiseaseController extends AbstractController {
    public void getAllDiseaseList(Callback<ListWrapper<Disease>> diseaseListCallBack) {
        medipalAPI.allDiseaseList().enqueue(diseaseListCallBack);
    }
}
