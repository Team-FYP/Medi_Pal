package lk.ac.mrt.cse.medipal.adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.gson.Gson;

import java.util.ArrayList;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.util.StringUtil;
import lk.ac.mrt.cse.medipal.util.VectorDrawableUtil;
import lk.ac.mrt.cse.medipal.view.patient.PrescriptionViewActivity;

/**
 * Created by chand on 2017-06-13.
 */

public class PrescriptionSelfRecyclerAdaptor extends RecyclerView.Adapter<PrescriptionSelfRecyclerAdaptor.PredictionRecyclerViewHolder>{
    private ArrayList<Prescription> prescriptionList;
    private Context context;

    public PrescriptionSelfRecyclerAdaptor(ArrayList<Prescription> prescriptionList, Context context) {
        this.context = context;
        this.prescriptionList = prescriptionList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }
    @Override
    public PrescriptionSelfRecyclerAdaptor.PredictionRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_row_layout,parent,false);
        return new PredictionRecyclerViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(PrescriptionSelfRecyclerAdaptor.PredictionRecyclerViewHolder holder, int position) {
        Prescription prescription = prescriptionList.get(position);
        holder.pres_date_txt.setText(StringUtil.getDateIn2LineFormat(prescription.getPrescription_date()));
        holder.doctor_name_txt.setText(StringUtil.getDoctorName(prescription.getDoctor().getName()));
        Uri doc_image_uri = Uri.parse(prescription.getDoctor().getImage());
        holder.doctor_image_drawee.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(holder.doctor_image_drawee.getController())
                        .setUri(doc_image_uri)
                        .build());
        holder.disease_txt.setText(prescription.getDisease_id());
        ArrayList<PrescriptionDrug> prescriptionDrugs = prescription.getPrescription_drugs();
        if (prescriptionDrugs.size() <= 2){
            holder.btn_view_more.setVisibility(View.GONE);
        } else {
            holder.btn_view_more.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < prescriptionDrugs.size(); i++){
            if (i < 2) {
                addMedicinetoLinear(prescriptionDrugs.get(i), holder.layout_curr_med_linear);
            } else {
                addMedicinetoLinear(prescriptionDrugs.get(i), holder.expandable_curr_med_linear);
            }
        }
        holder.expandablelayout_curr_med_relative.collapse();
    }


    @Override
    public int getItemCount() {
        return (prescriptionList!=null? prescriptionList.size():0);
    }

    public class PredictionRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TimelineView timeLineView;
        private LinearLayout layout_curr_med_linear;
        private LinearLayout expandable_curr_med_linear;
        private ExpandableRelativeLayout expandablelayout_curr_med_relative;
        private Button btn_view_more;
        private TextView pres_date_txt;
        private TextView doctor_name_txt;
        private TextView disease_txt;
        private SimpleDraweeView doctor_image_drawee;
        private LinearLayout prescription_layout_outer;

        public PredictionRecyclerViewHolder(View itemView, int viewType) {
            super(itemView);
            timeLineView = (TimelineView) itemView.findViewById(R.id.time_marker);
            timeLineView.setMarker(VectorDrawableUtil.getDrawable(context, R.drawable.icon_time_line_marker, R.color.colorPrimary));
            timeLineView.initLine(viewType);
            layout_curr_med_linear = itemView.findViewById(R.id.layout_curr_med_linear);
            expandable_curr_med_linear = itemView.findViewById(R.id.expandable_curr_med_linear);
            btn_view_more = itemView.findViewById(R.id.btn_view_more);
            expandablelayout_curr_med_relative = itemView.findViewById(R.id.expandablelayout_curr_med_relative);
            pres_date_txt = itemView.findViewById(R.id.pres_date_txt);
            doctor_name_txt = itemView.findViewById(R.id.txt_doctor_name);
            disease_txt = itemView.findViewById(R.id.disease_txt);
            doctor_image_drawee = itemView.findViewById(R.id.doctor_image_drawee);
            prescription_layout_outer = itemView.findViewById(R.id.prescription_layout_outer);

            btn_view_more.setOnClickListener(this);
            prescription_layout_outer.setOnClickListener(this);
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
            } else if (view.getId() == R.id.prescription_layout_outer){
                Intent intent = new Intent(context, PrescriptionViewActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(prescriptionList.get(getAdapterPosition()));
                intent.putExtra(ObjectType.OBJECT_TYPE_PRESCRIPTION, json);
                context.startActivity(intent);
            }
        }
    }

    private void addMedicinetoLinear(PrescriptionDrug prescriptionDrug, LinearLayout linearLayout){
        LayoutInflater li = LayoutInflater.from(context);
        View single_medication_row = li.inflate(R.layout.single_medication_row_small, null);
        TextView image_txt = single_medication_row.findViewById(R.id.image_txt);
        TextView drugNmae_txt = single_medication_row.findViewById(R.id.drugNmae_txt);
        TextView unit_size_txt= single_medication_row.findViewById(R.id.unit_size_txt);
        TextView dosage_txt= single_medication_row.findViewById(R.id.dosage_txt);
        TextView use_time_txt = single_medication_row.findViewById(R.id.use_time_txt);
        TextView duration_txt = single_medication_row.findViewById(R.id.duration_txt);

        image_txt.setText(prescriptionDrug.getDrug().getDrug_name().substring(0,1).toUpperCase());
        drugNmae_txt.setText(prescriptionDrug.getDrug().getDrug_name());
        unit_size_txt.setText(prescriptionDrug.getUnitSize());
        dosage_txt.setText(prescriptionDrug.getDosage()+" "+ prescriptionDrug.getFrequency());
        use_time_txt.setText(prescriptionDrug.getUseTime());
        duration_txt.setText(String.format(Common.DURATION_TXT_VALUE, prescriptionDrug.getDuration(), prescriptionDrug.getStartDate()));
        linearLayout.addView(single_medication_row);
    }

}

