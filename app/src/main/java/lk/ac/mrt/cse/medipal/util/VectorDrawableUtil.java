package lk.ac.mrt.cse.medipal.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import lk.ac.mrt.cse.medipal.model.Patient;

/**
 * Created by chand on 2017-06-14.
 */

public class VectorDrawableUtil {

    public static Drawable getDrawable(Context context, int drawableResId) {
        Drawable drawable;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            drawable = context.getResources().getDrawable(drawableResId, context.getTheme());
        } else {
            drawable = VectorDrawableCompat.create(context.getResources(), drawableResId, context.getTheme());
        }

        return drawable;
    }

    public static Drawable getDrawable(Context context, int drawableResId, int colorFilter) {
        Drawable drawable = getDrawable(context, drawableResId);
        drawable.setColorFilter(ContextCompat.getColor(context, colorFilter), PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = getDrawable(context, drawableId);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getBitmapfromString(String image) {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Drawable getDrawablefromString(String image, Context context) {
        Bitmap decodedByte = getBitmapfromString(image);
        Drawable d = new BitmapDrawable(context.getResources(), decodedByte);
        return d;
    }

    public static String getBase64fromBitmap(Bitmap bitmap, Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public static Bitmap getBitmapfromResource(int resource, Context context) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resource);
        return bm;
    }

    public static String getBase64fromReource(int resource, Context context){
        return getBase64fromBitmap(getBitmapfromResource(resource,context),context);
    }
}