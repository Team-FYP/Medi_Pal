package lk.ac.mrt.cse.medipal.view;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.CorneredSort;
import com.willowtreeapps.spruce.sort.LinearSort;

import lk.ac.mrt.cse.medipal.R;

public class UserTypeSelectionActivity extends AppCompatActivity {
    LinearLayout layout_user_selector_container;
    LinearLayout layout_doctor_selector_container;
    LinearLayout layout_patient_selector_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_selection);
        layout_user_selector_container = (LinearLayout) findViewById(R.id.activity_user_type_selection);
        layout_doctor_selector_container = (LinearLayout) findViewById(R.id.layout_doctor_selector);
        layout_patient_selector_container = (LinearLayout) findViewById(R.id.layout_patient_selector);
        animate_ui();
    }
    private void animate_ui(){
        Animator[] animators = new Animator[]{
                DefaultAnimations.shrinkAnimator(layout_user_selector_container, /*duration=*/800),
                DefaultAnimations.fadeInAnimator(layout_user_selector_container, /*duration=*/800)
        };
        Animator spruceAnimator = new Spruce
                .SpruceBuilder(layout_user_selector_container)
                .sortWith(new LinearSort(500, false, LinearSort.Direction.RIGHT_TO_LEFT))
                .animateWith(animators)
                .start();
    }
}
