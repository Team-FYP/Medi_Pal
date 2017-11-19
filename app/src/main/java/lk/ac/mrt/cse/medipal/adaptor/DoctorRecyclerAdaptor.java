package lk.ac.mrt.cse.medipal.adaptor;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import java.util.ArrayList;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.controller.PatientController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.DataWriteResponse;
import lk.ac.mrt.cse.medipal.model.network.ShareRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lakshan on 11/18/17.
 */

public class DoctorRecyclerAdaptor extends RecyclerView.Adapter<DoctorRecyclerAdaptor.DoctorRecyclerViewHolder> implements View.OnClickListener{
    private ArrayList<Doctor> doctorList = null;
    private Context context = null;
    private Patient patient;

    public DoctorRecyclerAdaptor(ArrayList<Doctor> doctorList, Context context, Patient patient) {
        this.context = context;
        this.doctorList = doctorList;
        this.patient = patient;
    }

    @Override
    public DoctorRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_row_layout,parent,false);
        return new DoctorRecyclerViewHolder(view, context, doctorList);
    }
    @Override
    public void onBindViewHolder(DoctorRecyclerViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.txt_name.setText(doctor.getName());
        holder.txt_nic.setText(doctor.getRegistration_id());
        holder.txt_mobile.setText(doctor.getMobile());
        if (doctor.getImage() != null) {
            Uri imageUri = Uri.parse(doctor.getImage());
            holder.contact_icon.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController(holder.contact_icon.getController())
                            .setUri(imageUri)
                            .build());
        }
        if (doctor.isHistory_shared()){
            holder.btn_share.setVisibility(View.GONE);
            holder.btn_already_share.setVisibility(View.VISIBLE);
        } else {
            holder.btn_share.setVisibility(View.VISIBLE);
            holder.btn_already_share.setVisibility(View.GONE);
        }
        holder.progress_bar_share.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return doctorList.size();
    }
    @Override
    public void onClick(View view) {

    }

    public class DoctorRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txt_name;
        private TextView txt_mobile;
        private TextView txt_nic;
        private SimpleDraweeView contact_icon;
        private ArrayList<Doctor> patientList = null;
        private Context context = null;
        private MaterialFancyButton btn_share;
        private MaterialFancyButton btn_already_share;
        private ProgressBar progress_bar_share;

        public DoctorRecyclerViewHolder(View itemView, Context context, ArrayList<Doctor> patientList) {
            super(itemView);
            this.context =context;
            this.patientList = patientList;
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_mobile = (TextView) itemView.findViewById(R.id.txt_mobile);
            txt_nic = (TextView) itemView.findViewById(R.id.txt_nic);
            btn_share =  itemView.findViewById(R.id.btn_share);
            btn_already_share =  itemView.findViewById(R.id.btn_already_share);
            progress_bar_share =  itemView.findViewById(R.id.progress_bar_share);
            contact_icon = (SimpleDraweeView) itemView.findViewById(R.id.icon_patient);
            btn_share.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_share){
                shareDetails();
            }
        }


        private void shareDetails() {
            btn_share.setVisibility(View.GONE);
            progress_bar_share.setVisibility(View.VISIBLE);

            Callback<DataWriteResponse> shareResponse = new Callback<DataWriteResponse>() {
                @Override
                public void onResponse(Call<DataWriteResponse> call, Response<DataWriteResponse> response) {
                    DataWriteResponse responseObject = response.body();
                    if (response.isSuccessful()) {
                        if (responseObject.isSuccess()) {

                        }else {
                            progress_bar_share.setVisibility(View.GONE);
                            Toast.makeText(context, "Error saving prescription. Tey Again.", Toast.LENGTH_LONG).show();
                            btn_share.setVisibility(View.VISIBLE);
                        }
                    }else {
                        progress_bar_share.setVisibility(View.GONE);
                        Toast.makeText(context, "Error saving prescription. Tey Again.", Toast.LENGTH_LONG).show();
                        btn_share.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<DataWriteResponse> call, Throwable t) {
                    progress_bar_share.setVisibility(View.GONE);
                    Toast.makeText(context, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                    btn_already_share.setVisibility(View.VISIBLE);
                }
            };
            ShareRequest shareRequest = new ShareRequest(patient.getNic(), doctorList.get(getAdapterPosition()).getRegistration_id(), false);
            PatientController patientController = new PatientController();
            patientController.shareDetails(shareResponse, shareRequest);
        }
    }

}
