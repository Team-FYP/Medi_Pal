package lk.ac.mrt.cse.medipal.view.patient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.PatientOwnPrescriptionRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.adaptor.PrescriptionRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.model.Prescription;


public class PatientMyPrescriptionsFragment extends Fragment {
    private static final boolean GRID_LAYOUT = false;
    private ArrayList<Prescription> prescriptionList;
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_patient_my_prescriptions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prescriptionList = new ArrayList<>();
        for (int i = 0; i < 100; i++ ){
            prescriptionList.add(new Prescription());
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.patient_view_own_prescription);

        //setup materialviewpager

        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(new PatientOwnPrescriptionRecyclerAdaptor(prescriptionList,getContext()));
    }
}
