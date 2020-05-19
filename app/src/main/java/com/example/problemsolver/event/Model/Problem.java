
package com.example.problemsolver.event.Model;

import com.example.problemsolver.organization.model.RegisteredOrganization;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Problem {

    @SerializedName("id")
    @Expose
    private String id;
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
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("coordinates")
    @Expose
    private String coordinates;
    @SerializedName("personsOfThisProblemAsParticipant")
    @Expose
    private List<String> personsOfThisProblemAsParticipant;
    @SerializedName("scale")
    @Expose
    private String scale;
    @SerializedName("pictures")
    @Expose
    private List<String> pictures;
    @SerializedName("organization")
    @Expose
    private RegisteredOrganization organization;

    public Problem() {
    }

    public Problem(String id) {
        this.id = id;
    }

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

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public List<String> getPersonsOfThisProblemAsParticipant() {
        return personsOfThisProblemAsParticipant;
    }

    public void setPersonsOfThisProblemAsParticipant(List<String> personsOfThisProblemAsParticipant) {
        this.personsOfThisProblemAsParticipant = personsOfThisProblemAsParticipant;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public RegisteredOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(RegisteredOrganization organization) {
        this.organization = organization;
    }
}
