package com.example.problemsolver;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapService {

    private static MapService mInstance;
    private static final String BASE_URL = "https://geocode-maps.yandex.ru/1.x/";
    private Retrofit mRetrofit;
    private OkHttpClient client;

    private MapService() {
        /*
        if(PersistantStorage.getProperty("Authorization") != null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization", PersistantStorage.getProperty("Authorization"))
                                .build();
                        return chain.proceed(request);
                    }).build();
        }
         */

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public static MapService getInstance() { //должен быть singleton-объектом
        if (mInstance == null) {
            mInstance = new MapService();
        }
        return mInstance;
    }

    //хотим, чтобы Retrofit предоставил реализацию интерфейса getJSONApi (черезе метод create)
    public ServerApi getJSONApi() {
        return mRetrofit.create(ServerApi.class);
    }
}
