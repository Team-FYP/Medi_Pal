package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.network.DataWriteResponse;
import lk.ac.mrt.cse.medipal.model.network.DrugChangeRequest;
import lk.ac.mrt.cse.medipal.model.network.DrugSuggestion;
import lk.ac.mrt.cse.medipal.model.network.ShareRequest;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/21/17.
 */

public class PredictionController extends AbstractController{

    public void getDrugSuggestions(Callback<DrugSuggestion> suggestionCallBack, DrugChangeRequest drugSuggestion) {
        medipalAPI.drugSuggestions(drugSuggestion).enqueue(suggestionCallBack);
    }
}
