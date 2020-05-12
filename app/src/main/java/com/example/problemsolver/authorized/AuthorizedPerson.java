
package com.example.problemsolver.authorized;

import android.provider.ContactsContract;

import java.util.List;

import com.example.problemsolver.organization.model.RegisteredOrganization;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthorizedPerson {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("personAreas")
    @Expose
    private List<PersonArea> personAreas = null;
    @SerializedName("personProblemsAsParticipant")
    @Expose
    private List<Object> personProblemsAsParticipant = null;
    @SerializedName("personProblemsAsOwner")
    @Expose
    private List<Object> personProblemsAsOwner = null;
    @SerializedName("role")
    @Expose
    private Role role;
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
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("organization")
    @Expose
    private RegisteredOrganization organization;

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

    public List<Object> getPersonProblemsAsParticipant() {
        return personProblemsAsParticipant;
    }

    public void setPersonProblemsAsParticipant(List<Object> personProblemsAsParticipant) {
        this.personProblemsAsParticipant = personProblemsAsParticipant;
    }

    public List<Object> getPersonProblemsAsOwner() {
        return personProblemsAsOwner;
    }

    public void setPersonProblemsAsOwner(List<Object> personProblemsAsOwner) {
        this.personProblemsAsOwner = personProblemsAsOwner;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
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

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public RegisteredOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(RegisteredOrganization organization) {
        this.organization = organization;
    }
}
