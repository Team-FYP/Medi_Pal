package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugCategory;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/15/17.
 */

public class DrugCategoryController extends AbstractController{
    public void getAllDrugCategoryList(Callback<ListWrapper<DrugCategory>> drugCategoryListCallBack) {
        medipalAPI.allDrugCategoryList().enqueue(drugCategoryListCallBack);
    }

    public void getAllCategoryDrugList(Callback<ListWrapper<Drug>> categoryDrugListCallBack, String category_id) {
        medipalAPI.allCategoryMedicineList(category_id).enqueue(categoryDrugListCallBack);
    }
}
