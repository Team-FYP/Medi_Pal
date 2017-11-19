package lk.ac.mrt.cse.medipal.view.patient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.DraweeView;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.DoctorRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.adaptor.PatientRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.controller.PatientController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import lk.ac.mrt.cse.medipal.view.doctor.DoctorMainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zemin.notification.NotificationBoard;

public class DoctorViewActivity extends AppCompatActivity {
    private Context context;
    private EditText txt_search;
    private Toolbar toolbar;
    private RecyclerView doctorRecyclerView;
    private RecyclerView.LayoutManager doctorlayoutManager;
    private DoctorRecyclerAdaptor doctorRecyclerAdaptor;
    private ArrayList<Doctor> searchDoctorList;
    private ArrayList<Doctor> doctorList;
    private int previousLength = 0;
    private Patient patient;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view);
        context = this;
        patient = (Patient) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
        getElements();
        addListeners();
        setElementValues();
        loadRecyclerData();
        configureSearchText();
    }

    private void getElements(){
        //main elements
        txt_search = (EditText) findViewById(R.id.txt_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        //recyclerview elemets
        doctorRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_doctor);
        doctorlayoutManager = new LinearLayoutManager(this);
        searchDoctorList = new ArrayList<>();
    }

    private void addListeners(){
        txt_search.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        float density = dm.density;
                        Drawable img = context.getResources().getDrawable(
                                R.drawable.icon_search);
                        img.setBounds(0, 0, Math.round(18*density), Math.round(18*density));

                        txt_search.setCompoundDrawables(img, null, null, null);
                    }
                });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecyclerData();
            }
        });
    }

    private void setElementValues(){
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        //recyclerview elements
        doctorRecyclerView.setLayoutManager(doctorlayoutManager);
    }

    private void loadRecyclerData(){
        doctorList = new ArrayList<>();
        searchDoctorList = new ArrayList<>();
        Callback<ListWrapper<Doctor>> doctorListResponse = new Callback<ListWrapper<Doctor>>() {
            @Override
            public void onResponse(Call<ListWrapper<Doctor>> call, Response<ListWrapper<Doctor>> response) {
                ListWrapper<Doctor> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getItems() != null) {
                        doctorList.addAll(responseObject.getItems());
                        configureRecyclerView();
                    }
                } else {
                    Toast.makeText(DoctorViewActivity.this, Common.ERROR_OCCURED_TXT+response.message(), Toast.LENGTH_LONG).show();
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<ListWrapper<Doctor>> call, Throwable t) {
                Toast.makeText(DoctorViewActivity.this, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        String nic  = patient.getNic();
        PatientController patientController = new PatientController();
        patientController.getDoctorList(doctorListResponse, nic);
    }
    private void configureRecyclerView(){
        searchDoctorList.addAll(doctorList);
        doctorRecyclerAdaptor = new DoctorRecyclerAdaptor(searchDoctorList,context, patient);
        doctorRecyclerView.setAdapter(doctorRecyclerAdaptor);
        doctorRecyclerView.setNestedScrollingEnabled(true);
    }

    private void configureSearchText(){
        txt_search = (EditText) findViewById(R.id.txt_search);
        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charSequence = charSequence.toString().toLowerCase();
                if (charSequence.length() > 0){
                    if (charSequence.length() > previousLength){
                        previousLength++;
                        for (Doctor doctor: new ArrayList<>(searchDoctorList)){
                            if (!doctor.getName().toLowerCase().contains(charSequence) && !doctor.getRegistration_id().toLowerCase().contains(charSequence)){
                                searchDoctorList.remove(doctor);
                            }
                        }
                    }else {
                        if (previousLength!=0)
                            previousLength--;
                        searchDoctorList = new ArrayList<Doctor>();
                        for (Doctor doctor: doctorList){
                            if (patient.getName().toLowerCase().contains(charSequence) || patient.getNic().toLowerCase().contains(charSequence) ){
                                searchDoctorList.add(doctor);
                            }
                        }
                    }
                }else {
                    searchDoctorList = new ArrayList<Doctor>();
                    searchDoctorList.addAll(doctorList);
                }
                doctorRecyclerAdaptor = new DoctorRecyclerAdaptor(searchDoctorList,context, patient);
                doctorRecyclerView.setAdapter(doctorRecyclerAdaptor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
