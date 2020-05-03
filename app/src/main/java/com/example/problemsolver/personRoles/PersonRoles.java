
package com.example.problemsolver.personRoles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonRoles {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("role")
    @Expose
    private Role role;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
