package lk.ac.mrt.cse.medipal.view.doctor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.PatientInfoRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.adaptor.PrescriptionRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.controller.PrescriptionController;
import lk.ac.mrt.cse.medipal.controller.PrescriptionDrugController;
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
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prescription_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        patient = retrievePatient();
        getElements(view);
        setElementValues();
        retrievePrescriptions();
        refreshRecyclerView();
    }

    private Patient retrievePatient(){
        return (Patient) JsonConvertor.getElementObject(this.getArguments(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
    }

    private void getElements(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_prescription);
        progressBar = view.findViewById(R.id.progress_bar);
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
                    Toast.makeText(getActivity(), "Error Occured: "+response.message(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ListWrapper<Prescription>> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Failure. Check your connection", Toast.LENGTH_LONG).show();
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
}
