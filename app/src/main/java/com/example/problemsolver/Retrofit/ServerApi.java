package com.example.problemsolver.Retrofit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServerApi {

    @POST("/api/person/register")
    public Call<RegistedPerson> postRegistedPersonData(@Body RegistedPerson data);
}
