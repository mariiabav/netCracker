package com.example.problemsolver.problemFeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedResponse {
    @SerializedName("problemDTOList")
    @Expose
    private List<Feed2Problem> feed2ProblemList;

    @SerializedName("pagesLimit")
    @Expose
    private Integer pagesLimit;

    public List<Feed2Problem> getFeed2ProblemList() {
        return feed2ProblemList;
    }

    public void setFeed2ProblemList(List<Feed2Problem> feed2ProblemList) {
        this.feed2ProblemList = feed2ProblemList;
    }

    public Integer getPagesLimit() {
        return pagesLimit;
    }

    public void setPagesLimit(Integer pagesLimit) {
        this.pagesLimit = pagesLimit;
    }
}
