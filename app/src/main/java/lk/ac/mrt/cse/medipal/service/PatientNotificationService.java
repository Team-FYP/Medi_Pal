package lk.ac.mrt.cse.medipal.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.constant.SharedPreferencesKeys;
import lk.ac.mrt.cse.medipal.controller.DoctorController;
import lk.ac.mrt.cse.medipal.model.Doctor;
import lk.ac.mrt.cse.medipal.model.Notification;
import lk.ac.mrt.cse.medipal.model.Patient;
import lk.ac.mrt.cse.medipal.model.network.NotificationResponse;
import lk.ac.mrt.cse.medipal.util.JsonConvertor;
import lk.ac.mrt.cse.medipal.util.VectorDrawableUtil;
import lk.ac.mrt.cse.medipal.view.SplashActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lakshan on 11/19/17.
 */

public class PatientNotificationService extends Service {
    public Context context = this;
    public Handler handler = null;
    public ArrayList<Notification> notificationList;
    public static Runnable runnable = null;
    SharedPreferences mPrefs ;
    public String registration_id = null;
    public Patient patient;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (registration_id == null) {
                    mPrefs = getSharedPreferences(SharedPreferencesKeys.MEDIPAL_KEY, Context.MODE_PRIVATE);
                    patient = (Patient) JsonConvertor.getElementObject(mPrefs, SharedPreferencesKeys.PATIENT_OBJECT_KEY, Patient.class);
                    registration_id = patient.getNic();
                }
                retireveNotifications();
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 10000);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart(Intent intent, int startid) {
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
                    for (final Notification notification : notificationList){
                        if (notification.isStatus().equals(Common.NotificationStatus.NEW)){
                            ImageRequest imageRequest = ImageRequest.fromUri(notification.getDoctor().getImage());
                            ImagePipeline imagePipeline = Fresco.getImagePipeline();
                            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
                            dataSource.subscribe(
                                    new BaseBitmapDataSubscriber() {

                                        @Override
                                        protected void onNewResultImpl(Bitmap bitmap) {
                                            sendNotofication(notification, bitmap);
                                        }

                                        @Override
                                        protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                                            // In general, failing to fetch the image should not keep us from displaying the
                                            // notification. We proceed without the bitmap.
                                            sendNotofication(notification, null);
                                        }
                                    },
                                    UiThreadImmediateExecutorService.getInstance());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
            }
        };
        DoctorController doctorController = new DoctorController();
        doctorController.getNotificationResponseList(notificationResponseListResponse, registration_id);
    }

    public void sendNotofication(Notification notification, Bitmap bitmap){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.medipal_logo);
        if (bitmap == null) {
            mBuilder.setLargeIcon(VectorDrawableUtil.getBitmapfromResource(R.drawable.medipal_logo, context));
        } else {
            mBuilder.setLargeIcon(bitmap);
        }
        mBuilder.setContentTitle(notification.getPatient().getName());
        mBuilder.setContentText(notification.getMessage());
        mBuilder.setDefaults(android.app.Notification.DEFAULT_ALL);
        Intent resultIntent = new Intent(context, SplashActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notification.getNotification_id(), mBuilder.build());
    }

}
