package lk.ac.mrt.cse.medipal.adaptor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.controller.DrugCategoryController;
import lk.ac.mrt.cse.medipal.controller.DrugController;
import lk.ac.mrt.cse.medipal.controller.PredictionController;
import lk.ac.mrt.cse.medipal.controller.PrescriptionController;
import lk.ac.mrt.cse.medipal.model.ConflictScoreValue;
import lk.ac.mrt.cse.medipal.model.Disease;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.DrugChangeRequest;
import lk.ac.mrt.cse.medipal.model.network.DrugSuggestion;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import lk.ac.mrt.cse.medipal.util.StringUtil;
import lk.ac.mrt.cse.medipal.view.doctor.PrescriptionDrugSelectionActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lakshan on 11/15/17.
 */

public class PrescriptionDrugSelectionRecyclerAdaptor extends RecyclerView.Adapter<PrescriptionDrugSelectionRecyclerAdaptor.PrescriptiontionDrugRecyclerViewHolder>  {
    private Context context;
    private Patient patient;
    private Disease disease;
    final Calendar calendar = Calendar.getInstance();
    private ArrayList<PrescriptionDrug> prescriptionDrugList;
    private ArrayList<Drug> drugList;
    private PrescriptionDrug lastPrescriptionDrug;
    private String[] unitTypes = {"mg", "ml", "N/A"};
    private String[] frequencyTypes = {"day", "week", "month"};
    private String[] useTimes = {Common.Prescription.USE_TIME_BEFORE_MEAL, Common.Prescription.USE_TIME_AFTER_MEAL};
    private String[] durationTypes = {Common.Prescription.DAYS_TXT, Common.Prescription.WEEKS_TXT, Common.Prescription.MONTHS_TXT};
    private ArrayList<ConflictScoreValue> conflictScoreValues;

    public PrescriptionDrugSelectionRecyclerAdaptor(Context context, ArrayList<PrescriptionDrug> prescriptionDrugList, ArrayList<Drug> drugList, PrescriptionDrug lastPrescriptionDrug, Patient patient, Disease disease) {
        this.context = context;
        this.prescriptionDrugList = prescriptionDrugList;
        this.drugList = drugList;
        this.lastPrescriptionDrug = lastPrescriptionDrug;
        this.patient = patient;
        this.disease = disease;
    }

