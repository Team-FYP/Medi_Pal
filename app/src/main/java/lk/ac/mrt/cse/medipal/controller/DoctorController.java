package lk.ac.mrt.cse.medipal.controller;

import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.network.LoginRequest;
import lk.ac.mrt.cse.medipal.model.network.DoctorLoginResponse;
import retrofit2.Callback;

/**
 * Created by lakshan on 11/2/17.
 */

public class DoctorController extends AbstractController{
    public void attemptSignUp(Callback<DoctorLoginResponse> signUpCallback, Doctor doctor){
        medipalAPI.doctorSignUp(doctor).enqueue(signUpCallback);
    }

    public void attemptSignIn(Callback<DoctorLoginResponse> signUpCallback, LoginRequest loginRequest){
        medipalAPI.doctorSignIn(loginRequest).enqueue(signUpCallback);
    }
}
