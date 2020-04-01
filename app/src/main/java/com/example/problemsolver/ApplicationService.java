package com.example.problemsolver;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationService {

    private static ApplicationService mInstance;
    private static final String BASE_URL = "https://netcrackeredu.herokuapp.com";
    private Retrofit mRetrofit;

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                    Credentials.basic("login@mail.ru", "pass"));

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    }).build();

    private ApplicationService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    } //с помощью билдера теперь можем преобразовывать json данные с помощью Gson


    public static ApplicationService getInstance() { //должен быть singleton-объектом
        if (mInstance == null) {
            mInstance = new ApplicationService();
        }
        return mInstance;
    }

    //хотим, чтобы Retrofit предоставил реализацию интерфейса getJSONApi (черезе метод create)
    public ServerApi getJSONApi() {
        return mRetrofit.create(ServerApi.class);
    }
}
