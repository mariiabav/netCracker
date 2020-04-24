package com.example.problemsolver.ProblemFeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchCriteria {

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("operation")
    @Expose
    private String operation;

    @SerializedName("value")
    @Expose
    private String value;

    public SearchCriteria(String key, String operation, String value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "key=" + key + "&" + "operation=" + operation + "&" + "value=" + value;
    }
}
