package lk.ac.mrt.cse.medipal.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.network.LoginRequest;
import lk.ac.mrt.cse.medipal.rest.MedipalAPI;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lakshan on 11/2/17.
 */

public class DoctorController extends AbstractController{
    public void attemptSignUp(Callback signUpCallback, LoginRequest loginRequest){
        medipalAPI.doctorSignUp(loginRequest).enqueue(signUpCallback);
    }
}
