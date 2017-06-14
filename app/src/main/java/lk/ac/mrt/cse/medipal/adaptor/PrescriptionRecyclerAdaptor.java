package lk.ac.mrt.cse.medipal.adaptor;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.model.Prescription;

/**
 * Created by chand on 2017-06-13.
 */

public class PrescriptionRecyclerAdaptor extends RecyclerView.Adapter<PrescriptionRecyclerAdaptor.PredictionRecyclerViewHolder> implements View.OnClickListener{
    ArrayList<Prescription> predictionList = null;
    Context context = null;
    Activity activity;
    public PrescriptionRecyclerAdaptor(ArrayList<Prescription> predictionList, Context context) {
        this.context = context;
        this.predictionList = predictionList;
        this.activity = activity;
    }

    @Override
    public PrescriptionRecyclerAdaptor.PredictionRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_row_layout,parent,false);
        PrescriptionRecyclerAdaptor.PredictionRecyclerViewHolder recyclerViewHolder = new PrescriptionRecyclerAdaptor.PredictionRecyclerViewHolder(view, context, predictionList);
        return recyclerViewHolder;
    }
    @Override
    public void onBindViewHolder(PrescriptionRecyclerAdaptor.PredictionRecyclerViewHolder holder, int position) {
        Prescription prescription = predictionList.get(position);
    }
    @Override
    public int getItemCount() {
        return predictionList.size();
    }
    @Override
    public void onClick(View view) {

    }

    public class PredictionRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ArrayList<Prescription> patientList = null;
        Context context = null;
        public PredictionRecyclerViewHolder(View itemView, Context context, ArrayList<Prescription> patientList) {
            super(itemView);
            this.context =context;
            this.patientList = patientList;
        }

        @Override
        public void onClick(View view) {
        }
    }
}

