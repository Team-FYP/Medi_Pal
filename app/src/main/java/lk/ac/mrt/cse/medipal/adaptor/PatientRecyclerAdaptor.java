package lk.ac.mrt.cse.medipal.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.util.VectorDrawableUtil;
import lk.ac.mrt.cse.medipal.view.doctor.PatientInfoActivity;

/**
 * Created by Lakshan on 2017-01-07.
 */

public class PatientRecyclerAdaptor extends RecyclerView.Adapter<PatientRecyclerAdaptor.PatientRecyclerViewHolder> implements View.OnClickListener{
    ArrayList<Patient> patientList = null;
    Context context = null;
    Activity activity;
    public PatientRecyclerAdaptor(ArrayList<Patient> patientList, Context context, Activity activity) {
        this.context = context;
        this.patientList = patientList;
        this.activity = activity;
    }

    @Override
    public PatientRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_row_layout,parent,false);
        PatientRecyclerViewHolder recyclerViewHolder = new PatientRecyclerViewHolder(view, context, patientList);
        return recyclerViewHolder;
    }
    @Override
    public void onBindViewHolder(PatientRecyclerViewHolder holder, int position) {
        Patient patient = patientList.get(position);
        holder.txt_name.setText(patient.getName());
        holder.txt_nic.setText(patient.getNic());
        holder.txt_mobile.setText(patient.getMobile());
        if (patient.getImage() != null) {
            Uri imageUri = Uri.parse(patient.getImage());
            holder.contact_icon.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController(holder.contact_icon.getController())
                            .setUri(imageUri)
                            .build());
        } else if (patient.getGender().equals(Common.FEMALE_TXT)){
            holder.contact_icon.setImageResource(R.drawable.icon_user_female);
        } else if (patient.getGender().equals(Common.MALE_TXT)){
            holder.contact_icon.setImageResource(R.drawable.icon_user_male);
        }
    }
    @Override
    public int getItemCount() {
        return patientList.size();
    }
    @Override
    public void onClick(View view) {

    }

    public class PatientRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_name;
        TextView txt_mobile;
        TextView txt_nic;
        SimpleDraweeView contact_icon;
        ArrayList<Patient> patientList = null;
        Context context = null;
        public PatientRecyclerViewHolder(View itemView, Context context, ArrayList<Patient> patientList) {
            super(itemView);
            this.context =context;
            this.patientList = patientList;
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_mobile = (TextView) itemView.findViewById(R.id.txt_mobile);
            txt_nic = (TextView) itemView.findViewById(R.id.txt_nic);
            contact_icon = (SimpleDraweeView) itemView.findViewById(R.id.icon_patient);
            contact_icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, PatientInfoActivity.class);
            intent.putExtra("patient_id", getAdapterPosition());
            context.startActivity(intent);
        }
    }
}

