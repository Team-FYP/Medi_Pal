package lk.ac.mrt.cse.medipal.controller;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.LoginResponse;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/2/17.
 */

public class PatientController extends AbstractController{
    public void attemptSignUp(Callback<LoginResponse> signUpCallback, Patient patient){
        medipalAPI.patientSignUp(patient).enqueue(signUpCallback);
    }
}
