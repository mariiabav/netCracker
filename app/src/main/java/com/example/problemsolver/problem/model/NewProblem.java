
package com.example.problemsolver.problem.model;

import com.example.problemsolver.organization.model.RegisteredOrganization;
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
    @SerializedName("coordinates")
    @Expose
    private String coordinates;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("picture")
    @Expose
    private DBFile picture;
    @SerializedName("organization")
    @Expose
    private RegisteredOrganization orgId;

    public NewProblem(Address address, String problemName, String description, String status,  String coordinates, Owner owner, DBFile dbFile, RegisteredOrganization orgId) {
        this.address = address;
        this.problemName = problemName;
        this.description = description;
        this.status = status;
        this.coordinates = coordinates;
        this.owner = owner;
        this.picture = dbFile;
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

    public DBFile getPicture() {
        return picture;
    }

    public void setPicture(DBFile picture) {
        this.picture = picture;
    }

    public RegisteredOrganization getOrgId() {
        return orgId;
    }

    public void setOrgId(RegisteredOrganization orgId) {
        this.orgId = orgId;
    }
}
