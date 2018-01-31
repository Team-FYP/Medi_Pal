package lk.ac.mrt.cse.medipal.view.doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.DiseaseSpinnerAdaptor;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.controller.DiseaseController;
import lk.ac.mrt.cse.medipal.controller.PrescriptionController;
import lk.ac.mrt.cse.medipal.model.Disease;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescribingActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PRESCRIPTION_DRUG_SELECTION_REQUEST_CODE = 125;
    private Context context;
    private Doctor doctor;
    private Patient patient;
    private Disease prescribingDisease;
    private Toolbar toolbar;
    private ContentLoadingProgressBar progressBar;
    private TextView patient_name_txt;
    private SimpleDraweeView patient_image_drawee;
    private SearchableSpinner disease_spinner;
    private DiseaseSpinnerAdaptor disease_spinner_adaptor;
    private ArrayList<String> diseaseNameList = new ArrayList<>();
    private ArrayList<Disease> diseaseList = new ArrayList<>();
    private LinearLayout pres_med_layout_linear;
    private Prescription currentPrescription;
    private boolean isLastPrescriptionAvailable = true;
    private MaterialFancyButton btn_level_down;
    private MaterialFancyButton btn_level_up;
    private MaterialFancyButton btn_continue;
    private LinearLayout btn_layout_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescribing);
        context = this;
        patient = (Patient) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
        doctor = (Doctor) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_DOCTOR, Doctor.class);
        getElements();
        setElementValues();
        retrieveAllDisease();
        refreshDiseaseSpinner();
        addListeners();
    }

    public void getElements() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        patient_name_txt = findViewById(R.id.patient_name_txt);
        patient_image_drawee = findViewById(R.id.patient_image_drawee);
        disease_spinner = findViewById(R.id.disease_spinner);
        progressBar = findViewById(R.id.progress_bar);
        disease_spinner_adaptor = new DiseaseSpinnerAdaptor(this, diseaseList);
        pres_med_layout_linear = findViewById(R.id.pres_med_layout_linear);
        btn_level_down = findViewById(R.id.btn_level_down);
        btn_level_up = findViewById(R.id.btn_level_up);
        btn_continue = findViewById(R.id.btn_continue);
        btn_layout_linear = findViewById(R.id.btn_layout_linear);
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
        progressBar.setVisibility(View.GONE);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void retrieveAllDisease() {
        progressBar.setVisibility(View.VISIBLE);

        Callback<ListWrapper<Disease>> allDiseaseListCallback = new Callback<ListWrapper<Disease>>() {
            @Override
            public void onResponse(Call<ListWrapper<Disease>> call, Response<ListWrapper<Disease>> response) {
                ListWrapper<Disease> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getItems() != null) {
                        diseaseNameList = new ArrayList<>();
                        diseaseList = new ArrayList<>();
                        diseaseList.addAll(responseObject.getItems());
                        for (Disease disease : diseaseList) {
                            diseaseNameList.add(disease.getDisease_name());
                        }
                        refreshDiseaseSpinner();
                    }
                } else {
                    Toast.makeText(context, Common.ERROR_OCCURED_TXT + response.message(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ListWrapper<Disease>> call, Throwable t) {
                Toast.makeText(context, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };
        DiseaseController diseaseController = new DiseaseController();
        diseaseController.getAllDiseaseList(allDiseaseListCallback);
    }

    private void refreshDiseaseSpinner() {
        disease_spinner_adaptor = new DiseaseSpinnerAdaptor(this, diseaseList);
        disease_spinner_adaptor.notifyDataSetChanged();
        disease_spinner.setAdapter(disease_spinner_adaptor);
    }

    private void addListeners() {
        disease_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                prescribingDisease = diseaseList.get(position);
                retrieveCurrentPrescription(prescribingDisease.getDisease_id());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        btn_level_up.setOnClickListener(this);
        btn_level_down.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
    }

    private void retrieveCurrentPrescription(String disease_id) {
        progressBar.setVisibility(View.VISIBLE);

        Callback<Prescription> currentPrescriptionResponse = new Callback<Prescription>() {
            @Override
            public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                if (response.isSuccessful()) {
                    currentPrescription = response.body();
                    addMedicinetoLinear();
                    if (isLastPrescriptionAvailable) {

                    }
                } else {
                    Toast.makeText(context, Common.ERROR_OCCURED_TXT + response.message(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Prescription> call, Throwable t) {
                Toast.makeText(context, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };
        String patient_nic = patient.getNic();
        PrescriptionController prescriptionController = new PrescriptionController();
        prescriptionController.getLastPrescriptionOfDisease(currentPrescriptionResponse, patient_nic, disease_id);
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

    private void addMedicinetoLinear() {
        pres_med_layout_linear.removeAllViews();
        if (currentPrescription != null) {
            ArrayList<PrescriptionDrug> prescriptionDrugs = currentPrescription.getPrescription_drugs();
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
                    image_txt.setText(prescriptionDrug.getDrug().getDrug_name().substring(0, 1).toUpperCase());
                    drugNmae_txt.setText(prescriptionDrug.getDrug().getDrug_name());
                    unit_size_txt.setText(prescriptionDrug.getUnitSize());
                    if (prescriptionDrug.getDosage() != null) {
                        dosage_txt.setText(prescriptionDrug.getDosage() + " " + prescriptionDrug.getFrequency());
                        use_time_txt.setText(prescriptionDrug.getUseTime());
                        duration_txt.setText(String.format(Common.DURATION_TXT_VALUE, prescriptionDrug.getDuration(), prescriptionDrug.getStartDate()));
                    } else {
                        dosage_txt.setText("");
                        use_time_txt.setText("");
                        duration_txt.setText("");
                    }
                    pres_med_layout_linear.addView(single_medication_row);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (prescribingDisease != null) {

            switch (view.getId()) {
                case R.id.btn_continue:
                    continuePrescription();
                    break;
                case R.id.btn_level_down:
                    leveDownPrescription();
                    break;
                case R.id.btn_level_up:
                    leveUpPrescription();
                    break;
            }

        } else {
            Toast.makeText(context, "Select a disease first", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PRESCRIPTION_DRUG_SELECTION_REQUEST_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    finish();
                }
                break;
            }
        }
    }

    private void continuePrescription() {
        Intent intent = new Intent(PrescribingActivity.this, PrescriptionDrugSelectionActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(patient);
        intent.putExtra(ObjectType.OBJECT_TYPE_PATIENT, json);
        json = gson.toJson(doctor);
        intent.putExtra(ObjectType.OBJECT_TYPE_DOCTOR, json);
        json = gson.toJson(prescribingDisease);
        intent.putExtra(ObjectType.OBJECT_TYPE_DISEASE, json);
        json = gson.toJson(currentPrescription);
        intent.putExtra(ObjectType.OBJECT_TYPE_PRESCRIPTION, json);
        startActivityForResult(intent, PRESCRIPTION_DRUG_SELECTION_REQUEST_CODE);
        progressBar.setVisibility(View.GONE);
        btn_layout_linear.setVisibility(View.VISIBLE);
    }

    private void leveDownPrescription() {
        progressBar.setVisibility(View.VISIBLE);
        btn_layout_linear.setVisibility(View.GONE);

        Callback<ListWrapper<Drug>> levelDownResponse = new Callback<ListWrapper<Drug>>() {
            @Override
            public void onResponse(Call<ListWrapper<Drug>> call, Response<ListWrapper<Drug>> response) {
                ListWrapper<Drug> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getItems() != null) {
                        ArrayList<PrescriptionDrug> prescriptionDrugs = new ArrayList<>();
                        for (Drug drug : responseObject.getItems()) {
                            prescriptionDrugs.add(new PrescriptionDrug(drug));
                        }
                        currentPrescription = new Prescription(doctor, patient, prescribingDisease.getDisease_id(), doctor.getRegistration_id(), prescriptionDrugs);
                        continuePrescription();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PrescribingActivity.this, "Error getting prescription. Try Again.", Toast.LENGTH_LONG).show();
                        btn_layout_linear.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PrescribingActivity.this, "Error getting prescription. Try Again.", Toast.LENGTH_LONG).show();
                    btn_layout_linear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ListWrapper<Drug>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PrescribingActivity.this, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                btn_layout_linear.setVisibility(View.VISIBLE);
            }
        };
        PrescriptionController prescriptionController = new PrescriptionController();
        prescriptionController.levelDownPrescription(levelDownResponse, patient.getNic(), prescribingDisease.getDisease_name());
    }

    private void leveUpPrescription() {
        progressBar.setVisibility(View.VISIBLE);
        btn_layout_linear.setVisibility(View.GONE);

        Callback<ListWrapper<Drug>> levelUpResponse = new Callback<ListWrapper<Drug>>() {
            @Override
            public void onResponse(Call<ListWrapper<Drug>> call, Response<ListWrapper<Drug>> response) {
                ListWrapper<Drug> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getItems() != null) {
                        ArrayList<PrescriptionDrug> prescriptionDrugs = new ArrayList<>();
                        for (Drug drug : responseObject.getItems()) {
                            prescriptionDrugs.add(new PrescriptionDrug(drug));
                        }
                        currentPrescription = new Prescription(doctor, patient, prescribingDisease.getDisease_id(), doctor.getRegistration_id(), prescriptionDrugs);
                        continuePrescription();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PrescribingActivity.this, "Error getting prescription. Try Again.", Toast.LENGTH_LONG).show();
                        btn_layout_linear.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PrescribingActivity.this, "Error getting prescription. Try Again.", Toast.LENGTH_LONG).show();
                    btn_layout_linear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ListWrapper<Drug>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PrescribingActivity.this, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                btn_layout_linear.setVisibility(View.VISIBLE);
            }
        };
        PrescriptionController prescriptionController = new PrescriptionController();
        prescriptionController.levelUpPrescription(levelUpResponse, patient.getNic(), prescribingDisease.getDisease_name());
    }
}
