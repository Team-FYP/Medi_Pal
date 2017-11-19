package lk.ac.mrt.cse.medipal.view.patient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.DraweeView;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.constant.SharedPreferencesKeys;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.service.PatientNotificationService;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import lk.ac.mrt.cse.medipal.util.VectorDrawableUtil;
import lk.ac.mrt.cse.medipal.view.LoginActivity;
import lk.ac.mrt.cse.medipal.view.doctor.DoctorMainActivity;
import lk.ac.mrt.cse.medipal.view.doctor.PatientInformationFragment;
import lk.ac.mrt.cse.medipal.view.doctor.PrescriptionRecyclerFragment;
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
    private Handler notificationHandler;
    private NotificationBoard notificationBoard;
    private Runnable notificationSender;
    private TextView text_notification_count;
    private Patient patient;

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
        //startService(new Intent(this, PatientNotificationService.class));
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
        //toolbar = mViewPager.getToolbar();
    }

    private void addListeners() {
        btn_notfication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationBoard.isOpened()) {
                    notificationBoard.close();
                } else {
                    notificationBoard.open(true);
                }
            }
        });

        navigationView.setItemIconTintList(null);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
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
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            //actionBar.setDisplayUseLogoEnabled(false);
        }
        mViewPager.setToolbar(toolbar);
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

}
