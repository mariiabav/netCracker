package com.example.problemsolver;


import com.example.problemsolver.Feed.model.FeedResponse;
import com.example.problemsolver.Organization.RegisteredOrganization;
import com.example.problemsolver.Map.Models.DistrictResponse.DistrictResponse;
import com.example.problemsolver.Map.Models.NewProblemResponse.RegionDataResponse;
import com.example.problemsolver.Problem.NewProblem;
import com.example.problemsolver.Registration.RegisteredPerson;

import java.util.List;

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
    @POST("/api/org/create") //вставить нормальное название серверного метода
    public Call<RegisteredOrganization> postRegistedOrgData(
            @Body RegisteredOrganization registeredOrg
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


    @Headers("Content-Type: application/json")
    @GET("/api/problem/allmap")
    public Call<List<NewProblem>> getAllProblems();

    @Headers("Content-Type: application/json")
    @GET("/api/problem/all")
    public Call<FeedResponse>getAllProblems(
            @Query("pageSize") Integer pageSize,
            @Query("pageNo") Integer pageNo,
            @Query("sort") String creationDate
    );
}
