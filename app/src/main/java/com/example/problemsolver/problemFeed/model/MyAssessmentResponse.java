package com.example.problemsolver.problemFeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyAssessmentResponse {

    @SerializedName("response")
    @Expose
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
