
package com.example.problemsolver.Registration;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisteredPerson {

    @SerializedName("roles")
    @Expose
    private List<Role> roles;

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

    @SerializedName("personAreas")
    @Expose
    private List<Area> personAreas;

    public RegisteredPerson(String firstName, String secondName, String email1, String phone, String password1, String bithdate1, List<Role> roles,
                            List<Area> personAreas) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email1;
        this.password = password1;
        this.phone = phone;
        this.birthDate = bithdate1;
        this.roles = roles;
        this.personAreas = personAreas;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
