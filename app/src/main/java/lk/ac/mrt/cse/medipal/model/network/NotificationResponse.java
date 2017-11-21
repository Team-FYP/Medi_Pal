package lk.ac.mrt.cse.medipal.model.network;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.model.AllergyNotification;
import lk.ac.mrt.cse.medipal.model.PrescriptionNotification;
import lk.ac.mrt.cse.medipal.model.ShareNotification;

/**
 * Created by lakshan on 11/19/17.
 */

public class NotificationResponse {
    private ArrayList<ShareNotification> share_notifications;
    private ArrayList<PrescriptionNotification> prescription_notifications;
    private ArrayList<AllergyNotification> allergy_notifications;

    public NotificationResponse() {
    }

    public NotificationResponse(ArrayList<ShareNotification> share_notifications, ArrayList<PrescriptionNotification> prescription_notifications, ArrayList<AllergyNotification> allergy_notifications) {
        this.share_notifications = share_notifications;
        this.prescription_notifications = prescription_notifications;
        this.allergy_notifications = allergy_notifications;
    }

    public ArrayList<ShareNotification> getShare_notifications() {
        return share_notifications;
    }

    public void setShare_notifications(ArrayList<ShareNotification> share_notifications) {
        this.share_notifications = share_notifications;
    }

    public ArrayList<PrescriptionNotification> getPrescription_notifications() {
        return prescription_notifications;
    }

    public void setPrescription_notifications(ArrayList<PrescriptionNotification> prescription_notifications) {
        this.prescription_notifications = prescription_notifications;
    }

    public ArrayList<AllergyNotification> getAllergy_notifications() {
        return allergy_notifications;
    }

    public void setAllergy_notifications(ArrayList<AllergyNotification> allergy_notifications) {
        this.allergy_notifications = allergy_notifications;
    }
}
