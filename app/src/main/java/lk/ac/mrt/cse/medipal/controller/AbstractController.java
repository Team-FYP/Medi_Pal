package lk.ac.mrt.cse.medipal.controller;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.rest.MedipalAPI;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static lk.ac.mrt.cse.medipal.constant.Connection.Network.NETWORK_CALL_RETRY_COUNT;

/**
 * Created by lakshan on 11/2/17.
 */

public abstract class AbstractController {
    Gson gson;
    Retrofit retrofit;
    MedipalAPI medipalAPI;
    HttpLoggingInterceptor loggingInterceptor;
    Interceptor retryInterceptor;
    public AbstractController() {
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retryInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                int tryCount = 0;
                while (!response.isSuccessful() && tryCount < NETWORK_CALL_RETRY_COUNT) {
                    Log.d("intercept", "Request is not successful - " + tryCount);
                    tryCount++;
                    response = chain.proceed(request);
                }
                return response;
            }
        };
        gson = new GsonBuilder()
                .setLenient()
                .create();
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(retryInterceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Common.URL.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        medipalAPI = retrofit.create(MedipalAPI.class);
    }
}
