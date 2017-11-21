package lk.ac.mrt.cse.medipal.view.patient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.DraweeView;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.Collections;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.NotificationRecyclerViewAdaptor;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.constant.SharedPreferencesKeys;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.model.Notification;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.NotificationResponse;
import lk.ac.mrt.cse.medipal.service.PatientNotificationService;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import lk.ac.mrt.cse.medipal.util.VectorDrawableUtil;
import lk.ac.mrt.cse.medipal.view.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zemin.notification.NotificationBoard;

public class PatientMainActivity extends AppCompatActivity {
    private Activity activity;
    private Context context;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private DraweeView imageView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView txt_doctor_name;
    private TextView txt_reg_id;
    private ImageView btn_notfication;
    private NotificationBoard notificationBoard;
    private ArrayList<Notification> notificationList;
    private TextView text_notification_count;
    private Patient patient;
    private RecyclerView recycler_view_notification;
    private RecyclerView.LayoutManager notification_layout_manager;
    private NotificationRecyclerViewAdaptor notificationRecyclerViewAdaptor;

    private static final int TAB_COUNT = 2;
    private MaterialViewPager mViewPager;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        context = this;
        activity = this;
        patient = (Patient) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
        getElements();
        setElementValues();
        addListeners();
        startService(new Intent(this, PatientNotificationService.class));
    }

    private void getElements() {
        //main elements
        text_notification_count = (TextView) findViewById(R.id.text_notification_count);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        notificationBoard = (NotificationBoard) findViewById(R.id.board);
        btn_notfication = (ImageView) findViewById(R.id.btn_notfication);
        //drawer elements
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_docotor_main);
        headerView = navigationView.inflateHeaderView(R.layout.header_drawer);
        imageView = (DraweeView) headerView.findViewById(R.id.headerimage);
        txt_doctor_name = (TextView) headerView.findViewById(R.id.txt_doctor_name);
        txt_reg_id = (TextView) headerView.findViewById(R.id.txt_reg_id);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        viewPager = mViewPager.getViewPager();

        recycler_view_notification = findViewById(R.id.recycler_view_notification);
        notification_layout_manager = new LinearLayoutManager(this);
        //toolbar = mViewPager.getToolbar();
    }

    private void addListeners() {
        btn_notfication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(recycler_view_notification)) {
                    drawerLayout.closeDrawer(recycler_view_notification);
                } else {
                    drawerLayout.openDrawer(recycler_view_notification);
                }
                retireveNotifications();
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
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    private FragmentStatePagerAdapter getFragmentAdapter() {
        return new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % TAB_COUNT) {
                    case 0:
                        PrescriptionSelfRecyclerFragment prescriptionRecyclerFragment = new PrescriptionSelfRecyclerFragment();
                        Bundle dataBundle = new Bundle();
                        dataBundle.putString(ObjectType.OBJECT_TYPE_PATIENT, getIntent().getStringExtra(ObjectType.OBJECT_TYPE_PATIENT));
                        dataBundle.putString(ObjectType.OBJECT_TYPE_DOCTOR, getIntent().getStringExtra(ObjectType.OBJECT_TYPE_DOCTOR));
                        prescriptionRecyclerFragment.setArguments(dataBundle);
                        return prescriptionRecyclerFragment;
                    default:
                        PatientSelfInformationFragment patientInformationFragment = new PatientSelfInformationFragment();
                        Bundle infoBundle = new Bundle();
                        infoBundle.putString(ObjectType.OBJECT_TYPE_PATIENT, getIntent().getStringExtra(ObjectType.OBJECT_TYPE_PATIENT));
                        patientInformationFragment.setArguments(infoBundle);
                        return patientInformationFragment;
                }
            }

            @Override
            public int getCount() {
                return TAB_COUNT;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % TAB_COUNT) {
                    case 0:
                        return Common.TAB_INFORMATION;
                    case 1:
                        return Common.TAB_PRESCRITIONS;
                }
                return "";
            }
        };
    }

    private void setElementValues() {
        if (patient.getImage() != null) {
            Uri imageUri = Uri.parse(patient.getImage());
            imageView.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController(imageView.getController())
                            .setUri(imageUri)
                            .setTapToRetryEnabled(true)
                            .build());
        }

        txt_doctor_name.setText(patient.getName());
        txt_reg_id.setText(patient.getNic());

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
//                switch (page) {
//                    case 0:
//                        return HeaderDesign.fromColorResAndUrl(R.color.blue, patient.getImage());
//                    case 1:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.blue,
//                                "http://demo.geekslabs.com/materialize/v2.1/layout01/images/user-profile-bg.jpg");
//                }
                //execute others actions if needed (ex : modify your header logo)
//                if (!patient.getImage().equals(Common.URL.ICON_USER_MALE) && !patient.getImage().equals(Common.URL.ICON_USER_FEMALE)) {
//                    return HeaderDesign.fromColorResAndUrl(R.color.white, patient.getImage());
//                }
                return HeaderDesign.fromColorResAndDrawable(R.color.white, VectorDrawableUtil.getDrawable(context, R.drawable.patient_info_background));
            }
        });
        viewPager.setAdapter(getFragmentAdapter());
        viewPager.setOffscreenPageLimit(0);
        mViewPager.getPagerTitleStrip().setViewPager(viewPager);
        //mViewPager.getPagerTitleStrip().setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.whiteTransparent, getTheme()));
        mViewPager.getPagerTitleStrip().setTextColorStateListResource(R.color.white);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            //actionBar.setDisplayUseLogoEnabled(false);
        }
        mViewPager.setToolbar(toolbar);
        recycler_view_notification.setLayoutManager(notification_layout_manager);
    }

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
        Intent intent = new Intent(PatientMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void configureNotificationRecyclerView() {
        notificationRecyclerViewAdaptor = new NotificationRecyclerViewAdaptor(context, notificationList);
        recycler_view_notification.setAdapter(notificationRecyclerViewAdaptor);
        recycler_view_notification.setNestedScrollingEnabled(true);
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
                    int newCount = 0;
                    for (Notification notification : notificationList){
                        if (!notification.isStatus().equals(Common.NotificationStatus.SEEN)){
                            newCount++;
                        }
                    }
                    text_notification_count.setVisibility(View.VISIBLE);
                    text_notification_count.setText(String.valueOf(newCount));
                    if (newCount == 0){
                        text_notification_count.setVisibility(View.GONE);
                    }
                    configureNotificationRecyclerView();
                } else {
                    Toast.makeText(PatientMainActivity.this, Common.ERROR_OCCURED_TXT + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Toast.makeText(PatientMainActivity.this, Common.ERROR_NETWORK, Toast.LENGTH_LONG).show();
            }
        };
        String registration_id = patient.getNic();
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
