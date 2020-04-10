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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ApplicationService {

    private static ApplicationService mInstance;
    private static final String BASE_URL = "https://netcrackeredu.herokuapp.com";
    private Retrofit mRetrofit;
    private String token;


    private ApplicationService() {
        OkHttpClient httpClient = new OkHttpClient();

        httpClient.newBuilder().addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .method(chain.request().method(), chain.request().body())
                    .build();
            Response response = chain.proceed(request);
            return response;
        });

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
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
