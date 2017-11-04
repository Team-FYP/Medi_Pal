package lk.ac.mrt.cse.medipal.rest;

import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lakshan on 11/2/17.
 */

public interface MedipalAPI {
    @POST("app/doctor/signup")
    Call<LoginResponse> doctorSignUp(@Body Doctor doctor);

    @POST("app/patient/signup")
    Call<LoginResponse> patientSignUp(@Body Patient patient);
}
