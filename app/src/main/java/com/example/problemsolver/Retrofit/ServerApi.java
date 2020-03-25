package com.example.problemsolver.Retrofit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ServerApi {
    @Headers("Content-Type: application/json")
    @POST("/api/person/register")
    public Call<RegistedPerson> postRegistedPersonData(
            @Body RegistedPerson registedPerson
    );
}
