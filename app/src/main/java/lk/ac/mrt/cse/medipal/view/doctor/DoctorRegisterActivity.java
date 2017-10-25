package lk.ac.mrt.cse.medipal.view.doctor;

import android.Manifest;
import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.LinearSort;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Alert;
import lk.ac.mrt.cse.medipal.view.patient.PatientRegisterActivity;

public class DoctorRegisterActivity extends AppCompatActivity {
    private static final int MEDIA_REQUEST_ACTIVITY_CODE = 111;
    private static final int MEDIA_REQUEST_PERMISSION_CODE = 222;
    private CircleImageView image_add_picture;
    GridLayout layout_grid_input;
    private MaterialEditText input_reg_id;
    private MaterialEditText input_speciality;
    private MaterialEditText input_name;
    private MaterialEditText input_gender;
    private MaterialEditText input_email;
    private MaterialEditText input_mobile;
    private MaterialEditText input_password;
    private MaterialEditText input_reenter_password;
    private RadioGroup layout_radio_gender;
    private RadioButton radio_male;
    private RadioButton radio_female;
    private Button btn_sign_up;
    private String profileimage;

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
        layout_radio_gender = (RadioGroup) findViewById(R.id.layout_radio_gender);
        radio_male = (RadioButton) findViewById(R.id.radio_male);
        radio_female = (RadioButton) findViewById(R.id.radio_female);


        input_gender.setKeyListener(null);
        input_gender.setShowClearButton(false);
        input_gender.setFocusable(false);

        image_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestPermission()){
                    open_media_activity();
                }
            }
        });
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
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                Cursor cursor = getContentResolver().query(resultUri,
//                        filePathColumn, null, null, null);
//                assert cursor != null;
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String picturePath = cursor.getString(columnIndex);
//                cursor.close();
                InputStream image_stream = null;
                try {
                    image_stream = getContentResolver().openInputStream(resultUri);
                    Bitmap imagemap= BitmapFactory.decodeStream(image_stream );
                    //Bitmap imagemap = BitmapFactory.decodeFile(picturePath);
                    //imagemap = Bitmap.createScaledBitmap(imagemap,imagemap.getWidth(),imagemap.getHeight(),false);
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
