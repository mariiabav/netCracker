package com.example.problemsolver.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static NetworkService mInstance;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com"; //фейковый онлайн REST API для тестов
    private Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    } //с помощью билдера теперь можем преобразовывать json данные с помощью Gson


    public static NetworkService getInstance() { //должен быть singleton-объектом
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    //хотим, чтобы Retrofit предоставил реализацию интерфейса getJSONApi (черезе метод create)
    public ServerApi getJSONApi() {
        return mRetrofit.create(ServerApi.class);
    }
}
