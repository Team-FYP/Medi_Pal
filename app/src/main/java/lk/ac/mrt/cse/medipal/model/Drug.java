package lk.ac.mrt.cse.medipal.model;

/**
 * Created by chand on 2017-06-13.
 */

public class Drug {
    private String drug_name;
    private String drug_id;

    public Drug() {
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(String drug_id) {
        this.drug_id = drug_id;
    }
}
