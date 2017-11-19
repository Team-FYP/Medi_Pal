package lk.ac.mrt.cse.medipal.model;

import lk.ac.mrt.cse.medipal.model.network.PrescriptionAllergy;

public class AllergyNotification extends Notification {
    private Prescription prescription;
    private PrescriptionAllergy prescriptionAllergy;

    public AllergyNotification(Patient patient, Doctor doctor, String message, String status, String time, Prescription prescription, PrescriptionAllergy prescriptionAllergy) {
        super(patient, doctor, message, status, time);
        this.prescription = prescription;
        this.prescriptionAllergy = prescriptionAllergy;
    }

    public AllergyNotification(){}
}
