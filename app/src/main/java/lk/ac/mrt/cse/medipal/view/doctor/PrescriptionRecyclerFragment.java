package lk.ac.mrt.cse.medipal.view.doctor;

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

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.PrescriptionRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.model.Prescription;

/**
 * Created by chand on 2017-06-13.
 */

public class PrescriptionRecyclerFragment extends Fragment {
    private static final boolean GRID_LAYOUT = false;
    private ArrayList<Prescription> prescriptionList;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_prescription_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prescriptionList = new ArrayList<>();
        for (int i = 0; i < 100; i++ ){
            prescriptionList.add(new Prescription());
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_prescription);
        progressBar = view.findViewById(R.id.progress_bar);
        //setup materialviewpager

        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(new PrescriptionRecyclerAdaptor(prescriptionList,getContext()));
    }
}
