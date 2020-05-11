
package com.example.problemsolver.problem.model;

import com.example.problemsolver.organization.model.RegisteredOrganization;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("coordinates")
    @Expose
    private String coordinates;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("pictures")
    @Expose
    private List<String> pictures;
    @SerializedName("organization")
    @Expose
    private RegisteredOrganization orgId;

    public NewProblem(Address address, String problemName, String description, String status,  String coordinates, Owner owner, List<String> pictures, RegisteredOrganization orgId) {
        this.address = address;
        this.problemName = problemName;
        this.description = description;
        this.status = status;
        this.coordinates = coordinates;
        this.owner = owner;
        this.pictures = pictures;
        this.orgId = orgId;
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


    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public RegisteredOrganization getOrgId() {
        return orgId;
    }

    public void setOrgId(RegisteredOrganization orgId) {
        this.orgId = orgId;
    }
}
