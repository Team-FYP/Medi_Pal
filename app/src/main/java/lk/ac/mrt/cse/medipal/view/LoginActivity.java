package lk.ac.mrt.cse.medipal.view;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.CorneredSort;
import com.willowtreeapps.spruce.sort.LinearSort;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.view.doctor.DoctorMainActivity;
import lk.ac.mrt.cse.medipal.view.patient.PatientMainActivity;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout layout_login_container;
    TextView text_create_account;
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout_login_container = (RelativeLayout) findViewById(R.id.layout_login_container);
        text_create_account = (TextView) findViewById(R.id.text_create_account);
        text_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserTypeSelectionActivity.class);
                startActivity(intent);
            }
        });
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, DoctorMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        animate_ui();
    }

    private void animate_ui(){
        Animator[] animators = new Animator[]{
                DefaultAnimations.shrinkAnimator(layout_login_container, /*duration=*/800),
                DefaultAnimations.fadeInAnimator(layout_login_container, /*duration=*/800)
        };
        Animator spruceAnimator = new Spruce
                .SpruceBuilder(layout_login_container)
                .sortWith(new CorneredSort(1000, false, CorneredSort.Corner.TOP_LEFT))
                .animateWith(animators)
                .start();
    }
}
