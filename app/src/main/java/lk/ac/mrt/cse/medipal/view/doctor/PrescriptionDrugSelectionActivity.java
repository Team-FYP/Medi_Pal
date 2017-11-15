package lk.ac.mrt.cse.medipal.view.doctor;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.DiseaseSpinnerAdaptor;
import lk.ac.mrt.cse.medipal.adaptor.DrugAutoCompleteAdaptor;
import lk.ac.mrt.cse.medipal.adaptor.PrescriptionDrugSelectionRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.controller.DiseaseController;
import lk.ac.mrt.cse.medipal.controller.DrugCategoryController;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.model.Disease;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugCategory;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionDrugSelectionActivity extends AppCompatActivity {
    private Context context;
    private Doctor doctor;
    private Disease disease;
    private Patient patient;
    private Toolbar toolbar;
    private TextView disease_name_txt;
    private AutoCompleteTextView drug_auto_txt;
    private SimpleDraweeView patient_image_drawee;
    private ContentLoadingProgressBar progressBar;
    private RecyclerView pres_med_recycler;
    private ArrayList<Drug> drugList = new ArrayList<>();
    private ArrayList<DrugCategory> drugCategoryList = new ArrayList<>();
    private ArrayList<PrescriptionDrug> prescriptionDrugList = new ArrayList<>();
    private DrugAutoCompleteAdaptor drugAutoCompleteAdaptor;
    private LinearLayoutManager prescriptionDruglayoutManager;
    private PrescriptionDrugSelectionRecyclerAdaptor pres_drug_select_recyc_adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_drug_selection);
        context = this;
        patient = (Patient) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
        doctor = (Doctor) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_DOCTOR, Doctor.class);
        disease = (Disease) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_DISEASE, Disease.class);
        getElements();
        setElementValues();
        retrieveDiseaseMedicine();
        retrieveDrugCategories();
        refreshDrugAutoCompleteSpinner();
        addListeners();
    }

    private void addListeners() {
        drug_auto_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DrugAutoCompleteAdaptor drugAutoCompleteAdaptor = (DrugAutoCompleteAdaptor) adapterView.getAdapter();
                Drug drug = drugAutoCompleteAdaptor.getDrug_suggestions().get(i);
                PrescriptionDrug prescriptionDrug = new PrescriptionDrug();
                prescriptionDrug.setDrug(drug);
                prescriptionDrugList.add(prescriptionDrug);
                refreshRecyclerView();
                drug_auto_txt.setText("");
            }
        });
    }

    public void getElements() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        disease_name_txt = findViewById(R.id.disease_name_txt);
        patient_image_drawee = findViewById(R.id.patient_image_drawee);
        progressBar = findViewById(R.id.progress_bar);
        drug_auto_txt = findViewById(R.id.drug_auto_txt);
        pres_med_recycler = findViewById(R.id.pres_med_recycler);
        prescriptionDruglayoutManager = new LinearLayoutManager(this);
        attachRecycleItemTouchHelper();
    }

    private void attachRecycleItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.LEFT) {    //if swipe left

                    AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionDrugSelectionActivity.this); //alert for confirm to delete
                    builder.setMessage("Are you sure to remove this medicine?");    //set message

                    builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() { //when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pres_drug_select_recyc_adaptor.notifyItemRemoved(position);    //item removed from recylcerview
                            prescriptionDrugList.remove(position);  //then remove item
                            return;
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pres_drug_select_recyc_adaptor.notifyItemRemoved(position);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                            pres_drug_select_recyc_adaptor.notifyItemRangeChanged(position, pres_drug_select_recyc_adaptor.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            return;
                        }
                    }).show();  //show alert dialog
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(pres_med_recycler); //set swipe to recylcerview
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
        disease_name_txt.setText(disease.getDisease_name());
        Uri image_uri = Uri.parse(patient.getImage());
        patient_image_drawee.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(patient_image_drawee.getController())
                        .setUri(image_uri)
                        .build());
        progressBar.setVisibility(View.GONE);
        pres_med_recycler.setLayoutManager(prescriptionDruglayoutManager);
        drug_auto_txt.setThreshold(0);
        pres_drug_select_recyc_adaptor = new PrescriptionDrugSelectionRecyclerAdaptor(this, prescriptionDrugList, drugList);
        pres_med_recycler.setAdapter(pres_drug_select_recyc_adaptor);
        pres_med_recycler.setNestedScrollingEnabled(true);
    }

    private void retrieveDiseaseMedicine() {
        progressBar.setVisibility(View.VISIBLE);

        Callback<ListWrapper<Drug>> diseaseMedicineListCallback = new Callback<ListWrapper<Drug>>() {
            @Override
            public void onResponse(Call<ListWrapper<Drug>> call, Response<ListWrapper<Drug>> response) {
                ListWrapper<Drug> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getItems() != null) {
                        drugList = new ArrayList<>();
                        drugList.addAll(responseObject.getItems());
                        refreshDrugAutoCompleteSpinner();
                    }
                } else {
                    Toast.makeText(context, Common.ERROR_OCCURED_TXT + response.message(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ListWrapper<Drug>> call, Throwable t) {
                Toast.makeText(context, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };
        DrugController drugController = new DrugController();
        drugController.getAllDiseaseMedicineList(diseaseMedicineListCallback, disease.getDisease_id());
    }

    private void retrieveDrugCategories() {
        progressBar.setVisibility(View.VISIBLE);

        Callback<ListWrapper<DrugCategory>> drugCategoryListCallback = new Callback<ListWrapper<DrugCategory>>() {
            @Override
            public void onResponse(Call<ListWrapper<DrugCategory>> call, Response<ListWrapper<DrugCategory>> response) {
                ListWrapper<DrugCategory> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getItems() != null) {
                        drugCategoryList = new ArrayList<>();
                        drugCategoryList.addAll(responseObject.getItems());
                        refreshDrugAutoCompleteSpinner();
                    }
                } else {
                    Toast.makeText(context, Common.ERROR_OCCURED_TXT + response.message(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ListWrapper<DrugCategory>> call, Throwable t) {
                Toast.makeText(context, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };
        DrugCategoryController drugCategoryController = new DrugCategoryController();
        drugCategoryController.getAllDrugCategoryList(drugCategoryListCallback);
    }
    private void refreshRecyclerView(){
       pres_drug_select_recyc_adaptor.notifyDataSetChanged();
    }
    private void refreshDrugAutoCompleteSpinner() {
        drugAutoCompleteAdaptor = new DrugAutoCompleteAdaptor(this, R.layout.drug_adaptor_item_layout, drugList, drugCategoryList);
        drugAutoCompleteAdaptor.notifyDataSetChanged();
        drug_auto_txt.setAdapter(drugAutoCompleteAdaptor);
    }
}