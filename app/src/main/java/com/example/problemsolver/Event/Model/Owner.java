
package com.example.problemsolver.Event.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("personAreas")
    @Expose
    private List<PersonArea> personAreas = null;
    @SerializedName("personProblemsAsOwner")
    @Expose
    private List<Object> personProblemsAsOwner = null;
    @SerializedName("role")
    @Expose
    private Object role;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("secondName")
    @Expose
    private String secondName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("birthDate")
    @Expose
    private String birthDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PersonArea> getPersonAreas() {
        return personAreas;
    }

    public void setPersonAreas(List<PersonArea> personAreas) {
        this.personAreas = personAreas;
    }

    public List<Object> getPersonProblemsAsOwner() {
        return personProblemsAsOwner;
    }

    public void setPersonProblemsAsOwner(List<Object> personProblemsAsOwner) {
        this.personProblemsAsOwner = personProblemsAsOwner;
    }

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

}
