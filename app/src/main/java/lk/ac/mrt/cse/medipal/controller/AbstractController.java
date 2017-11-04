package lk.ac.mrt.cse.medipal.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.rest.MedipalAPI;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lakshan on 11/2/17.
 */

public class AbstractController {
    Gson gson;
    Retrofit retrofit;
    MedipalAPI medipalAPI;
    HttpLoggingInterceptor loggingInterceptor;
    public AbstractController() {
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        gson = new GsonBuilder()
                .setLenient()
                .create();
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Common.URL.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        medipalAPI = retrofit.create(MedipalAPI.class);
    }
}
