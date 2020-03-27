package com.example.problemsolver;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProblemService {


    private static ProblemService mInstance;
    private static final String BASE_URL = "https://geocode-maps.yandex.ru";
    private Retrofit mRetrofit;


    private ProblemService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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
