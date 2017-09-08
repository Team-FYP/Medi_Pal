package lk.ac.mrt.cse.medipal.view.patient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.Prescription;
import lk.ac.mrt.cse.medipal.view.doctor.DoctorMainActivity;
import lk.ac.mrt.cse.medipal.view.doctor.PatientInformationFragment;
import lk.ac.mrt.cse.medipal.view.doctor.PrescriptionRecyclerFragment;

import static lk.ac.mrt.cse.medipal.view.doctor.DoctorMainActivity.patientList;

public class PatientMainActivity extends AppCompatActivity {

    private static final int TAB_COUNT = 2;
    public static ArrayList<Patient> patientList;
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ArrayList<Prescription> prescriptionList;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        loadRecyclerData();
        patient = patientList.get(0);
        configureMaterialViewPager();
        configureToolbar();
    }

    private void configureViewPager() {

        viewPager = mViewPager.getViewPager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % TAB_COUNT) {
                    case 0:
                        PatientSelfInformationFragment patientSelfInformationFragment = new PatientSelfInformationFragment();
                        Bundle infoBundle = new Bundle();
                        infoBundle.putInt("patient_id", 0);
                        patientSelfInformationFragment.setArguments(infoBundle);
                        return patientSelfInformationFragment;
                    case 1:
                        //    return RecyclerViewFragment.newInstance();
                        //case 2:
                        //    return WebViewFragment.newInstance();
                    default:
                        PatientMyPrescriptionsFragment patientMyPrescriptionsFragment = new PatientMyPrescriptionsFragment();
                        Bundle dataBundle = new Bundle();
                        dataBundle.putInt("patient_id", 0);
                        patientMyPrescriptionsFragment.setArguments(dataBundle);
                        return patientMyPrescriptionsFragment;
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
                        return "Information";
                    case 1:
                        return "Prescription";
                }
                return "";
            }
        });

        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(viewPager);
    }

    private void configureToolbar() {
        toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void configureMaterialViewPager() {
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.red,
//                                "http://api.androidhive.info/images/nav-menu-header-bg.jpg");
                        return HeaderDesign.fromColorResAndDrawable(R.color.blue, getResources().getDrawable( patient.getImage(), getTheme() ));
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://demo.geekslabs.com/materialize/v2.1/layout01/images/user-profile-bg.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
        configureViewPager();
    }

    private void loadRecyclerData(){
        patientList = new ArrayList<>();

        Patient yasiru = new Patient("922134234v","Yasiru Nilan","Male","shalith@gmail.com","1992-04-22","0712345784","0413254785");

        yasiru.setImage(R.drawable.profile_yasiru);

        patientList.add(yasiru);
    }
}