    @Override
    public PrescriptiontionDrugRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pres_med_selection_recyc_view,parent,false);
        return new PrescriptionDrugSelectionRecyclerAdaptor.PrescriptiontionDrugRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PrescriptiontionDrugRecyclerViewHolder holder, int position) {

        setElementValues(holder, position);
        holder.setNoConflictState();
        holder.setIsRecyclable(false);
        holder.expandablelayout_pres_med_linear.initLayout();
        holder.expandablelayout_pres_med_linear.setInRecyclerView(true);
        holder.collapse();
    }

    @Override
    public int getItemCount() {
        return prescriptionDrugList.size();
    }

    public class PrescriptiontionDrugRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ArrayAdapter<String> unitTypeAdaptor;
        private ArrayAdapter<String> frequencyTypeAdaptor;
        private ArrayAdapter<String> useTimeAdaptor;
        private ArrayAdapter<String> durationAdaptor;
        private ArrayList<Drug> categoryDrugList = new ArrayList<>();
        private TextView image_txt;
        private TextView drugNmae_txt;
        private TextView unit_size_txt;
        private TextView dosage_txt;
        private TextView use_time_txt;
        private TextView duration_txt;
        private ExpandableLinearLayout expandablelayout_pres_med_linear;
        private MaterialEditText input_unitsize;
        private MaterialEditText input_frequency;
        private MaterialEditText input_dosage;
        private MaterialEditText input_duration;
        private MaterialEditText input_start_date;
        private AppCompatSpinner unit_type_spinner;
        private AppCompatSpinner use_time_spinner;
        private AppCompatSpinner frequency_type_spinner;
        private AppCompatSpinner duration_type_spinner;
        private RelativeLayout drug_row_layout_rel;
        private DatePickerDialog.OnDateSetListener date;
        private ProgressBar progress_bar;
        private MaterialFancyButton btn_show_alternatives;
        private MaterialFancyButton btn_show_conflicts;
        private RecyclerView alternative_recycler_view;
        private LinearLayoutManager alternativeDruglayoutManager;
        private DrugAlternativesAdaptor drugAlternativesAdaptor;

        public PrescriptiontionDrugRecyclerViewHolder(View view) {
            super(view);
            image_txt = view.findViewById(R.id.image_txt);
            drugNmae_txt = view.findViewById(R.id.drugNmae_txt);
            unit_size_txt = view.findViewById(R.id.unit_size_txt);
            dosage_txt = view.findViewById(R.id.dosage_txt);
            use_time_txt = view.findViewById(R.id.use_time_txt);
            duration_txt = view.findViewById(R.id.duration_txt);
            expandablelayout_pres_med_linear = view.findViewById(R.id.expandablelayout_pres_med_linear);
            input_unitsize = view.findViewById(R.id.input_unitsize);
            input_dosage = view.findViewById(R.id.input_dosage);
            input_frequency = view.findViewById(R.id.input_frequency);
            input_duration = view.findViewById(R.id.input_duration);
            input_start_date = view.findViewById(R.id.input_start_date);
            unit_type_spinner = view.findViewById(R.id.unit_type_spinner);
            use_time_spinner = view.findViewById(R.id.use_time_spinner);
            duration_type_spinner = view.findViewById(R.id.duration_type_spinner);
            frequency_type_spinner = view.findViewById(R.id.frequency_type_spinner);
            drug_row_layout_rel = view.findViewById(R.id.drug_row_layout_rel);
            progress_bar = view.findViewById(R.id.progress_bar);
            alternative_recycler_view = view.findViewById(R.id.alternative_recycler_view);
            btn_show_alternatives = view.findViewById(R.id.btn_show_alternatives);
            btn_show_conflicts = view.findViewById(R.id.btn_show_conflicts);

            drug_row_layout_rel.setOnClickListener(this);
            expandablelayout_pres_med_linear.collapse();

            unitTypeAdaptor = new ArrayAdapter<String>(context, R.layout.template_spinner_pres_dropdown, unitTypes);
            frequencyTypeAdaptor = new ArrayAdapter<String>(context, R.layout.template_spinner_pres_dropdown, frequencyTypes);
            useTimeAdaptor = new ArrayAdapter<String>(context, R.layout.template_spinner_pres_dropdown, useTimes);
            durationAdaptor = new ArrayAdapter<String>(context, R.layout.template_spinner_pres_dropdown, durationTypes);

            unitTypeAdaptor.setDropDownViewResource(R.layout.template_spinner_pres);
            frequencyTypeAdaptor.setDropDownViewResource(R.layout.template_spinner_pres);
            useTimeAdaptor.setDropDownViewResource(R.layout.template_spinner_pres);
            durationAdaptor.setDropDownViewResource(R.layout.template_spinner_pres);

            unit_type_spinner.setAdapter(unitTypeAdaptor);
            frequency_type_spinner.setAdapter(frequencyTypeAdaptor);
            use_time_spinner.setAdapter(useTimeAdaptor);
            duration_type_spinner.setAdapter(durationAdaptor);

            progress_bar.setVisibility(View.GONE);
            addStartDateListener();
            setTextChangedListeners();

        }

        private void addStartDateListener() {
            date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String format = Common.DATE_FORMAT;
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
//                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
//                    prescriptionDrug.setStartDate(sdf.format(calendar.getTime()));
                    input_start_date.setText(sdf.format(calendar.getTime()));
                }

            };
            input_start_date.setOnClickListener(this);
            btn_show_alternatives.setOnClickListener(this);
            btn_show_conflicts.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.drug_row_layout_rel:
                    if (expandablelayout_pres_med_linear.isExpanded()) {
                        expandablelayout_pres_med_linear.collapse();
                    } else {
                        expandablelayout_pres_med_linear.expand();
                        hideAltDrugRecycler();
                    }
                    break;
                case R.id.input_start_date:
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                    break;
                case R.id.btn_show_alternatives:
                    if (alternative_recycler_view.getVisibility() == View.GONE) {
                        alternativeDruglayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        alternative_recycler_view.setLayoutManager(alternativeDruglayoutManager);
                        RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                int action = e.getAction();
                                switch (action) {
                                    case MotionEvent.ACTION_MOVE:
                                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                                        break;
                                }
                                return false;
                            }

                            @Override
                            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                            }
                        };
                        alternative_recycler_view.addOnItemTouchListener(mScrollTouchListener);
                        if (!categoryDrugList.isEmpty()) {
                            alternative_recycler_view.setVisibility(View.VISIBLE);
                            btn_show_alternatives.setText(Common.Prescription.TXT_HIDE_ALTERNATIVES);
                            btn_show_alternatives.setIconResource("\uf077");
                        } else {
                            retrieveCategoryDrugs();
                        }
                        //retrieveCategoryDrugs();
                    } else {
                        hideAltDrugRecycler();
                    }
                    break;
                case R.id.btn_show_conflicts:
                    if (conflictScoreValues != null) {
                        ((PrescriptionDrugSelectionActivity) context).showBottomSheet(conflictScoreValues);
                    }
                    break;
            }
        }

        public void retrieveCategoryDrugs(){
            progress_bar.setVisibility(View.VISIBLE);

            Callback<ListWrapper<Drug>> categoryMedicineListCallback = new Callback<ListWrapper<Drug>>() {
                @Override
                public void onResponse(Call<ListWrapper<Drug>> call, Response<ListWrapper<Drug>> response) {
                    ListWrapper<Drug> responseObject = response.body();
                    if (response.isSuccessful()) {
                        if (responseObject.getItems() != null) {
                            categoryDrugList = new ArrayList<>();
                            categoryDrugList.addAll(responseObject.getItems());
                            Drug currentDrug = prescriptionDrugList.get(getAdapterPosition()).getDrug();
                            for (int i=0; i< categoryDrugList.size(); i++){
                                if (categoryDrugList.get(i).getDrug_id().equals(currentDrug.getDrug_id())){
                                    categoryDrugList.remove(i);
                                    break;
                                }
                            }
                            if (!categoryDrugList.isEmpty()){
                                alternative_recycler_view.setVisibility(View.VISIBLE);
                                btn_show_alternatives.setText(Common.Prescription.TXT_HIDE_ALTERNATIVES);
                                btn_show_alternatives.setIconResource("\uf077");
                                refreshAlternativeRecycler();
                            } else {
                                Toast.makeText(context, "No Alternatives were found.",Toast.LENGTH_LONG );
                            }

                        } else {
                            Toast.makeText(context, "No Alternatives were found.",Toast.LENGTH_LONG );
                        }
                    } else {
                        Toast.makeText(context, Common.ERROR_OCCURED_TXT + response.message(), Toast.LENGTH_LONG).show();
                    }
                    progress_bar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ListWrapper<Drug>> call, Throwable t) {
                    Toast.makeText(context, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                    progress_bar.setVisibility(View.GONE);
                }
            };
            DrugCategoryController drugCategoryController = new DrugCategoryController();
            drugCategoryController.getAllCategoryDrugList(categoryMedicineListCallback, prescriptionDrugList.get(getAdapterPosition()).getDrug().getCategory_id());
        }

        public void refreshAlternativeRecycler(){
            drugAlternativesAdaptor = new DrugAlternativesAdaptor(context, categoryDrugList, this);
            alternative_recycler_view.setAdapter(drugAlternativesAdaptor);
        }

        public void hideAltDrugRecycler(){
            alternative_recycler_view.setVisibility(View.GONE);
            btn_show_alternatives.setText(Common.Prescription.TXT_SHOW_ALTERNATIVES);
            btn_show_alternatives.setIconResource("\uf078");
        }

        public void setTextChangedListeners(){

            input_unitsize.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
                    if (charSequence.length() != 0){
                        prescriptionDrug.setUnitSize(charSequence+""+unit_type_spinner.getSelectedItem());
                    } else {
                        prescriptionDrug.setUnitSize("");
                    }
                    unit_size_txt.setText(prescriptionDrug.getUnitSize());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            input_dosage.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
                    if (charSequence.length() != 0){
                        prescriptionDrug.setDosage(charSequence +" "+Common.Prescription.UNITS_TXT);
                    } else {
                        prescriptionDrug.setDosage("");
                    }
                    if (prescriptionDrug.getFrequency() != null) {
                        dosage_txt.setText(prescriptionDrug.getDosage() + " " + prescriptionDrug.getFrequency());
                    }else {
                        dosage_txt.setText(prescriptionDrug.getDosage());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            input_frequency.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
                    if (charSequence.length() != 0){
                        prescriptionDrug.setFrequency(charSequence +" "+Common.Prescription.TIMES_TXT+(String)frequency_type_spinner.getSelectedItem());
                    } else {
                        prescriptionDrug.setFrequency("");
                    }
                    if (prescriptionDrug.getDosage() != null) {
                        dosage_txt.setText(prescriptionDrug.getDosage() + " " + prescriptionDrug.getFrequency());
                    }else {
                        dosage_txt.setText(prescriptionDrug.getFrequency());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            input_duration.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
                    if (charSequence.length() != 0){
                        int no_of_days = getDurationInDays(charSequence);
                        prescriptionDrug.setDuration(no_of_days);
                        duration_txt.setText(String.valueOf(prescriptionDrug.getDuration()) + " " + Common.Prescription.DAYS_TXT +" "+ Common.Prescription.FROM_TXT+" "+prescriptionDrug.getStartDate());
                    } else {
                        prescriptionDrug.setDuration(0);
                        duration_txt.setText(String.valueOf( Common.Prescription.FROM_TXT+" "+prescriptionDrug.getStartDate()));
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            input_start_date.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
                    if (charSequence.length() != 0){
                        prescriptionDrug.setStartDate(charSequence.toString());
                        if (prescriptionDrug.getDuration() != 0)
                        duration_txt.setText(prescriptionDrug.getDuration()+" "+Common.Prescription.DAYS_TXT+" "+ Common.Prescription.FROM_TXT + " " +prescriptionDrug.getStartDate());
                        else duration_txt.setText(Common.Prescription.FROM_TXT + " " +prescriptionDrug.getStartDate());
                    } else {
                        prescriptionDrug.setDuration(0);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            use_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
                    prescriptionDrug.setUseTime(useTimeAdaptor.getItem(i));
                    use_time_txt.setText(prescriptionDrug.getUseTime());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            unit_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
                    if (prescriptionDrug.getUnitSize() != null && !prescriptionDrug.getUnitSize().isEmpty()) {
                        prescriptionDrug.setUnitSize(StringUtil.getPreNumericValue(prescriptionDrug.getUnitSize())+unitTypeAdaptor.getItem(i));
                        unit_size_txt.setText(prescriptionDrug.getUnitSize());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            frequency_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
                    if (prescriptionDrug.getFrequency() != null && !prescriptionDrug.getFrequency().isEmpty()) {
                        prescriptionDrug.setFrequency(StringUtil.getPreNumericValue(prescriptionDrug.getFrequency())+" "+Common.Prescription.TIMES_TXT+frequencyTypeAdaptor.getItem(i));
                        dosage_txt.setText(prescriptionDrug.getDosage()+" "+prescriptionDrug.getFrequency());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            duration_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
                    if (prescriptionDrug.getDuration() != 0) {
                        prescriptionDrug.setDuration(getDurationInDays(input_duration.getText()));
                        duration_txt.setText(String.valueOf(prescriptionDrug.getDuration()) + " " + Common.Prescription.DAYS_TXT+" "+ Common.Prescription.FROM_TXT+" "+prescriptionDrug.getStartDate());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
        private int getDurationInDays(CharSequence charSequenc){
            int no_of_days = Integer.parseInt(charSequenc.toString());
            switch ((String)duration_type_spinner.getSelectedItem()){
                case Common.Prescription.WEEKS_TXT:
                    no_of_days = no_of_days * Common.Prescription.DAYS_IN_WEEK;
                    break;
                case Common.Prescription.MONTHS_TXT:
                    no_of_days = no_of_days * Common.Prescription.DAYS_IN_MONTH;
                    break;
            }
            return no_of_days;
        }

        public MaterialEditText getInput_unitsize() {
            return input_unitsize;
        }

        public MaterialEditText getInput_frequency() {
            return input_frequency;
        }

        public MaterialEditText getInput_dosage() {
            return input_dosage;
        }

        public MaterialEditText getInput_duration() {
            return input_duration;
        }

        public ExpandableLinearLayout getExpandablelayout_pres_med_linear() {
            return expandablelayout_pres_med_linear;
        }

        public RelativeLayout getDrug_row_layout_rel() {
            return drug_row_layout_rel;
        }

        public void collapse(){
            if (expandablelayout_pres_med_linear.isExpanded()) {
                expandablelayout_pres_med_linear.collapse();
            }
        }
        public void expand(){
            if (!expandablelayout_pres_med_linear.isExpanded()) {
                expandablelayout_pres_med_linear.expand();
            }
        }

        public void setConflictState(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                drug_row_layout_rel.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
            }else {
                drug_row_layout_rel.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            btn_show_conflicts.setVisibility(View.VISIBLE);
        }
        public void setNoConflictState(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                drug_row_layout_rel.setBackgroundColor(context.getResources().getColor(R.color.met_baseColor, context.getTheme()));
            }else {
                drug_row_layout_rel.setBackgroundColor(context.getResources().getColor(R.color.met_baseColor));
            }
            btn_show_conflicts.setVisibility(View.GONE);
        }

        public void exchangeDrug(Drug newDrug){
            prescriptionDrugList.get(getAdapterPosition()).setDrug(newDrug);
            lastPrescriptionDrug = prescriptionDrugList.get(getAdapterPosition());
            hideAltDrugRecycler();
            notifyDataSetChanged();
        }


        private void checkConflicts() {
            progress_bar.setVisibility(View.VISIBLE);

            final Callback<DrugSuggestion> suggestionResponse = new Callback<DrugSuggestion>() {
                @Override
                public void onResponse(Call<DrugSuggestion> call, Response<DrugSuggestion> response) {
                    DrugSuggestion responseObject = response.body();
                    if (response.isSuccessful()) {
                        if (responseObject.getConflictScoreValueArrayList() != null && !responseObject.getConflictScoreValueArrayList().isEmpty()) {
                            conflictScoreValues = responseObject.getConflictScoreValueArrayList();
                            setConflictState();
                            expand();
                            progress_bar.setVisibility(View.GONE);
                        }else {
                            conflictScoreValues = null;
                            progress_bar.setVisibility(View.GONE);
                            setNoConflictState();
                        }
                        if (responseObject.getSuggested_drugs() != null){
                            categoryDrugList = new ArrayList<>();
                            categoryDrugList.addAll(responseObject.getSuggested_drugs());
                            Drug currentDrug = prescriptionDrugList.get(getAdapterPosition()).getDrug();
                            for (int i=0; i< categoryDrugList.size(); i++){
                                if (categoryDrugList.get(i).getDrug_id().equals(currentDrug.getDrug_id())){
                                    categoryDrugList.remove(i);
                                    break;
                                }
                            }
                            refreshAlternativeRecycler();
                        }
                    }else {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(context, "Error checking conflicts. Try Again.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<DrugSuggestion> call, Throwable t) {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(context, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                }
            };
            ArrayList<Drug> currentDrugList = new ArrayList<>();
            for (PrescriptionDrug prescriptionDrug : prescriptionDrugList){
                currentDrugList.add(prescriptionDrug.getDrug());
            }
            DrugChangeRequest drugChangeRequest = new DrugChangeRequest(lastPrescriptionDrug.getDrug(),currentDrugList, patient.getNic(), disease.getDisease_name());
            PredictionController predictionController = new PredictionController();
            predictionController.getDrugSuggestions(suggestionResponse, drugChangeRequest);
        }
    }

    private void setElementValues(PrescriptiontionDrugRecyclerViewHolder holder, int position){
        PrescriptionDrug prescriptionDrug = prescriptionDrugList.get(position);
        holder.drugNmae_txt.setText(prescriptionDrugList.get(position).getDrug().getDrug_name());
        holder.image_txt.setText(prescriptionDrugList.get(position).getDrug().getDrug_name().substring(0,1).toUpperCase());
        if (prescriptionDrug.getUnitSize() != null && !prescriptionDrug.getUnitSize().isEmpty()){
            holder.unit_size_txt.setText(prescriptionDrug.getUnitSize());

            holder.input_unitsize.setText(StringUtil.getPreNumericValue(prescriptionDrug.getUnitSize()));
            holder.unit_type_spinner.setSelection(holder.unitTypeAdaptor.getPosition(StringUtil.getPostAlphebeticValue(prescriptionDrug.getUnitSize())), true);
        }else {
            holder.unit_size_txt.setText("");
        }
        if (prescriptionDrug.getDosage() != null && !prescriptionDrug.getDosage().isEmpty()){
            holder.dosage_txt.setText(prescriptionDrug.getDosage());
            holder.input_dosage.setText(StringUtil.getPreNumericValue(prescriptionDrug.getDosage()));
        }else {
            holder.dosage_txt.setText("");
        }
        if (prescriptionDrug.getFrequency() != null && !prescriptionDrug.getFrequency().isEmpty()){
            holder.dosage_txt.setText(holder.dosage_txt.getText()+" "+prescriptionDrug.getFrequency());

            holder.input_frequency.setText(StringUtil.getPreNumericValue(prescriptionDrug.getFrequency()));
            holder.frequency_type_spinner.setSelection(holder.frequencyTypeAdaptor.getPosition(StringUtil.getFrequencyUnit(prescriptionDrug.getFrequency())), true);
        }
        if (prescriptionDrug.getUseTime() != null && !prescriptionDrug.getUseTime().isEmpty()){
            holder.use_time_txt.setText(prescriptionDrug.getUseTime());

            holder.use_time_spinner.setSelection(holder.useTimeAdaptor.getPosition(prescriptionDrug.getUseTime()), true);
        }else {
            holder.use_time_txt.setText("");
        }
        if (prescriptionDrug.getDuration() != 0){
            holder.duration_txt.setText(String.valueOf(prescriptionDrug.getDuration()) + " " + Common.Prescription.DAYS_TXT);

            if (prescriptionDrug.getDuration() % Common.Prescription.DAYS_IN_MONTH == 0) {
                holder.input_duration.setText(String.valueOf(prescriptionDrug.getDuration()/Common.Prescription.DAYS_IN_MONTH));
                holder.duration_type_spinner.setSelection(holder.durationAdaptor.getPosition(Common.Prescription.MONTHS_TXT));
            } else if (prescriptionDrug.getDuration() % Common.Prescription.DAYS_IN_WEEK == 0){
                holder.input_duration.setText(String.valueOf(prescriptionDrug.getDuration()/Common.Prescription.DAYS_IN_WEEK));
                holder.duration_type_spinner.setSelection(holder.durationAdaptor.getPosition(Common.Prescription.WEEKS_TXT));
            } else {
                holder.input_duration.setText(String.valueOf(prescriptionDrug.getDuration()));
                holder.duration_type_spinner.setSelection(holder.durationAdaptor.getPosition(Common.Prescription.DAYS_TXT));
            }
        }else {
            holder.duration_txt.setText("");
        }
        if (prescriptionDrug.getStartDate() != null && !prescriptionDrug.getStartDate().isEmpty()){
            holder.duration_txt.setText(holder.duration_txt.getText() +" "+ Common.Prescription.FROM_TXT + " " +prescriptionDrug.getStartDate());

            holder.input_start_date.setText(prescriptionDrug.getStartDate());
        } else {
            holder.input_start_date.setText(getCurrentDate());
        }
        if (lastPrescriptionDrug != null && prescriptionDrugList.get(position).getDrug().getDrug_id().equals(lastPrescriptionDrug.getDrug().getDrug_id())) {
            holder.checkConflicts();
        } else {
            holder.progress_bar.setVisibility(View.GONE);
        }
        holder.alternative_recycler_view.setVisibility(View.GONE);
    }
    private String getCurrentDate(){
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.DAY_OF_MONTH);
        String format = Common.DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }

    public void setLastPrescriptionDrug(PrescriptionDrug lastPrescriptionDrug){
        this.lastPrescriptionDrug = lastPrescriptionDrug;

    }
}
