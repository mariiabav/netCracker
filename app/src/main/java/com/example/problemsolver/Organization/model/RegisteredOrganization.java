
package com.example.problemsolver.Organization.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.UUID;

public class RegisteredOrganization {

    @SerializedName("id")
    @Expose
    private UUID id;

    @SerializedName("address")
    @Expose
    private Address address;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("description")
    @Expose
    private String description;

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


    public RegisteredOrganization() {
    }

    public RegisteredOrganization(Address address, String name, String description, String email, String phone) {
        this.address = address;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
