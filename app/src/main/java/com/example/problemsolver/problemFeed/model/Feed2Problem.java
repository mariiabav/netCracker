package com.example.problemsolver.problemFeed.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Objects;

public class Feed2Problem {

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
    @SerializedName("pictures")
    @Expose
    private List<String> pictures;
    @SerializedName("personsOfThisProblemAsParticipant")
    @Expose
    private List<String> personsOfThisProblemAsParticipant;
    @SerializedName("lastChangeTime")
    @Expose
    private String lastChangeTime;

    public Feed2Problem() {
    }

    public Feed2Problem(String id) {
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

    public String getLastChangeTime() {
        return lastChangeTime;
    }

    public void setLastChangeTime(String lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feed2Problem that = (Feed2Problem) o;
        return id.equals(that.id) &&
                coordinates.equals(that.coordinates);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, coordinates);
    }
}