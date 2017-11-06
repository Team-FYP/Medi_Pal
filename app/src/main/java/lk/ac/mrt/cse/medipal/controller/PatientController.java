package lk.ac.mrt.cse.medipal.controller;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.LoginRequest;
import lk.ac.mrt.cse.medipal.model.network.DoctorLoginResponse;
import lk.ac.mrt.cse.medipal.model.network.PatientLoginResponse;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/2/17.
 */

public class PatientController extends AbstractController{
    public void attemptSignUp(Callback<PatientLoginResponse> signUpCallback, Patient patient){
        medipalAPI.patientSignUp(patient).enqueue(signUpCallback);
    }

    public void attemptSignIn(Callback<PatientLoginResponse> signUpCallback, LoginRequest loginRequest){
        medipalAPI.patientSignIn(loginRequest).enqueue(signUpCallback);
    }
}
