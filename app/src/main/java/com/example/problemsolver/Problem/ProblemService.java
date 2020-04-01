package com.example.problemsolver.Problem;

import com.example.problemsolver.ServerApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProblemService {

    private static ProblemService mInstance;
    private static final String BASE_URL = "https://geocode-maps.yandex.ru/1.x/";
    private Retrofit mRetrofit;

    private ProblemService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }//с помощью билдера теперь можем преобразовывать json данные с помощью Gson


    public static ProblemService getInstance() { //должен быть singleton-объектом
        if (mInstance == null) {
            mInstance = new ProblemService();
        }
        return mInstance;
    }

    //хотим, чтобы Retrofit предоставил реализацию интерфейса getJSONApi (черезе метод create)
    public ServerApi getJSONApi() {
        return mRetrofit.create(ServerApi.class);
    }
}
