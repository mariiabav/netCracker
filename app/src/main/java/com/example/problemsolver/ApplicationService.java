package com.example.problemsolver;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.problemsolver.Main.MainActivity;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ApplicationService {

    private static ApplicationService mInstance;
    private static final String BASE_URL = "https://netcrackeredu.herokuapp.com";
    private Retrofit mRetrofit;
    private String token;


    private ApplicationService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();



        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }


    public static synchronized ApplicationService getInstance() {
        if (mInstance == null) {
            mInstance = new ApplicationService();
        }
        return mInstance;
    }

    public ServerApi getJSONApi() {
        return mRetrofit.create(ServerApi.class);
    }

}
