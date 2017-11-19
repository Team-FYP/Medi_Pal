package lk.ac.mrt.cse.medipal.view.doctor;

import android.content.Context;
import android.net.Uri;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import java.util.ArrayList;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.PrescriptionAllergy;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import lk.ac.mrt.cse.medipal.util.StringUtil;

public class DoctorPrescriptionView extends AppCompatActivity implements View.OnClickListener{
    private Context context;
    private Prescription prescription;
    private Patient patient;
    private Toolbar toolbar;
    private ContentLoadingProgressBar progressBar;
    private TextView patient_name_txt;
    private SimpleDraweeView patient_image_drawee;
    private TextView disease_txt;
    private LinearLayout pres_med_layout_linear;
    private ImageView btn_add_allergy;
    private LinearLayout allergy_linear_layout;
    private RadioButton radio_minor;
    private RadioButton radio_medium;
    private RadioButton radio_major;
    private EditText allergy_desc_txt;
    private ProgressBar progress_bar_confirm;
    private RadioGroup layout_radio_severity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_prescription_view);
        context = this;
        prescription = (Prescription) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_PRESCRIPTION, Prescription.class);
        patient = (Patient) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
        getElements();
        setElementValues();
        addListeners();
        addMedicinetoLinear();
    }

    public void getElements() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        patient_name_txt = findViewById(R.id.doctor_name_txt);
        patient_image_drawee = findViewById(R.id.doctor_image_drawee);
        disease_txt = findViewById(R.id.disease_txt);
        progressBar = findViewById(R.id.progress_bar);
        pres_med_layout_linear = findViewById(R.id.pres_med_layout_linear);
        btn_add_allergy = findViewById(R.id.btn_add_allergy);
        allergy_linear_layout = findViewById(R.id.allergy_linear_layout);
        radio_minor = findViewById(R.id.radio_minor);
        radio_medium = findViewById(R.id.radio_medium);
        radio_major = findViewById(R.id.radio_major);
        allergy_desc_txt = findViewById(R.id.allergy_desc_txt);
        progress_bar_confirm = findViewById(R.id.progress_bar_confirm);
        layout_radio_severity = findViewById(R.id.layout_radio_severity);
    }

    private void setElementValues() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        patient_name_txt.setText(patient.getName());
        Uri image_uri = Uri.parse(patient.getImage());
        patient_image_drawee.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(patient_image_drawee.getController())
                        .setUri(image_uri)
                        .build());
        disease_txt.setText(prescription.getDisease_id());

        progressBar.setVisibility(View.GONE);
        progress_bar_confirm.setVisibility(View.GONE);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (prescription.getPrescription_allergy() != null){
            PrescriptionAllergy prescriptionAllergy = prescription.getPrescription_allergy();
            allergy_linear_layout.setVisibility(View.VISIBLE);
            if (prescriptionAllergy.getSeverity().equals(Common.AllergyTypes.MEDIUM)) radio_medium.setChecked(true);
            else if (prescriptionAllergy.getSeverity().equals(Common.AllergyTypes.MAJOR)) radio_major.setChecked(true);
            else radio_minor.setChecked(true);
        }
    }

    private void addListeners() {
        btn_add_allergy.setOnClickListener(this);
    }

    private void addMedicinetoLinear() {
        pres_med_layout_linear.removeAllViews();
        if (prescription != null) {
            ArrayList<PrescriptionDrug> prescriptionDrugs = prescription.getPrescription_drugs();
            if (prescriptionDrugs != null && !prescriptionDrugs.isEmpty()) {
                for (PrescriptionDrug prescriptionDrug : prescriptionDrugs) {
                    LayoutInflater li = LayoutInflater.from(context);
                    View single_medication_row = li.inflate(R.layout.single_medication_row_small, null);
                    TextView image_txt = single_medication_row.findViewById(R.id.image_txt);
                    TextView drugNmae_txt = single_medication_row.findViewById(R.id.drugNmae_txt);
                    TextView unit_size_txt = single_medication_row.findViewById(R.id.unit_size_txt);
                    TextView dosage_txt = single_medication_row.findViewById(R.id.dosage_txt);
                    TextView use_time_txt = single_medication_row.findViewById(R.id.use_time_txt);
                    TextView duration_txt = single_medication_row.findViewById(R.id.duration_txt);
                    image_txt.setText(prescriptionDrug.getDrug().getDrug_name().substring(0,1).toUpperCase());
                    drugNmae_txt.setText(prescriptionDrug.getDrug().getDrug_name());
                    unit_size_txt.setText(prescriptionDrug.getUnitSize());
                    dosage_txt.setText(prescriptionDrug.getDosage() + " " + prescriptionDrug.getFrequency());
                    use_time_txt.setText(prescriptionDrug.getUseTime());
                    duration_txt.setText(String.format(Common.DURATION_TXT_VALUE, prescriptionDrug.getDuration(), prescriptionDrug.getStartDate()));
                    pres_med_layout_linear.addView(single_medication_row);
                }
            }
        }
    }
    @Override
    public void onClick(View view) {
        Gson gson = new Gson();
        switch (view.getId()) {
            case R.id.btn_add_allergy:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

