package lk.ac.mrt.cse.medipal.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import io.supercharge.shimmerlayout.ShimmerLayout;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Alert;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.Connection;
import lk.ac.mrt.cse.medipal.constant.SharedPreferencesKeys;
import lk.ac.mrt.cse.medipal.constant.UserType;
import lk.ac.mrt.cse.medipal.view.doctor.DoctorMainActivity;
import lk.ac.mrt.cse.medipal.view.patient.PatientMainActivity;

public class SplashActivity extends AppCompatActivity {
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private ImageView logo_image;
    private Context context;
    LocationManager lm;
    boolean network_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        logo_image = (ImageView) findViewById(R.id.logo_image);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ShimmerLayout shimmerText = (ShimmerLayout) findViewById(R.id.activity_splash);
                shimmerText.startShimmerAnimation();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                checkNetworkAndLocation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logo_image.startAnimation(fadeInAnimation);
    }

    private void checkNetworkAndLocation(){
        new CheckInternet().execute();
    }

    private void goToNextActivity() {
        SharedPreferences mPrefs = getSharedPreferences(SharedPreferencesKeys.USER_LOGIN_KEY, Context.MODE_PRIVATE);
        boolean is_logged_in = mPrefs.getBoolean(SharedPreferencesKeys.IS_LOGGED_IN_KEY, false);
        String user_type = mPrefs.getString(SharedPreferencesKeys.USER_TYPE_KEY, null);
        if (!is_logged_in) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (user_type != null && user_type.equals(UserType.DOCTOR)){

            Intent intent = new Intent(this, DoctorMainActivity.class);
            intent.putExtra(SharedPreferencesKeys.USER_ID_KEY, mPrefs.getString(SharedPreferencesKeys.USER_ID_KEY, null));
            startActivity(intent);
            finish();

        }else if (user_type != null && user_type.equals(UserType.PATIENT)){

            Intent intent = new Intent(this, PatientMainActivity.class);
            intent.putExtra(SharedPreferencesKeys.USER_ID_KEY, mPrefs.getString(SharedPreferencesKeys.USER_ID_KEY, null));
            startActivity(intent);
            finish();
        }
    }

    class CheckInternet extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(Connection.Connectivity.CONNECTION_CHECK_URL).openConnection());
                urlc.setRequestProperty( Connection.Connectivity.USER_AGENT_KEY, Connection.Connectivity.USER_AFENT_VALUE);
                urlc.setRequestProperty(Connection.Connectivity.CONNECTION_KEY,Connection.Connectivity.CONNECTION_VALUE);
                urlc.setConnectTimeout(Connection.Connectivity.CHECK_TIME_OUT);
                urlc.connect();
                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0 ) ? Common.TRUE_TXT : Common.FALSE_TXT;
            } catch (IOException e) {
                return Common.FALSE_TXT;
            }
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            if (file_url.equals(Common.TRUE_TXT)) {
                goToNextActivity();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage(Alert.NO_MOBILE_NETWORK_ALERT);
                dialog.setPositiveButton(Alert.OK_BTN_TXT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        checkNetworkAndLocation();
                    }
                });
                dialog.setNegativeButton(Alert.CANCEL_BTN_TXT, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
                dialog.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (int permisson : grantResults) {
                if (permisson != PackageManager.PERMISSION_GRANTED) {
                    showMessageOKCancel(Alert.GRANT_PERMISSION_TXT, null);
                    return;
                }
            }
            checkNetworkAndLocation();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton(Alert.OK_BTN_TXT, okListener)
                .setNegativeButton(Alert.CANCEL_BTN_TXT, null)
                .create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkNetworkAndLocation();
    }

}
