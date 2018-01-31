package lk.ac.mrt.cse.medipal.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;

/**
 * Created by chand on 2017-06-14.
 */

public class PatientInfoRecyclerAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Patient patient;
    private ArrayList<PrescriptionDrug> prescriptionDrugs;
    private Context context;

    public PatientInfoRecyclerAdaptor(Context context, Patient patient, ArrayList<PrescriptionDrug> prescriptionDrugs) {
        this.context = context;
        this.patient = patient;
        this.prescriptionDrugs = prescriptionDrugs;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_patient_basic_information, parent, false);
                return new InfoRecyclerViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_patient_current_medicine, parent, false);
                return new RecentMedRecyclerViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_patient_basic_information, parent, false);
                return new InfoRecyclerViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                InfoRecyclerViewHolder infoHolder = ((InfoRecyclerViewHolder)holder);
                infoHolder.input_nic.setText(patient.getNic());
                infoHolder.input_name.setText(patient.getName());
                infoHolder.input_birthday.setText(patient.getBirthday());
                infoHolder.input_email.setText(patient.getEmail());
                infoHolder.input_mobile.setText(patient.getMobile());
                infoHolder.input_emergency_contact.setText(patient.getEmergency_contact());
                infoHolder.input_gender.setText(patient.getGender());
                infoHolder.expandable_relative_layout.collapse();
                break;
            case 1:
                RecentMedRecyclerViewHolder medHolder = ((RecentMedRecyclerViewHolder)holder);
                if (prescriptionDrugs.size() <= 3){
                    medHolder.btn_view_more.setVisibility(View.GONE);
                } else {
                    medHolder.btn_view_more.setVisibility(View.VISIBLE);
                }
                medHolder.layout_curr_med_linear.removeAllViews();
                for (int i = 0; i < prescriptionDrugs.size(); i++){
                    if (i < 3) {
                        addMedicinetoLinear(prescriptionDrugs.get(i), medHolder.layout_curr_med_linear);
                    } else {
                        addMedicinetoLinear(prescriptionDrugs.get(i), medHolder.expandable_curr_med_linear);
                    }
                }
                medHolder.expandablelayout_curr_med_relative.collapse();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class InfoRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView input_nic;
        private TextView input_name;
        private TextView input_gender;
        private TextView input_birthday;
        private TextView input_email;
        private TextView input_mobile;
        private TextView input_emergency_contact;
        private Button btn_view_more;
        private ExpandableRelativeLayout expandable_relative_layout;
        public InfoRecyclerViewHolder(View itemView) {
            super(itemView);
            input_nic = (TextView) itemView.findViewById(R.id.input_nic);
            input_name = (TextView) itemView.findViewById(R.id.input_name);
            input_birthday = (TextView) itemView.findViewById(R.id.input_birthday);
            input_email = (TextView) itemView.findViewById(R.id.input_email);
            input_mobile = (TextView) itemView.findViewById(R.id.input_mobile);
            input_emergency_contact = (TextView) itemView.findViewById(R.id.input_emergency_contact);
            input_gender = (TextView) itemView.findViewById(R.id.input_gender);
            btn_view_more = (Button) itemView.findViewById(R.id.btn_view_more);
            expandable_relative_layout = (ExpandableRelativeLayout) itemView.findViewById(R.id.expandablelayout_basic_info_relative);
            btn_view_more.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_view_more) {
                if (expandable_relative_layout.isExpanded()) {
                    expandable_relative_layout.collapse();
                    btn_view_more.setText(R.string.view_more);
                } else {
                    expandable_relative_layout.expand();
                    btn_view_more.setText(R.string.view_less);
                }
            }
        }
    }
    public class RecentMedRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout layout_curr_med_linear;
        private LinearLayout expandable_curr_med_linear;
        private ExpandableRelativeLayout expandablelayout_curr_med_relative;
        private Button btn_view_more;

        public RecentMedRecyclerViewHolder(View itemView) {
            super(itemView);
            layout_curr_med_linear = itemView.findViewById(R.id.layout_curr_med_linear);
            expandable_curr_med_linear = itemView.findViewById(R.id.expandable_curr_med_linear);
            btn_view_more = itemView.findViewById(R.id.btn_view_more);
            expandablelayout_curr_med_relative = itemView.findViewById(R.id.expandablelayout_curr_med_relative);

            btn_view_more.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_view_more) {
                if (expandablelayout_curr_med_relative.isExpanded()) {
                    expandablelayout_curr_med_relative.collapse();
                    btn_view_more.setText(R.string.view_more);
                } else {
                    expandablelayout_curr_med_relative.expand();
                    btn_view_more.setText(R.string.view_less);
                }
            }
        }
    }
    private void addMedicinetoLinear(PrescriptionDrug prescriptionDrug, LinearLayout linearLayout){
        LayoutInflater li = LayoutInflater.from(context);
        View single_medication_row = li.inflate(R.layout.single_medication_row, null);
        TextView image_txt = single_medication_row.findViewById(R.id.image_txt);
        TextView drugNmae_txt = single_medication_row.findViewById(R.id.drugNmae_txt);
        TextView unit_size_txt= single_medication_row.findViewById(R.id.unit_size_txt);
        TextView dosage_txt= single_medication_row.findViewById(R.id.dosage_txt);
        TextView use_time_txt = single_medication_row.findViewById(R.id.use_time_txt);
        TextView duration_txt = single_medication_row.findViewById(R.id.duration_txt);

        image_txt.setText(prescriptionDrug.getDrug().getDrug_name().substring(0,1).toUpperCase());
        drugNmae_txt.setText(prescriptionDrug.getDrug().getDrug_name());
        unit_size_txt.setText(prescriptionDrug.getUnitSize());
        dosage_txt.setText(prescriptionDrug.getDosage()+ " " + prescriptionDrug.getFrequency());
        use_time_txt.setText(prescriptionDrug.getUseTime());
        duration_txt.setText(String.format(Common.DURATION_TXT_VALUE, prescriptionDrug.getDuration(), prescriptionDrug.getStartDate()));
        linearLayout.addView(single_medication_row);
    }
}
