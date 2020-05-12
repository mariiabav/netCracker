
package com.example.problemsolver.registration;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisteredPerson {

    @SerializedName("personAreas")
    @Expose
    private List<Area> personAreas;

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

    @SerializedName("birthDate")
    @Expose
    private String birthDate;

    @SerializedName("orgCode")
    @Expose
    private String orgCode;
    public RegisteredPerson(String firstName, String secondName, String email1, String phone, String password1, String bithdate1, Role role,
                            List<Area> personAreas, String orgCode) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email1;
        this.password = password1;
        this.phone = phone;
        this.birthDate = bithdate1;
        this.role = role;
        this.personAreas = personAreas;
        this.orgCode = orgCode;
    }

    public List<Area> getPersonAreas() {
        return personAreas;
    }

    public void setPersonAreas(List<Area> personAreas) {
        this.personAreas = personAreas;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

}
