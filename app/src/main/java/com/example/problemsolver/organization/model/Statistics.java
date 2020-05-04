package com.example.problemsolver.organization.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistics {
    @SerializedName("allProblemsCount")
    @Expose
    private Integer allProblemsCount;

    @SerializedName("initProblemsCount")
    @Expose
    private Integer initProblemsCount;

    @SerializedName("solvedProblemsCount")
    @Expose
    private Integer solvedProblemsCount;

    @SerializedName("unsolvedProblemsCount")
    @Expose
    private Integer unsolvedProblemsCount;

    @SerializedName("closedProblemsCount")
    @Expose
    private Integer closedProblemsCount;

    @SerializedName("inProcessProblemsCount")
    @Expose
    private Integer inProcessProblemsCount;

    public Integer getAllProblemsCount() {
        return allProblemsCount;
    }

    public void setAllProblemsCount(Integer allProblemsCount) {
        this.allProblemsCount = allProblemsCount;
    }

    public Integer getInitProblemsCount() {
        return initProblemsCount;
    }

    public void setInitProblemsCount(Integer initProblemsCount) {
        this.initProblemsCount = initProblemsCount;
    }

    public Integer getSolvedProblemsCount() {
        return solvedProblemsCount;
    }

    public void setSolvedProblemsCount(Integer solvedProblemsCount) {
        this.solvedProblemsCount = solvedProblemsCount;
    }

    public Integer getUnsolvedProblemsCount() {
        return unsolvedProblemsCount;
    }

    public void setUnsolvedProblemsCount(Integer unsolvedProblemsCount) {
        this.unsolvedProblemsCount = unsolvedProblemsCount;
    }

    public Integer getClosedProblemsCount() {
        return closedProblemsCount;
    }

    public void setClosedProblemsCount(Integer closedProblemsCount) {
        this.closedProblemsCount = closedProblemsCount;
    }

    public Integer getInProcessProblemsCount() {
        return inProcessProblemsCount;
    }

    public void setInProcessProblemsCount(Integer inProcessProblemsCount) {
        this.inProcessProblemsCount = inProcessProblemsCount;
    }
}
