package lk.ac.mrt.cse.medipal.view.doctor;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
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
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.adaptor.PatientRecyclerAdaptor;
import lk.ac.mrt.cse.medipal.model.Notification;
import lk.ac.mrt.cse.medipal.model.Patient;
import zemin.notification.NotificationBoard;
import zemin.notification.NotificationBuilder;
import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationGlobal;

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
    private CircleImageView imageView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView txt_doctor_name;
    private TextView txt_reg_id;
    private ImageView btn_notfication;
    private ArrayList<Patient> searchPatientList;
    private PatientRecyclerAdaptor patientRecyclerAdaptor;
    public static ArrayList<Patient> patientList;
    private int previousLength = 0;
    int notificationCount = 0;
    private Handler notificationHandler;
    private NotificationBoard notificationBoard;
    Runnable notificationSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationDelegater.initialize(this, NotificationDelegater.GLOBAL);
        setContentView(R.layout.activity_doctor_main);
        context = this;
        activity = this;
        configureUI();
        configureDrawer();
        configureRecyclerView();
        configureSearchText();
        configureNotifictation();
    }

    private void configureNotifictation() {
        NotificationGlobal global = NotificationDelegater.getInstance().global();
        global.setViewEnabled(true);
        global.setBoardEnabled(true);
        notificationHandler = new Handler();
        notificationSender = new Runnable() {
            @Override
            public void run() {
                try {
                    if (notificationCount < patientList.size()){
                        NotificationBuilder.V1 builder = NotificationBuilder.global()
                                .setIconDrawable(ResourcesCompat.getDrawable(getResources(), patientList.get(notificationCount).getImage(), null))
                                .setTitle(patientList.get(notificationCount).getName())
                                .setText("Shared his medical history with you.");

                        NotificationDelegater delegater = NotificationDelegater.getInstance();
                        delegater.send(builder.getNotification());
                        notificationCount++;
                    } else {
                        notificationHandler.removeCallbacks(notificationSender);
                    }
                } finally {
                    notificationHandler.postDelayed(notificationSender, 7000);
                }
            }
        };
        notificationSender.run();
    }

    private void sendNotification(final Notification notification) {
        notificationHandler = new Handler();
        notificationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationBuilder.V1 builder = NotificationBuilder.global()
                        .setIconDrawable(ResourcesCompat.getDrawable(getResources(), notification.getPatient().getImage(), null))
                        .setTitle(notification.getPatient().getName())
                        .setText(notification.getMessage());

                NotificationDelegater delegater = NotificationDelegater.getInstance();
                delegater.send(builder.getNotification());
            }
        }, 7000);
    }

    private void configureUI(){
        txt_search = (EditText) findViewById(R.id.txt_search);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        notificationBoard = (NotificationBoard) findViewById(R.id.board);
        btn_notfication = (ImageView) findViewById(R.id.btn_notfication);
        btn_notfication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationBoard.isOpened()){
                    notificationBoard.close();
                }else {
                    notificationBoard.open(true);
                }
            }
        });
    }

    public void configureDrawer(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_docotor_main);
        headerView = navigationView.inflateHeaderView(R.layout.header_drawer);
        imageView = (CircleImageView) headerView.findViewById(R.id.headerimage);
        txt_doctor_name = (TextView) headerView.findViewById(R.id.txt_doctor_name);
        txt_reg_id = (TextView) headerView.findViewById(R.id.txt_reg_id);

        navigationView.setItemIconTintList(null);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        imageView.setImageResource(R.drawable.profile_darika);
        txt_doctor_name.setText("Darika Sellahewa");
        txt_reg_id.setText("rg123/456");

    }

    public void jump_to_account(MenuItem menuItem){

    }
    public void go_to_settings(MenuItem menuItem){

    }
    public void logout(MenuItem menuItem){

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void configureRecyclerView(){
        loadRecyclerData();
        patientRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_patient);
        patientlayoutManager = new LinearLayoutManager(this);
        patientRecyclerView.setLayoutManager(patientlayoutManager);
        searchPatientList = new ArrayList<>();
        searchPatientList.addAll(patientList);
        patientRecyclerAdaptor = new PatientRecyclerAdaptor(searchPatientList,context, activity);
        patientRecyclerView.setAdapter(patientRecyclerAdaptor);
        patientRecyclerView.setNestedScrollingEnabled(true);
    }

    private void loadRecyclerData(){
        patientList = new ArrayList<>();

        Patient lakshan = new Patient("933030270v","Lakshan Gamage","Male","lakshan@gmail.com","1993-10-29","0773472649","0914931530");
        Patient shalith = new Patient("923245232v","Shalith Fernando","Male","shalith@gmail.com","1992-05-28","0775543456","0114569857");
        Patient yasiru = new Patient("922134234v","Yasiru Nilan","Male","shalith@gmail.com","1992-04-22","0712345784","0413254785");
        Patient darika = new Patient("922124544v","Darika Sellahewa","Female","darika@gmail.com","1992-03-22","0715698748","0412254765");
        Patient dineth = new Patient("934578451v","Dineth Egodage","Male","dinneth@gmail.com","1993-08-28","0719875485","0412254765");
        Patient heshan = new Patient("924578457v","Heshan Fernando","Male","hfernando@gmail.com","1992-08-18","0712542015","0112254765");
        Patient manesh = new Patient("934578451v","Manesh Jayawardene","Male","maneshj@gmail.com","1993-05-18","0715553345","0612254765");

        lakshan.setImage(R.drawable.profile_lakshan);
        shalith.setImage(R.drawable.profile_shalith);
        yasiru.setImage(R.drawable.profile_yasiru);
        darika.setImage(R.drawable.profile_darika);
        dineth.setImage(R.drawable.profile_dineth);
        heshan.setImage(R.drawable.profile_heshan);
        manesh.setImage(R.drawable.profile_manesh);

        patientList.add(lakshan);
        patientList.add(shalith);
        patientList.add(yasiru);
        patientList.add(darika);
        patientList.add(dineth);
        patientList.add(heshan);
        patientList.add(manesh);
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
                        for (Patient patient: new ArrayList<>(searchPatientList)){
                            if (!patient.getName().toLowerCase().contains(charSequence) && !patient.getNic().toLowerCase().contains(charSequence)){
                                searchPatientList.remove(patient);
                            }
                        }
                    }else {
                        if (previousLength!=0)
                            previousLength--;
                        searchPatientList = new ArrayList<Patient>();
                        for (Patient patient: patientList){
                            if (patient.getName().toLowerCase().contains(charSequence) || patient.getNic().toLowerCase().contains(charSequence) ){
                                searchPatientList.add(patient);
                            }
                        }
                    }

                }else {
                    searchPatientList = new ArrayList<Patient>();
                    searchPatientList.addAll(patientList);
                }
                patientRecyclerAdaptor = new PatientRecyclerAdaptor(searchPatientList,context, activity);
                patientRecyclerView.setAdapter(patientRecyclerAdaptor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
