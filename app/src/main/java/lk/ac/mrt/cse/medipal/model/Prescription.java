package lk.ac.mrt.cse.medipal.model;

import java.util.ArrayList;

/**
 * Created by chand on 2017-06-13.
 */

public class Prescription {
    private String prescription_id;
    private ArrayList<PrescriptionDrug> prescription_drugs;
    private Doctor doctor;
    private Patient patient;
    private String disease;


}
