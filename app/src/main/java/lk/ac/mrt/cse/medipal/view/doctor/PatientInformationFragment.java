package lk.ac.mrt.cse.medipal.view.doctor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.controller.PrescriptionDrugController;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.PrescriptionDrug;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chand on 2017-06-13.
 */

public class PatientInformationFragment extends Fragment {

    private Patient patient;
    private ArrayList<PrescriptionDrug> currentDrugs = new ArrayList<>();
    private Context context;
    private ProgressBar progressBar;
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_information_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        patient = retrievePatient();
        getElements(view);
        setElementValues();
        retrieveCurrentMedicines();
        refreshRecyclerView();
    }

    private Patient retrievePatient(){
        return (Patient) JsonConvertor.getElementObject(this.getArguments(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
    }

    private void getElements(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_patient_info);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void setElementValues(){
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        progressBar.setVisibility(View.GONE);
    }

    private void retrieveCurrentMedicines(){
        progressBar.setVisibility(View.VISIBLE);

        Callback<ListWrapper<PrescriptionDrug>> currDrugListResponse = new Callback<ListWrapper<PrescriptionDrug>>() {
            @Override
            public void onResponse(Call<ListWrapper<PrescriptionDrug>> call, Response<ListWrapper<PrescriptionDrug>> response) {
                ListWrapper<PrescriptionDrug> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getItems() != null) {
                        currentDrugs = new ArrayList<>();
                        currentDrugs.addAll(responseObject.getItems());
                        refreshRecyclerView();
                    }
                } else {
                    Toast.makeText(getActivity(), Common.ERROR_OCCURED_TXT+response.message(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ListWrapper<PrescriptionDrug>> call, Throwable t) {
                Toast.makeText(getActivity(), Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        };
        String patient_nic  = patient.getNic();
        PrescriptionDrugController prescriptionDrugController = new PrescriptionDrugController();
        prescriptionDrugController.getPatientCurrentMedList(currDrugListResponse, patient_nic);
    }

    private void refreshRecyclerView(){
        mRecyclerView.setAdapter(new PatientInfoRecyclerAdaptor(context, patient, currentDrugs));
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveCurrentMedicines();
    }
}
