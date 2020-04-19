package com.example.problemsolver;


import com.example.problemsolver.Authorized.AuthorizedPerson;
import com.example.problemsolver.Event.Model.EventResponse;
import com.example.problemsolver.Feed.model.FeedResponse;
import com.example.problemsolver.Login.LoginForm;
import com.example.problemsolver.Organization.RegisteredOrganization;
import com.example.problemsolver.Map.Models.DistrictResponse.DistrictResponse;
import com.example.problemsolver.Map.Models.NewProblemResponse.RegionDataResponse;
import com.example.problemsolver.PersonRoles.PersonRoles;
import com.example.problemsolver.Problem.NewProblem;
import com.example.problemsolver.Registration.RegisteredPerson;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
            @Header("Authorization") String token,
            @Body RegisteredOrganization registeredOrg
    );

    @Headers("Content-Type: application/json")
    @GET(".")
    public Call<RegionDataResponse> getRegionData(
            @Header("Authorization") String token,
            @Query("apikey") String apiKey,
            @Query("format") String format,
            @Query("geocode") String geoCode
    );

    @Headers("Content-Type: application/json")
    @GET(".")
    public Call<DistrictResponse> getDistrictName(
            @Header("Authorization") String token,
            @Query("apikey") String apiKey,
            @Query("format") String format,
            @Query("geocode") String coordinates
    );

    @Headers("Content-Type: application/json")
    @POST("/api/problem/create")
    public Call<NewProblem> postNewProblemData(
            @Header("Authorization") String token,
            @Body NewProblem newProblem
    );


    @Headers("Content-Type: application/json")
    @GET("/api/problem/allmap")
    public Call<List<NewProblem>> getAllProblems(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("/api/problem/all")
    public Call<FeedResponse>getAllProblems(
            @Header("Authorization") String token,
            @Query("pageNumber") Integer pageNumber,
            @Query("pageSize") Integer pageSize,
            @Query("sortBy") String sortBy,
            @Query("sortHow") String sortHow,
            @Query("filters") List<String> filters

    );


    @POST("/login")
    public Call<Void> login(
            @Body LoginForm loginForm
    );


    @Headers("Content-Type: application/json")
    @GET("/api/person/info")
    public Call<PersonRoles> getPersonRoles(
            @Header("Authorization") String token
    );

    @Headers("Content-Type: application/json")
    @GET("/api/person/{id}")
    public Call<AuthorizedPerson> getAuthorizedPersonInfo(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @Headers("Content-Type: application/json")
    @PATCH("/api/problem/subscribe")
    public Call<Void> subscribe(
            @Header("Authorization") String token,
            @Query("problemID") String problemId,
            @Query("personID") String personId
    );

    @Headers("Content-Type: application/json")
    @GET("/api/event/all")
    public Call<EventResponse> getEvents(
            @Header("Authorization") String token,
            @Query("pageNumber") Integer pageNumber,
            @Query("pageSize") Integer pageSize,
            @Query("sortBy") String sortBy,
            @Query("sortHow") String sortHow
    );

    @Headers("Content-Type: application/json")
    @GET("/api/event/all")
    public Call<Void> updateEvent(
            @Header("Authorization") String token,
            @Query("text") String text,
            @Query("status") String status,
            @Query("result") String result,
            @Query("moderatorId") String moderatorId
    );


    @Multipart
    @POST("/uploadFile")
    public Call<Photo> uploadFile(
            @Header("Authorization") String token,
            //@Query("multipartFile") MultipartFile file
            @Part MultipartBody.Part image
    );

    @Headers("Content-Type: application/json")
    @PUT("/api/problem/like")
    public Call<Void> setLike(
            @Header("Authorization") String token,
            @Query("problemId") String problemId,
            @Query("personId") String personId
    );

    @Headers("Content-Type: application/json")
    @PUT("/api/problem/dislike")
    public Call<Void> setDislike(
            @Header("Authorization") String token,
            @Query("problemId") String problemId,
            @Query("personId") String personId
    );

    @Headers("Content-Type: application/json")
    @GET("/api/problem/assessment")
    public Call<Assessment> getAssessment(
            @Header("Authorization") String token,
            @Query("problemId") String problemId
    );

}
