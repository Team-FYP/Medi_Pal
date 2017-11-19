package lk.ac.mrt.cse.medipal.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.util.VectorDrawableUtil;
import lk.ac.mrt.cse.medipal.view.SplashActivity;

/**
 * Created by lakshan on 11/19/17.
 */

public class PatientNotificationService extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.medipal_logo);
                mBuilder.setLargeIcon(VectorDrawableUtil.getBitmapfromResource(R.drawable.medipal_logo, context));
                mBuilder.setContentTitle("Notification Alert, Click Me!");
                mBuilder.setContentText("Hi, This is Android Notification Detail!");
                Intent resultIntent = new Intent(context, SplashActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(SplashActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
                mNotificationManager.notify(123, mBuilder.build());
                handler.postDelayed(runnable, 5000);
            }
        };

        handler.postDelayed(runnable, 5000);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }
}
