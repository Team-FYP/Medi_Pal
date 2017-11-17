package lk.ac.mrt.cse.medipal.rest;

import lk.ac.mrt.cse.medipal.model.Disease;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugCategory;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.DataWriteResponse;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import lk.ac.mrt.cse.medipal.model.network.LoginRequest;
import lk.ac.mrt.cse.medipal.model.network.DoctorLoginResponse;
import lk.ac.mrt.cse.medipal.model.network.PatientLoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @GET("app/doctor/{id}/patients")
    Call<ListWrapper<Patient>> doctorPatientList(@Path("id") String registration_id);

    @GET("app/prescription/patient/{id}/currentprescriptions")
    Call<ListWrapper<PrescriptionDrug>> patientCurrentMed(@Path("id") String nic);

    @GET("/app/prescription/patient/{id}")
    Call<ListWrapper<Prescription>> patientPrescriptionList(@Path("id") String nic);

    @GET("/app/prescription/patient/{id}/lastprescription/{disease}")
    Call<Prescription> lastPrescriptionofDisease(@Path("id") String nic , @Path("disease") String disease);

    @GET("/app/disease/diseases")
    Call<ListWrapper<Disease>> allDiseaseList();

    @GET("/app/category/categories")
    Call<ListWrapper<DrugCategory>> allDrugCategoryList();

    @GET("/app/disease/{id}/drugs")
    Call<ListWrapper<Drug>> allDiseaseMedicineList(@Path("id") String disease_id);

    @GET("/app/category/{id}/drugs")
    Call<ListWrapper<Drug>> allCategoryMedicineList(@Path("id") String category_id);

    @POST("app/prescription/addprescription")
    Call<DataWriteResponse> savePrescription(@Body Prescription prescription);
}
