package lk.ac.mrt.cse.medipal.view.doctor;

import android.content.Context;
import android.net.Uri;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

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
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescribingActivity extends AppCompatActivity {
    private Doctor doctor;
    private Patient patient;
    private Toolbar toolbar;
    private TextView patient_name_txt;
    private SimpleDraweeView patient_image_drawee;
    private SearchableSpinner disease_spinner;
    private DiseaseSpinnerAdaptor disease_spinner_adaptor;
    private ArrayList<String> diseaseNameList = new ArrayList<>();
    private ArrayList<Disease> diseaseList = new ArrayList<>();
    private ContentLoadingProgressBar progressBar;
    private Context context;
    private LinearLayout pres_med_layout_linear;
    private Prescription currentPrescription;
    private boolean isLastPrescriptionAvailable = true;
    private int _xDelta;
    private int _yDelta;

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
                retrieveCurrentPrescription(diseaseList.get(position).getDisease_id());
            }

            @Override
            public void onNothingSelected() {

            }
        });
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
                    dosage_txt.setText(String.format(Common.DOSAGE_TXT_VALUE, prescriptionDrug.getDosage(), prescriptionDrug.getFrequency()));
                    use_time_txt.setText(prescriptionDrug.getUseTime());
                    duration_txt.setText(String.format(Common.DURATION_TXT_VALUE, prescriptionDrug.getDuration(), prescriptionDrug.getStartDate()));
                    pres_med_layout_linear.addView(single_medication_row);
                }
            }
        }

    }
}
