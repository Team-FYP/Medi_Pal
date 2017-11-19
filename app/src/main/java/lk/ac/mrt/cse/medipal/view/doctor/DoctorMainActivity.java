package lk.ac.mrt.cse.medipal.view.doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
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
import java.util.Collections;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.NotificationRecyclerViewAdaptor;
import lk.ac.mrt.cse.medipal.adaptor.PatientRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.constant.SharedPreferencesKeys;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Notification;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.ListWrapper;
import lk.ac.mrt.cse.medipal.model.network.NotificationResponse;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import lk.ac.mrt.cse.medipal.view.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorMainActivity extends AppCompatActivity {
    private Activity activity;
    private Context context;
    private EditText txt_search;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private RecyclerView patientRecyclerView;
    private RecyclerView.LayoutManager patientlayoutManager;
    private DraweeView imageView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView txt_doctor_name;
    private TextView txt_reg_id;
    private ImageView btn_notfication;
    private ArrayList<Patient> searchPatientList;
    private PatientRecyclerAdaptor patientRecyclerAdaptor;
    private ArrayList<Patient> patientList;
    private ArrayList<Notification> notificationList;
    private int previousLength = 0;
    private TextView text_notification_count;
    private Doctor doctor;
    private TextView no_patient_text;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view_notification;
    private RecyclerView.LayoutManager notification_layout_manager;
    private NotificationRecyclerViewAdaptor notificationRecyclerViewAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        context = this;
        activity = this;
        doctor = (Doctor) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_DOCTOR, Doctor.class);
        getElements();
        addListeners();
        setElementValues();
        loadRecyclerData();
        configureSearchText();
        retireveNotifications();
        //configureNotification();
    }

    private void getElements() {
        //main elements
        txt_search = (EditText) findViewById(R.id.txt_search);
        text_notification_count = (TextView) findViewById(R.id.text_notification_count);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //notificationBoard = (NotificationBoard) findViewById(R.id.board);
        btn_notfication = (ImageView) findViewById(R.id.btn_notfication);
        no_patient_text = (TextView) findViewById(R.id.no_patient_text);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recycler_view_notification = findViewById(R.id.recycler_view_notification);

        //recyclerview elemets
        patientRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_patient);
        patientlayoutManager = new LinearLayoutManager(this);
        notification_layout_manager = new LinearLayoutManager(this);
        searchPatientList = new ArrayList<>();

        //drawer elements
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_docotor_main);
        headerView = navigationView.inflateHeaderView(R.layout.header_drawer);
        imageView = (DraweeView) headerView.findViewById(R.id.headerimage);
        txt_doctor_name = (TextView) headerView.findViewById(R.id.txt_doctor_name);
        txt_reg_id = (TextView) headerView.findViewById(R.id.txt_reg_id);
    }

    private void addListeners() {
        txt_search.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        float density = dm.density;
                        Drawable img = context.getResources().getDrawable(
                                R.drawable.icon_search);
                        img.setBounds(0, 0, Math.round(18 * density), Math.round(18 * density));

                        txt_search.setCompoundDrawables(img, null, null, null);
                    }
                });
        btn_notfication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (notificationBoard.isOpened()){
//                    notificationBoard.close();
//                }else {
//                    notificationBoard.open(true);
//                }
                if (drawerLayout.isDrawerOpen(recycler_view_notification)) {
                    drawerLayout.closeDrawer(recycler_view_notification);
                } else {
                    drawerLayout.openDrawer(recycler_view_notification);
                }
                retireveNotifications();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecyclerData();
            }
        });

        navigationView.setItemIconTintList(null);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View drawerView) {
                if (drawerView.equals(navigationView)) {
                    supportInvalidateOptionsMenu();
                    actionBarDrawerToggle.syncState();
                }
            }

            public void onDrawerOpened(View drawerView) {
                if (drawerView.equals(navigationView)) {
                    supportInvalidateOptionsMenu();
                    actionBarDrawerToggle.syncState();
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Avoid normal indicator glyph behaviour. This is to avoid glyph movement when opening the right drawer
                //super.onDrawerSlide(drawerView, slideOffset);
            }
        };
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    private void setElementValues() {
        if (doctor.getImage() != null) {
            Uri imageUri = Uri.parse(doctor.getImage());
            imageView.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController(imageView.getController())
                            .setUri(imageUri)
                            .setTapToRetryEnabled(true)
                            .build());
        }

        txt_doctor_name.setText(doctor.getName());
        txt_reg_id.setText(doctor.getRegistration_id());
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
        patientRecyclerView.setLayoutManager(patientlayoutManager);
        recycler_view_notification.setLayoutManager(notification_layout_manager);
    }

    private void loadRecyclerData() {
        patientList = new ArrayList<>();
        searchPatientList = new ArrayList<>();
        Callback<ListWrapper<Patient>> patientListResponse = new Callback<ListWrapper<Patient>>() {
            @Override
            public void onResponse(Call<ListWrapper<Patient>> call, Response<ListWrapper<Patient>> response) {
                ListWrapper<Patient> responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getItems() != null) {
                        patientList.addAll(responseObject.getItems());
                        configureRecyclerView();
                        if (no_patient_text.getVisibility() == View.VISIBLE) {
                            no_patient_text.setVisibility(View.GONE);
                        }
                    }
                    if (patientList.size() == 0) {
                        no_patient_text.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(DoctorMainActivity.this, Common.ERROR_OCCURED_TXT + response.message(), Toast.LENGTH_LONG).show();
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ListWrapper<Patient>> call, Throwable t) {
                Toast.makeText(DoctorMainActivity.this, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
                no_patient_text.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        String registration_id = doctor.getRegistration_id();
        DoctorController doctorController = new DoctorController();
        doctorController.getPatientList(patientListResponse, registration_id);
    }

    private void configureRecyclerView() {
        searchPatientList.addAll(patientList);
        patientRecyclerAdaptor = new PatientRecyclerAdaptor(searchPatientList, context, activity, doctor);
        patientRecyclerView.setAdapter(patientRecyclerAdaptor);
        patientRecyclerView.setNestedScrollingEnabled(true);
    }

    private void configureNotificationRecyclerView() {
        notificationRecyclerViewAdaptor = new NotificationRecyclerViewAdaptor(context, notificationList);
        recycler_view_notification.setAdapter(notificationRecyclerViewAdaptor);
        recycler_view_notification.setNestedScrollingEnabled(true);
    }

    private void configureSearchText() {
        txt_search = (EditText) findViewById(R.id.txt_search);
        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charSequence = charSequence.toString().toLowerCase();
                if (charSequence.length() > 0) {
                    if (charSequence.length() > previousLength) {
                        previousLength++;
                        for (Patient patient : new ArrayList<>(searchPatientList)) {
                            if (!patient.getName().toLowerCase().contains(charSequence) && !patient.getNic().toLowerCase().contains(charSequence)) {
                                searchPatientList.remove(patient);
                            }
                        }
                    } else {
                        if (previousLength != 0)
                            previousLength--;
                        searchPatientList = new ArrayList<Patient>();
                        for (Patient patient : patientList) {
                            if (patient.getName().toLowerCase().contains(charSequence) || patient.getNic().toLowerCase().contains(charSequence)) {
                                searchPatientList.add(patient);
                            }
                        }
                    }
                } else {
                    searchPatientList = new ArrayList<Patient>();
                    searchPatientList.addAll(patientList);
                }
                patientRecyclerAdaptor = new PatientRecyclerAdaptor(searchPatientList, context, activity, doctor);
                patientRecyclerView.setAdapter(patientRecyclerAdaptor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

//    private void configureNotification() {
//        NotificationGlobal global = NotificationDelegater.getInstance().global();
//        global.setViewEnabled(true);
//        global.setBoardEnabled(true);
//        global.enableEffect(true);
//        notificationHandler = new Handler();
////        patientList.get(notificationCount).getImage()
//        notificationSender = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (notificationCount < patientList.size()){
//                        NotificationBuilder.V1 builder = NotificationBuilder.global()
//                                .setIconDrawable(VectorDrawableUtil.getDrawablefromString(patientList.get(notificationCount).getImage(), context))
//                                .setTitle(patientList.get(notificationCount).getName())
//                                .setText("Shared his medical history with you.");
//
//                        NotificationDelegater delegater = NotificationDelegater.getInstance();
//                        delegater.send(builder.getNotification());
//                        notificationCount++;
//                    } else {
//                        notificationHandler.removeCallbacks(notificationSender);
//                    }
//                } finally {
//                    notificationHandler.postDelayed(notificationSender, 7000);
//                }
//            }
//        };
//        notificationSender.run();
//    }
//    private void sendNotification(final Notification notification) {
//        notificationHandler = new Handler();
//        notificationHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                NotificationBuilder.V1 builder = NotificationBuilder.global()
//                        .setIconDrawable(VectorDrawableUtil.getDrawablefromString(notification.getPatient().getImage(), context))
//                        .setTitle(notification.getPatient().getName())
//                        .setText(notification.getMessage());
//
//                NotificationDelegater delegater = NotificationDelegater.getInstance();
//                delegater.send(builder.getNotification());
//            }
//        }, 7000);
//    }

    public void jump_to_account(MenuItem menuItem) {

    }

    public void go_to_settings(MenuItem menuItem) {

    }

    public void logout(MenuItem menuItem) {
        SharedPreferences preferencs = context.getSharedPreferences(
                SharedPreferencesKeys.MEDIPAL_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencs.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(DoctorMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration con) {
        super.onConfigurationChanged(con);
        actionBarDrawerToggle.onConfigurationChanged(con);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        for (int i = 0; i < menu.size(); i++)
            menu.getItem(i).setVisible(!drawerLayout.isDrawerOpen(navigationView));
        return super.onPrepareOptionsMenu(menu);
    }

    private void retireveNotifications() {
        notificationList = new ArrayList<>();
        final Callback<NotificationResponse> notificationResponseListResponse = new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                NotificationResponse responseObject = response.body();
                if (response.isSuccessful()) {
                    if (responseObject.getShare_notifications() != null) {
                        notificationList.addAll(responseObject.getShare_notifications());
                    }
                    if (responseObject.getAllergy_notifications() != null) {
                        notificationList.addAll(responseObject.getAllergy_notifications());
                    }
                    if (responseObject.getPrescription_notifications() != null) {
                        notificationList.addAll(responseObject.getPrescription_notifications());
                    }
                    Collections.sort(notificationList);
                    configureNotificationRecyclerView();
                } else {
                    Toast.makeText(DoctorMainActivity.this, Common.ERROR_OCCURED_TXT + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Toast.makeText(DoctorMainActivity.this, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
            }
        };
        String registration_id = doctor.getRegistration_id();
        DoctorController doctorController = new DoctorController();
        doctorController.getNotificationResponseList(notificationResponseListResponse, registration_id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                actionBarDrawerToggle.onOptionsItemSelected(item);

                if (drawerLayout.isDrawerOpen(recycler_view_notification))
                    drawerLayout.closeDrawer(recycler_view_notification);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
