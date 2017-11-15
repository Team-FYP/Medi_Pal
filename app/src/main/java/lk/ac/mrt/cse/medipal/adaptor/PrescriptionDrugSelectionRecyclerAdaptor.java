package lk.ac.mrt.cse.medipal.adaptor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.view.patient.PatientRegisterActivity;

/**
 * Created by lakshan on 11/15/17.
 */

public class PrescriptionDrugSelectionRecyclerAdaptor extends RecyclerView.Adapter<PrescriptionDrugSelectionRecyclerAdaptor.PrescriptiontionDrugRecyclerViewHolder>  {
    private Context context;
    final Calendar calendar = Calendar.getInstance();
    private ArrayList<PrescriptionDrug> prescriptionDrugList;
    private ArrayList<Drug> drugList;
    private String[] unitTypes = {"mg", "ml", "N/A"};
    private String[] frequencyTypes = {"day", "week", "month"};
    private String[] useTimes = {"Before Meal", "After Meal"};
    private String[] durationTypes = {"days", "weeks", "months"};

    public PrescriptionDrugSelectionRecyclerAdaptor(Context context, ArrayList<PrescriptionDrug> prescriptionDrugList, ArrayList<Drug> drugList) {
        this.context = context;
        this.prescriptionDrugList = prescriptionDrugList;
        this.drugList = drugList;
    }

    @Override
    public PrescriptiontionDrugRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pres_med_selection_recyc_view,parent,false);
        return new PrescriptionDrugSelectionRecyclerAdaptor.PrescriptiontionDrugRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PrescriptiontionDrugRecyclerViewHolder holder, int position) {

        holder.unit_type_spinner.setAdapter(holder.unitTypeAdaptor);
        holder.frequency_type_spinner.setAdapter(holder.frequencyTypeAdaptor);
        holder.use_time_spinner.setAdapter(holder.useTimeAdaptor);
        holder.duration_type_spinner.setAdapter(holder.durationAdaptor);

        holder.drugNmae_txt.setText(prescriptionDrugList.get(position).getDrug().getDrug_name());
        holder.image_txt.setText(prescriptionDrugList.get(position).getDrug().getDrug_name().substring(0,1).toUpperCase());

        holder.input_start_date.setText(getCurrentDate());

        holder.setIsRecyclable(false);
        holder.expandablelayout_pres_med_linear.initLayout();
        holder.expandablelayout_pres_med_linear.setInRecyclerView(true);
        holder.expandablelayout_pres_med_linear.collapse();
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

            addStartDateListener();
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
                    input_start_date.setText(sdf.format(calendar.getTime()));
                }

            };
            input_start_date.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.drug_row_layout_rel:
                    if (expandablelayout_pres_med_linear.isExpanded()) {
                        expandablelayout_pres_med_linear.collapse();
                    } else {
                        expandablelayout_pres_med_linear.expand();
                    }
                    break;
                case R.id.input_start_date:
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                    break;
            }
        }
    }

    private String getCurrentDate(){
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.DAY_OF_MONTH);
        String format = Common.DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }
}
