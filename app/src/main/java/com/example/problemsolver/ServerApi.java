package com.example.problemsolver;


import com.example.problemsolver.Problem.Models.DistrictResponse.DistrictResponse;
import com.example.problemsolver.Problem.Models.NewProblemResponse.RegionDataResponse;
import com.example.problemsolver.Problem.NewProblem;
import com.example.problemsolver.Registration.RegisteredPerson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ServerApi {
    @Headers("Content-Type: application/json")
    @POST("/api/person/register")
    public Call<RegisteredPerson> postRegistedPersonData(
            @Body RegisteredPerson registeredPerson
    );

    @Headers("Content-Type: application/json")
    @GET(".")
    public Call<RegionDataResponse> getRegionData(
            @Query("apikey") String apiKey,
            @Query("format") String format,
            @Query("geocode") String geoCode
    );

    @Headers("Content-Type: application/json")
    @GET(".")
    public Call<DistrictResponse> getDistrictName(
            @Query("apikey") String apiKey,
            @Query("format") String format,
            @Query("geocode") String coordinates
    );

    @Headers("Content-Type: application/json")
    @POST("/api/problem/create")
    public Call<NewProblem> postNewProblemData(
            @Body NewProblem newProblem
    );

    //для ГЕТ запроса
    ///api/problem/all


}
