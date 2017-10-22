package lk.ac.mrt.cse.medipal.view.patient;

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
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.LinearSort;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Alert;
import lk.ac.mrt.cse.medipal.util.ConvertImageUtil;

public class PatientRegisterActivity extends AppCompatActivity {
    private static final int MEDIA_REQUEST_ACTIVITY_CODE = 111;
    private static final int MEDIA_REQUEST_PERMISSION_CODE = 222;
    final Calendar myCalendar = Calendar.getInstance();
    private CircleImageView image_add_picture;
    GridLayout layout_grid_input;
    private MaterialEditText input_nic;
    private MaterialEditText input_name;
    private MaterialEditText input_gender;
    private MaterialEditText input_birthday;
    private MaterialEditText input_email;
    private MaterialEditText input_mobile;
    private MaterialEditText input_emergency_contact;
    private MaterialEditText input_password;
    private MaterialEditText input_reenter_password;
    private RadioGroup layout_radio_gender;
    private RadioButton radio_male;
    private RadioButton radio_female;
    private Button btn_sign_up;
    private String profileimage;

    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);
        configure_ui();
        animate_ui();
    }

    private void configure_ui(){
        image_add_picture = (CircleImageView) findViewById(R.id.image_add_picture);
        layout_grid_input = (GridLayout) findViewById(R.id.layout_grid_input);
        input_nic = (MaterialEditText) findViewById(R.id.input_nic);
        input_name = (MaterialEditText) findViewById(R.id.input_name);
        input_birthday = (MaterialEditText) findViewById(R.id.input_birthday);
        input_email = (MaterialEditText) findViewById(R.id.input_email);
        input_mobile = (MaterialEditText) findViewById(R.id.input_mobile);
        input_emergency_contact = (MaterialEditText) findViewById(R.id.input_emergency_contact);
        input_password = (MaterialEditText) findViewById(R.id.input_password);
        input_reenter_password = (MaterialEditText) findViewById(R.id.input_reenter_password);
        input_gender = (MaterialEditText) findViewById(R.id.input_gender);
        layout_radio_gender = (RadioGroup) findViewById(R.id.layout_radio_gender);
        radio_male = (RadioButton) findViewById(R.id.radio_male);
        radio_female = (RadioButton) findViewById(R.id.radio_female);


        input_gender.setKeyListener(null);
        input_gender.setShowClearButton(false);
        input_gender.setFocusable(false);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                input_birthday.setText(sdf.format(myCalendar.getTime()));
            }

        };
        input_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PatientRegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        image_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestPermission()){
                   open_media_activity();
                }
            }
        });

//        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
//        btn_sign_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadImage(v);
//            }
//        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MEDIA_REQUEST_ACTIVITY_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap imagemap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath),300,300,false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagemap.compress(Bitmap.CompressFormat.JPEG, 40, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            profileimage = Base64.encodeToString(byte_arr,Base64.DEFAULT);
            image_add_picture.setBackgroundResource(0);
            image_add_picture.setImageBitmap(imagemap);
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
                    Toast toast = Toast.makeText(PatientRegisterActivity.this, Alert.PERMISSION_NOT_GRANTED_TXT, Toast.LENGTH_LONG);
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

    // When Upload button is clicked
    public void uploadImage(View v) {
        // When Image is selected from Gallery
        if (picturePath != null && !picturePath.isEmpty()) {
            // Convert image to String using Base64
            ConvertImageUtil.encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }
}
