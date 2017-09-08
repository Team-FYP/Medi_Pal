package lk.ac.mrt.cse.medipal.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.util.VectorDrawableUtil;

/**
 * Created by yasiru on 9/8/17.
 */

public class PatientOwnPrescriptionRecyclerAdaptor extends RecyclerView.Adapter<PatientOwnPrescriptionRecyclerAdaptor.PrescriptiontionRecyclerViewHolder> {
    ArrayList<Prescription> prescriptionList = null;
    Context context = null;
    public PatientOwnPrescriptionRecyclerAdaptor(ArrayList<Prescription> prescriptionList, Context context) {
        this.context = context;
        this.prescriptionList = prescriptionList;
    }

    @Override
    public PatientOwnPrescriptionRecyclerAdaptor.PrescriptiontionRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_row_layout,parent,false);
        PatientOwnPrescriptionRecyclerAdaptor.PrescriptiontionRecyclerViewHolder recyclerViewHolder = new PatientOwnPrescriptionRecyclerAdaptor.PrescriptiontionRecyclerViewHolder(view, context, viewType);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(PrescriptiontionRecyclerViewHolder holder, int position) {
        Prescription prescription = prescriptionList.get(position);
    }

    @Override
    public int getItemCount() {
        return (prescriptionList!=null? prescriptionList.size():0);
    }


    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    public class PrescriptiontionRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ArrayList<Prescription> prescriptionList = null;
        Context context = null;
        TimelineView timeLineView;

        public PrescriptiontionRecyclerViewHolder(View itemView, Context context, int viewType) {
            super(itemView);
            this.context =context;
            timeLineView = (TimelineView) itemView.findViewById(R.id.time_marker);
            timeLineView.setMarker(VectorDrawableUtil.getDrawable(context, R.drawable.icon_time_line_marker, R.color.colorPrimary));
            this.prescriptionList = prescriptionList;
            timeLineView.initLine(viewType);
        }
        @Override
        public void onClick(View view) {
        }

    }
}
