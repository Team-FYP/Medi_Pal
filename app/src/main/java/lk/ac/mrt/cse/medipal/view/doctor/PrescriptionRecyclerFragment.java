package lk.ac.mrt.cse.medipal.view.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.gson.Gson;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.PatientInfoRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.adaptor.PrescriptionRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.controller.PrescriptionController;
import lk.ac.mrt.cse.medipal.controller.PrescriptionDrugController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chand on 2017-06-13.
 */

public class PrescriptionRecyclerFragment extends Fragment {
    private static final boolean GRID_LAYOUT = false;
    private ArrayList<Prescription> prescriptionList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private Patient patient;
    private Doctor doctor;
    private Context context;
    private FloatingActionButton btn_fab;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prescription_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        patient = retrievePatient();
        doctor = retrieveDoctor();
        getElements(view);
        setElementValues();
        addListeners();
        retrievePrescriptions();
        refreshRecyclerView();
    }

    private Patient retrievePatient(){
        return (Patient) JsonConvertor.getElementObject(this.getArguments(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
    }

    private Doctor retrieveDoctor(){
        return (Doctor) JsonConvertor.getElementObject(this.getArguments(), ObjectType.OBJECT_TYPE_DOCTOR, Doctor.class);
    }

    private void getElements(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_prescription);
        progressBar = view.findViewById(R.id.progress_bar);
        btn_fab = view.findViewById(R.id.btn_fab);
    }

    private void addListeners() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 && btn_fab.isShown())
                    btn_fab.hide();
                else if (dy<0 && !btn_fab.isShown())
                    btn_fab.show();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    btn_fab.show();
//                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrescribingActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(patient);
                intent.putExtra(ObjectType.OBJECT_TYPE_PATIENT, json);
                json = gson.toJson(doctor);
                intent.putExtra(ObjectType.OBJECT_TYPE_DOCTOR, json);
                context.startActivity(intent);
            }
        });
    }

    private void setElementValues() {
        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        progressBar.setVisibility(View.GONE);
    }
    private void retrievePrescriptions(){
        progressBar.setVisibility(View.VISIBLE);

        Callback<ListWrapper<Prescription>> prescriptionListResponse = new Callback<ListWrapper<Prescription>>() {
            @Override
            public void onResponse(Call<ListWrapper<Prescription>> call, Response<ListWrapper<Prescription>> response) {
                ListWrapper<Prescription> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getItems() != null) {
                        prescriptionList = new ArrayList<>();
                        prescriptionList.addAll(responseObject.getItems());
                        refreshRecyclerView();
                    }
                } else {
                    Toast.makeText(getActivity(), Common.ERROR_OCCURED_TXT+response.message(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ListWrapper<Prescription>> call, Throwable t) {
                Toast.makeText(getActivity(), Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };
        String patient_nic  = patient.getNic();
        PrescriptionController prescriptionController = new PrescriptionController();
        prescriptionController.getPatientPrescriptionList(prescriptionListResponse, patient_nic);
    }
    private void refreshRecyclerView(){
        mRecyclerView.setAdapter(new PrescriptionRecyclerAdaptor(prescriptionList,getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        retrievePrescriptions();
    }
}
