
package com.example.problemsolver.Problem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewProblem {

    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("problemName")
    @Expose
    private String problemName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("coordinates")
    @Expose
    private String coordinates;

    public NewProblem(Address address, String problemName, String description, String status, Integer rate, String coordinates) {
        this.address = address;
        this.problemName = problemName;
        this.description = description;
        this.status = status;
        this.rate = rate;
        this.coordinates = coordinates;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

}
