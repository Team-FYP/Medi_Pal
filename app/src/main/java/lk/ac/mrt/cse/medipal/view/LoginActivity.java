package lk.ac.mrt.cse.medipal.view;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.CorneredSort;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.constant.SharedPreferencesKeys;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.controller.PatientController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.LoginRequest;
import lk.ac.mrt.cse.medipal.model.network.DoctorLoginResponse;
import lk.ac.mrt.cse.medipal.model.network.PatientLoginResponse;
import lk.ac.mrt.cse.medipal.util.Validator;
import lk.ac.mrt.cse.medipal.view.doctor.DoctorMainActivity;
import lk.ac.mrt.cse.medipal.view.patient.PatientMainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private RelativeLayout layout_login_container;
    private MaterialEditText input_email;
    private MaterialEditText input_password;
    private TextView text_create_account;
    private Button btn_login;
    private ProgressDialog progress;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout_login_container = (RelativeLayout) findViewById(R.id.layout_login_container);
        text_create_account = (TextView) findViewById(R.id.text_create_account);
        btn_login = (Button) findViewById(R.id.btn_login);
        input_email = (MaterialEditText) findViewById(R.id.input_email);
        input_password = (MaterialEditText) findViewById(R.id.input_password);
        context = this;

        animate_ui();
        addListeners();
        addValidators();
    }

    private void animate_ui(){
        Animator[] animators = new Animator[]{
                DefaultAnimations.shrinkAnimator(layout_login_container, /*duration=*/800),
                DefaultAnimations.fadeInAnimator(layout_login_container, /*duration=*/800)
        };
        new Spruce
                .SpruceBuilder(layout_login_container)
                .sortWith(new CorneredSort(1000, false, CorneredSort.Corner.TOP_LEFT))
                .animateWith(animators)
                .start();
    }

    private void addListeners(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    signIn();
                }
            }
        });

        text_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserTypeSelectionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addValidators(){
        input_email.addValidator(new METValidator("Invalid Username") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                int length = text.length();
                if (isEmpty || (!Validator.isValidNIC(text) && !Validator.isValidRegID(text))) {
                    return false;
                }
                return true;
            }
        });

        input_password.addValidator(new METValidator("Enter your password") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
    }

    private boolean validate() {
        if (input_email.validate() &&
                input_password.validate()) {
            return true;
        }
        return false;
    }

    private void signIn() {
        progress=new ProgressDialog(this);
        progress.setMessage("Signing In");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        String username  = input_email.getText().toString();
        String password = input_password.getText().toString();
        LoginRequest loginRequest = new LoginRequest(username,password);
        if (Validator.isValidNIC(username)) {
            Callback<PatientLoginResponse> patientSignInResponse = new Callback<PatientLoginResponse>() {
                @Override
                public void onResponse(Call<PatientLoginResponse> call, Response<PatientLoginResponse> response) {
                    if (progress!=null && progress.isShowing()) {
                        progress.hide();
                    }
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            PatientLoginResponse responseObject = response.body();
                            Patient patient = responseObject.getUserData();
                            SharedPreferences preferencs = context.getSharedPreferences(
                                    SharedPreferencesKeys.MEDIPAL_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(patient);
                            editor.putBoolean(SharedPreferencesKeys.IS_LOGGED_IN_KEY, true);
                            editor.putString(SharedPreferencesKeys.USER_TYPE_KEY, SharedPreferencesKeys.USER_TYPE_PATIENT);
                            editor.putString(SharedPreferencesKeys.PATIENT_OBJECT_KEY, json);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, PatientMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            input_password.setError("Wrong password.");
                            Toast.makeText(LoginActivity.this, "Invalid Username or password. Try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PatientLoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Network Failure. Check your connection", Toast.LENGTH_LONG).show();
                }

            };
            PatientController patientController = new PatientController();
            patientController.attemptSignIn(patientSignInResponse, loginRequest);
        } else if (Validator.isValidRegID(username)) {
            Callback<DoctorLoginResponse> doctorSignInResponse = new Callback<DoctorLoginResponse>() {
                @Override
                public void onResponse(Call<DoctorLoginResponse> call, Response<DoctorLoginResponse> response) {
                    if (progress!=null && progress.isShowing()) {
                        progress.hide();
                    }
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {

                            DoctorLoginResponse responseObject = response.body();
                            Doctor doctor = responseObject.getUserData();
                            SharedPreferences preferencs = context.getSharedPreferences(
                                    SharedPreferencesKeys.MEDIPAL_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(doctor);
                            editor.putBoolean(SharedPreferencesKeys.IS_LOGGED_IN_KEY, true);
                            editor.putString(SharedPreferencesKeys.USER_TYPE_KEY, SharedPreferencesKeys.USER_TYPE_DOCTOR);
                            editor.putString(SharedPreferencesKeys.DOCTOR_OBJECT_KEY, json);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, DoctorMainActivity.class);
                            intent.putExtra(ObjectType.OBJECT_TYPE_DOCTOR, json);
                            startActivity(intent);
                            finish();
                        } else {
                            input_password.setError("Wrong password.");
                            Toast.makeText(LoginActivity.this, "Invalid Username or password. Try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<DoctorLoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Network Failure. Check your connection", Toast.LENGTH_LONG).show();
                }

            };

            DoctorController doctorController = new DoctorController();
            doctorController.attemptSignIn(doctorSignInResponse, loginRequest);
        }
    }
}


