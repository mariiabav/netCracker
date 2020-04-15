
package com.example.problemsolver.Event.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Problem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("owner")
    @Expose
    private Owner owner;
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
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("coordinates")
    @Expose
    private String coordinates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

}
