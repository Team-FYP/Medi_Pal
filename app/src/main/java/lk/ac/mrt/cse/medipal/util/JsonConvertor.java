package lk.ac.mrt.cse.medipal.util;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;

import lk.ac.mrt.cse.medipal.constant.UserType;
import lk.ac.mrt.cse.medipal.model.Patient;

/**
 * Created by lakshan on 11/10/17.
 */

public class JsonConvertor {
    public static Object getElementObject(Intent intent, String extraKey, Class elementClass ){
        Gson gson = new Gson();
        return gson.fromJson(intent.getStringExtra(extraKey), elementClass);
    }

    public static Object getElementObject(Bundle bundle, String extraKey, Class elementClass ){
        Gson gson = new Gson();
        return gson.fromJson(bundle.getString(extraKey), elementClass);
    }
}
