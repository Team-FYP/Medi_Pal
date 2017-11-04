package lk.ac.mrt.cse.medipal.view.doctor;

import android.Manifest;
import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.theartofdev.edmodo.cropper.CropImage;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.LinearSort;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import de.hdodenhof.circleimageview.CircleImageView;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Alert;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.network.LoginResponse;
import lk.ac.mrt.cse.medipal.util.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRegisterActivity extends AppCompatActivity {
    private static final int MEDIA_REQUEST_ACTIVITY_CODE = 111;
    private static final int MEDIA_REQUEST_PERMISSION_CODE = 222;
    private CircleImageView image_add_picture;
    private GridLayout layout_grid_input;
    private MaterialEditText input_reg_id;
    private MaterialEditText input_speciality;
    private MaterialEditText input_name;
    private MaterialEditText input_gender;
    private MaterialEditText input_email;
    private MaterialEditText input_mobile;
    private MaterialEditText input_password;
    private MaterialEditText input_reenter_password;
    private RadioButton radio_male;
    private Button btn_sign_up;
    private String profileimage;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);
        configure_ui();
        animate_ui();
    }

    private void configure_ui(){
        image_add_picture = (CircleImageView) findViewById(R.id.image_add_picture);
        layout_grid_input = (GridLayout) findViewById(R.id.layout_grid_input);
        input_reg_id = (MaterialEditText) findViewById(R.id.input_reg_id);
        input_name = (MaterialEditText) findViewById(R.id.input_name);
        input_speciality = (MaterialEditText) findViewById(R.id.input_speciality);
        input_email = (MaterialEditText) findViewById(R.id.input_email);
        input_mobile = (MaterialEditText) findViewById(R.id.input_mobile);
        input_password = (MaterialEditText) findViewById(R.id.input_password);
        input_reenter_password = (MaterialEditText) findViewById(R.id.input_reenter_password);
        input_gender = (MaterialEditText) findViewById(R.id.input_gender);
        radio_male = (RadioButton) findViewById(R.id.radio_male);
        input_gender.setKeyListener(null);
        input_gender.setShowClearButton(false);
        input_gender.setFocusable(false);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);

        addListeners();
        addValidators();
    }
    private void addListeners() {

        image_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestPermission()){
                    open_media_activity();
                }
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    signUp();
                }
            }
        });
    }

    private void addValidators() {

        input_reg_id.addValidator(new METValidator("Invalid Registration ID") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                int length = text.length();
                if (isEmpty || !Validator.isValidRegID(text)) {
                    return false;
                }
                return true;
            }
        });

        input_name.addValidator(new METValidator("Enter your name") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });

        input_speciality.addValidator(new METValidator("Enter a valid speciality") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        input_email.addValidator(new METValidator("Enter a valid email") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return Validator.isValidEmail(text);
            }
        });
        input_mobile.addValidator(new METValidator("Enter a valid moibile number") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return Validator.isValidMobile(text);
            }
        });
        input_password.addValidator(new METValidator("Enter a password") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        input_reenter_password.addValidator(new METValidator("Password does not match") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                if (input_password.getText().toString().equals(text.toString())) {
                    return true;
                }
                return false;
            }
        });
    }

    private boolean validate() {

        if (input_reg_id.validate() &&
                input_name.validate() &&
                input_speciality.validate() &&
                input_email.validate() &&
                input_mobile.validate() &&
                input_password.validate() &&
                input_reenter_password.validate()) {
            return true;
        }
        return false;
    }

    private void signUp() {
        progress=new ProgressDialog(this);
        progress.setMessage("Signing Up");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        Callback<LoginResponse> signUpResponse = new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (progress!=null && progress.isShowing()) {
                    progress.hide();
                }
                LoginResponse<Doctor> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.isSuccess()) {
                        Doctor doctor = responseObject.getUserData();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(DoctorRegisterActivity.this, "Network Failure. Check your connection", Toast.LENGTH_LONG).show();
            }

        };
        String registration_id  = input_reg_id.getText().toString();
        String speciality = input_speciality.getText().toString();
        String name = input_name.getText().toString();
        String gender = Common.FEMALE_TXT;
        if (radio_male.isChecked()) {
            gender = Common.MALE_TXT;
        }
        String email = input_email.getText().toString();
        String mobile = input_mobile.getText().toString();
        String password = input_password.getText().toString();
        String image = profileimage;

        Doctor doctor = new Doctor(registration_id, speciality, name, gender, email, mobile, password, image);
        DoctorController doctorController = new DoctorController();
        doctorController.attemptSignUp(signUpResponse, doctor);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MEDIA_REQUEST_ACTIVITY_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            CropImage.activity(selectedImage).setAspectRatio(1,1)
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                InputStream image_stream = null;
                try {
                    image_stream = getContentResolver().openInputStream(resultUri);
                    Bitmap imagemap= BitmapFactory.decodeStream(image_stream );
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imagemap.compress(Bitmap.CompressFormat.JPEG, 40, stream); //compress to which format you want.
                    byte[] byte_arr = stream.toByteArray();
                    profileimage = Base64.encodeToString(byte_arr,Base64.DEFAULT);
                    image_add_picture.setBackgroundResource(0);
                    image_add_picture.setImageBitmap(imagemap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MEDIA_REQUEST_PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }

    private void open_media_activity(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, MEDIA_REQUEST_ACTIVITY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MEDIA_REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    open_media_activity();
                } else {
                    Toast toast = Toast.makeText(DoctorRegisterActivity.this, Alert.PERMISSION_NOT_GRANTED_TXT, Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
        }

    }

    private void animate_ui(){
        Animator[] animators = new Animator[]{
                DefaultAnimations.shrinkAnimator(layout_grid_input, /*duration=*/800),
                DefaultAnimations.fadeInAnimator(layout_grid_input, /*duration=*/800)
        };
        Animator spruceAnimator = new Spruce
                .SpruceBuilder(layout_grid_input)
                .sortWith(new LinearSort(80, false, LinearSort.Direction.TOP_TO_BOTTOM))
                .animateWith(animators)
                .start();
    }
}
