package com.example.problemsolver.Problem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DBFile {

    @SerializedName("id")
    @Expose
    private String id;

    public DBFile(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
