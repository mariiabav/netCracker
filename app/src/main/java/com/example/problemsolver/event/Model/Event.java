
package com.example.problemsolver.event.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userText")
    @Expose
    private String userText;
    @SerializedName("moderatorText")
    @Expose
    private String moderatorText;
    @SerializedName("offerStatus")
    @Expose
    private String offerStatus;
    @SerializedName("offerDate")
    @Expose
    private String offerDate;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("moderator")
    @Expose
    private Moderator moderator;
    @SerializedName("problem")
    @Expose
    private Problem problem;
    @SerializedName("photo")
    @Expose
    private List<String> pictures;

    public Event() {
    }

    public Event(String userText, String offerStatus, Problem problem, List<String> pictures) {
        this.userText = userText;
        this.offerStatus = offerStatus;
        this.problem = problem;
        this.pictures = pictures;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(String offerDate) {
        this.offerDate = offerDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Moderator getModerator() {
        return moderator;
    }

    public void setModerator(Moderator moderator) {
        this.moderator = moderator;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public String getModeratorText() {
        return moderatorText;
    }

    public void setModeratorText(String moderatorText) {
        this.moderatorText = moderatorText;
    }
}
