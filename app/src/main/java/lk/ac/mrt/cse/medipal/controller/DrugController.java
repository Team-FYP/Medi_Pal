package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/15/17.
 */

public class DrugController extends AbstractController {
    public void getAllDiseaseMedicineList(Callback<ListWrapper<Drug>> drugListCallBack, String disease_id) {
        medipalAPI.allDiseaseMedicineList(disease_id).enqueue(drugListCallBack);
    }
}
