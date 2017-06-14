package lk.ac.mrt.cse.medipal.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.model.Patient;

/**
 * Created by chand on 2017-06-14.
 */

public class PatientInfoRecyclerAdaptor extends RecyclerView.Adapter<PatientInfoRecyclerAdaptor.RecyclerViewHolder> {

    private Patient patient;

    public PatientInfoRecyclerAdaptor(Patient patient) {
        this.patient = patient;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_patient_basic_information, parent, false);
        PatientInfoRecyclerAdaptor.RecyclerViewHolder recyclerViewHolder = new PatientInfoRecyclerAdaptor.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.input_nic.setText(patient.getNic());
        holder.input_name.setText(patient.getName());
        holder.input_birthday.setText(patient.getBirthday());
        holder.input_email.setText(patient.getEmail());
        holder.input_mobile.setText(patient.getMobile());
        holder.input_emergency_contact.setText(patient.getEmergency_contact());
        holder.input_gender.setText(patient.getGender());
        holder.expandable_relative_layout.collapse();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView input_nic;
        private TextView input_name;
        private TextView input_gender;
        private TextView input_birthday;
        private TextView input_email;
        private TextView input_mobile;
        private TextView input_emergency_contact;
        private Button btn_view_more;
        private ExpandableRelativeLayout expandable_relative_layout;

        public RecyclerViewHolder(View itemView) {
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


            btn_view_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (expandable_relative_layout.isExpanded()) {
                        expandable_relative_layout.collapse();
                    } else {
                        expandable_relative_layout.expand();
                    }
                }
            });
        }
    }
}
