package lk.ac.mrt.cse.medipal.view.doctor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.PatientInfoRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;

/**
 * Created by chand on 2017-06-13.
 */

public class PatientInformationFragment extends Fragment {

    Patient patient;


    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_information_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patient = (Patient) JsonConvertor.getElementObject(this.getArguments(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_patient_info);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(new PatientInfoRecyclerAdaptor(patient));
    }
}
