package lk.ac.mrt.cse.medipal.adaptor;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;

/**
 * Created by lakshan on 11/15/17.
 */

public class PrescriptionDrugSelectionRecyclerAdaptor extends RecyclerView.Adapter<PrescriptionDrugSelectionRecyclerAdaptor.PrescriptiontionDrugRecyclerViewHolder>  {
    private Context context;
    private ArrayList<PrescriptionDrug> prescriptionDrugList;
    private ArrayList<Drug> drugList;

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
        holder.drugNmae_txt.setText(prescriptionDrugList.get(position).getDrug().getDrug_name());
        holder.image_txt.setText(prescriptionDrugList.get(position).getDrug().getDrug_name().substring(0,1).toUpperCase());
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
        private TextView image_txt;
        private TextView drugNmae_txt;
        private TextView unit_size_txt;
        private TextView dosage_txt;
        private TextView use_time_txt;
        private TextView duration_txt;
        private ExpandableLinearLayout expandablelayout_pres_med_linear;
        private MaterialEditText input_unitsize;
        private AppCompatSpinner unit_type_spinner;
        private MaterialEditText input_dosage;
        private RelativeLayout drug_row_layout_rel;

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
            unit_type_spinner = view.findViewById(R.id.unit_type_spinner);
            input_dosage = view.findViewById(R.id.input_dosage);
            drug_row_layout_rel = view.findViewById(R.id.drug_row_layout_rel);

            drug_row_layout_rel.setOnClickListener(this);
            expandablelayout_pres_med_linear.collapse();


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
            }
        }
    }
}
