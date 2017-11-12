package lk.ac.mrt.cse.medipal.view.doctor;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.ObjectType;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import lk.ac.mrt.cse.medipal.util.VectorDrawableUtil;

public class PatientInfoActivity extends AppCompatActivity {
    private static final int TAB_COUNT = 2;
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private Patient patient;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        context = this;
        patient = (Patient) JsonConvertor.getElementObject(getIntent(), ObjectType.OBJECT_TYPE_PATIENT, Patient.class);
        getElements();
        addListeners();
        setElementValues();
    }
    private void getElements(){
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        viewPager = mViewPager.getViewPager();
        toolbar = mViewPager.getToolbar();
    }
    private void addListeners() {
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
                if (patient.getImage() != null) {
                    return HeaderDesign.fromColorResAndUrl(R.color.white, patient.getImage());
                }
                return HeaderDesign.fromColorResAndDrawable(R.color.white, VectorDrawableUtil.getDrawable(context, R.drawable.patient_info_background));
            }
        });
        viewPager.setAdapter(getFragmentAdapter());
    }
    private FragmentStatePagerAdapter getFragmentAdapter(){
        return new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % TAB_COUNT) {
                    case 0:
                        PatientInformationFragment patientInformationFragment = new PatientInformationFragment();
                        Bundle infoBundle = new Bundle();
                        infoBundle.putString(ObjectType.OBJECT_TYPE_PATIENT,getIntent().getStringExtra(ObjectType.OBJECT_TYPE_PATIENT));
                        patientInformationFragment.setArguments(infoBundle);
                        return patientInformationFragment;
                    default:
                        PrescriptionRecyclerFragment prescriptionRecyclerFragment = new PrescriptionRecyclerFragment();
                        Bundle dataBundle = new Bundle();
                        dataBundle.putString(ObjectType.OBJECT_TYPE_PATIENT,getIntent().getStringExtra(ObjectType.OBJECT_TYPE_PATIENT));
                        prescriptionRecyclerFragment.setArguments(dataBundle);
                        return prescriptionRecyclerFragment;
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
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(viewPager);
        //mViewPager.getPagerTitleStrip().setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.whiteTransparent, getTheme()));
        mViewPager.getPagerTitleStrip().setTextColorStateListResource(R.color.white);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
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
