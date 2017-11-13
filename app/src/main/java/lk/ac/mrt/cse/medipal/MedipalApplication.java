package lk.ac.mrt.cse.medipal;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

import zemin.notification.NotificationDelegater;

/**
 * Created by lakshan on 10/23/17.
 */

public class MedipalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        NotificationDelegater.initialize(this, NotificationDelegater.GLOBAL);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearCaches();
    }
}
