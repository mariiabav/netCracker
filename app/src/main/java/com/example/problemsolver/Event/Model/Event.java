
package com.example.problemsolver.Event.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("text")
    @Expose
    private String text;
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

    public Event() {
    }

    public Event(String offerStatus, String offerDate) {
        this.offerStatus = offerStatus;
        this.offerDate = offerDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

}
