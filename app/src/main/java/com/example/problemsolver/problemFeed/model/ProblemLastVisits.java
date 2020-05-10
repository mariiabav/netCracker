package com.example.problemsolver.problemFeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProblemLastVisits {
    @SerializedName("person")
    @Expose
    private String person;

    @SerializedName("lastVisitTime")
    @Expose
    private String lastVisitTime;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(String lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }
}
