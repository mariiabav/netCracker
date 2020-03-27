package com.example.problemsolver;


import com.example.problemsolver.Retrofit.RegistedPerson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ServerApi {
    @Headers("Content-Type: application/json")
    @POST("/api/person/register")
    public Call<RegistedPerson> postRegistedPersonData(
            @Body RegistedPerson registedPerson
    );

    @Headers("Content-Type: application/json")
    @GET("/1.x/")
    public Call<MyPoint> getRegionData(
            @Query("apikey") String apiKey,
            @Query("geocode") String geoCode
    );
}
