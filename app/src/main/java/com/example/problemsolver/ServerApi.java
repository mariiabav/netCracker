package com.example.problemsolver;


import com.example.problemsolver.authorized.AuthorizedPerson;
import com.example.problemsolver.event.Model.Event;
import com.example.problemsolver.event.Model.EventResponse;
import com.example.problemsolver.problemFeed.model.Comment;
import com.example.problemsolver.problemFeed.model.CommentResponse;
import com.example.problemsolver.problemFeed.model.FeedResponse;
import com.example.problemsolver.problemFeed.model.MyAssessmentResponse;
import com.example.problemsolver.login.LoginForm;
import com.example.problemsolver.organization.model.FeedOrgResponse;
import com.example.problemsolver.organization.model.RegisteredOrganization;
import com.example.problemsolver.map.Models.DistrictResponse.DistrictResponse;
import com.example.problemsolver.map.Models.NewProblemResponse.RegionDataResponse;
import com.example.problemsolver.personRoles.PersonRoles;
import com.example.problemsolver.problem.model.NewProblem;
import com.example.problemsolver.registration.RegisteredPerson;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
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
    Call<RegionDataResponse> getRegionData(
            @Header("Authorization") String token,
            @Query("apikey") String apiKey,
            @Query("format") String format,
            @Query("geocode") String geoCode
    );

    @Headers("Content-Type: application/json")
    @GET(".")
    Call<DistrictResponse> getDistrictName(
            @Header("Authorization") String token,
            @Query("apikey") String apiKey,
            @Query("format") String format,
            @Query("geocode") String coordinates
    );

    @Headers("Content-Type: application/json")
    @POST("/api/problem/create")
    Call<NewProblem> postNewProblemData(
            @Header("Authorization") String token,
            @Body NewProblem newProblem
    );


    @Headers("Content-Type: application/json")
    @GET("/api/problem/allmap")
    Call<List<NewProblem>> getAllProblems(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("/api/problem/all")
    Call<FeedResponse>getAllProblems(
            @Header("Authorization") String token,
            @Query("pageNumber") Integer pageNumber,
            @Query("pageSize") Integer pageSize,
            @Query("sortBy") String sortBy,
            @Query("sortHow") String sortHow,
            @Query("filters") String filters

    );


    @POST("/login")
    Call<Void> login(
            @Body LoginForm loginForm
    );


    @Headers("Content-Type: application/json")
    @GET("/api/person/info")
    Call<PersonRoles> getPersonRoles(
            @Header("Authorization") String token
    );

    @Headers("Content-Type: application/json")
    @GET("/api/person/{id}")
    Call<AuthorizedPerson> getAuthorizedPersonInfo(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @Headers("Content-Type: application/json")
    @PUT("/api/problem/subscribe")
    Call<Void> subscribe(
            @Header("Authorization") String token,
            @Query("problemId") String problemId,
            @Query("personId") String personId
    );

    @Headers("Content-Type: application/json")
    @GET("/api/event/all")
    Call<EventResponse> getEvents(
            @Header("Authorization") String token,
            @Query("pageNumber") Integer pageNumber,
            @Query("pageSize") Integer pageSize,
            @Query("sortBy") String sortBy,
            @Query("sortHow") String sortHow
    );

    @Headers("Content-Type: application/json")
    @PUT("/api/event/update")
    Call<Void> updateEvent(
            @Header("Authorization") String token,
            @Query("eventId") String eventId,
            @Query("text") String text,
            @Query("status") String status,
            @Query("result") String result,
            @Query("scale") String scale,
            @Query("moderatorId") String moderatorId
    );

    @Multipart
    @POST("/uploadFile")
    Call<Photo> uploadFile(
            @Header("Authorization") String token,
            //@Query("multipartFile") MultipartFile file
            @Part MultipartBody.Part file
    );

    @Headers("Content-Type: application/json")
    @PUT("/api/problem/like")
    Call<Void> setLike(
            @Header("Authorization") String token,
            @Query("problemId") String problemId,
            @Query("personId") String personId
    );

    @Headers("Content-Type: application/json")
    @PUT("/api/problem/dislike")
    Call<Void> setDislike(
            @Header("Authorization") String token,
            @Query("problemId") String problemId,
            @Query("personId") String personId
    );

    @Headers("Content-Type: application/json")
    @GET("/api/problem/assessment")
    Call<Assessment> getAssessment(
            @Header("Authorization") String token,
            @Query("problemId") String problemId
    );
    @Headers("Content-Type: application/json")
    @GET("/api/org/all")
    Call<FeedOrgResponse> getOrgs(
            @Header("Authorization") String token,
            @Query("pageNumber") Integer pageNumber,
            @Query("pageSize") Integer pageSize,
            @Query("sortBy") String sortBy,
            @Query("sortHow") String sortHow,
            @Query("personId") String personId,
            @Query("onlyMyAreas") Boolean onlyMyAreas
    );

    //@Headers("Content-Type: application/json")
    @GET("/api/problem/myAssessment")
    Call<MyAssessmentResponse> getMyAssessment(
            @Header("Authorization") String token,
            @Query("problemId") String problemId,
            @Query("personId") String personId

    );

    @Headers("Content-Type: application/json")
    @GET("/api/comment/all")
    Call<CommentResponse> getComments(
            @Header("Authorization") String token,
            @Query("problemId") String problemId,
            @Query("pageNumber") Integer pageNumber,
            @Query("pageSize") Integer pageSize,
            @Query("sortBy") String sortBy
    );

    @Headers("Content-Type: application/json")
    @POST("/api/comment/create")
    Call<CommentResponse> createComment(
            @Header("Authorization") String token,
            @Body Comment comment
            );

    @Headers("Content-Type: application/json")
    @GET("/api/org/areaOrgs")
    Call<FeedOrgResponse> getAreaOrgs(
            @Header("Authorization") String token,
            @Query("pageNumber") Integer pageNumber,
            @Query("pageSize") Integer pageSize,
            @Query("sortBy") String sortBy,
            @Query("sortHow") String sortHow,
            @Query("areaName") String areaName
    );

    @Headers("Content-Type: application/json")
    @GET("/api/problem/{personId}")
    Call<FeedResponse> getMyProblems(
            @Header("Authorization") String token,
            @Path("personId") String personId,
            @Query("pageNumber") Integer pageNumber,
            @Query("pageSize") Integer pageSize,
            @Query("sortBy") String sortBy,
            @Query("sortHow") String sortHow,
            @Query("filter") String filter
    );

    @Headers("Content-Type: application/json")
    @POST("/api/event/create")
    Call<Void> createEvent(
            @Header("Authorization") String token,
            @Body Event event
            );

    @Headers("Content-Type: application/json")
    @PUT("/process")
    Call<Void> putStatusByOrg(
            @Header("Authorization") String token,
            @Query("problemId") String problemId
    );

    @Headers("Content-Type: application/json")
    @PUT("/api/problem/visit/{problem}")
    Call<Void> visitProblem(
            @Header("Authorization") String token,
            @Path("problem") String problemId,
            @Query("personId") String personId
    );
}
