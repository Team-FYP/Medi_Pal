package lk.ac.mrt.cse.medipal.model.network;

import lk.ac.mrt.cse.medipal.model.ConflictScoreValue;
import lk.ac.mrt.cse.medipal.model.Drug;

import java.util.ArrayList;

public class DrugSuggestion {
    private ArrayList<ConflictScoreValue> conflictScoreValueArrayList;
    private ArrayList<Drug> suggested_drugs;



    public DrugSuggestion() {
    }

    public ArrayList<ConflictScoreValue> getConflictScoreValueArrayList() {
        return conflictScoreValueArrayList;
    }

    public void setConflicting_drugs(ArrayList<ConflictScoreValue> conflictScoreValueArrayList) {
        this.conflictScoreValueArrayList = conflictScoreValueArrayList;
    }

    public ArrayList<Drug> getSuggested_drugs() {
        return suggested_drugs;
    }

    public void setSuggested_drugs(ArrayList<Drug> suggested_drugs) {
        this.suggested_drugs = suggested_drugs;
    }
}
