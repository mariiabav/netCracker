package com.example.problemsolver.organization.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedOrgResponse {
    @SerializedName("organizationDTOList")
    @Expose
    private List<RegisteredOrganization> organizationList;

    @SerializedName("pagesLimit")
    @Expose
    private Integer pagesLimit;

    public List<RegisteredOrganization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<RegisteredOrganization> organizationList) {
        this.organizationList = organizationList;
    }

    public Integer getPagesLimit() {
        return pagesLimit;
    }

    public void setPagesLimit(Integer pagesLimit) {
        this.pagesLimit = pagesLimit;
    }
}
