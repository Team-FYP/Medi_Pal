package lk.ac.mrt.cse.medipal.rest;

import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.LoginRequest;
import lk.ac.mrt.cse.medipal.model.network.DoctorLoginResponse;
import lk.ac.mrt.cse.medipal.model.network.PatientLoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lakshan on 11/2/17.
 */

public interface MedipalAPI {
    @POST("app/doctor/signup")
    Call<DoctorLoginResponse> doctorSignUp(@Body Doctor doctor);

    @POST("app/doctor/login")
    Call<DoctorLoginResponse> doctorSignIn(@Body LoginRequest loginRequest);

    @POST("app/patient/signup")
    Call<PatientLoginResponse> patientSignUp(@Body Patient patient);

    @POST("app/patient/login")
    Call<PatientLoginResponse> patientSignIn(@Body LoginRequest loginRequest);
}
