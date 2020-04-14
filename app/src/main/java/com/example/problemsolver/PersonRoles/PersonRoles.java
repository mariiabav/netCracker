
package com.example.problemsolver.PersonRoles;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonRoles {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("roles")
    @Expose
    private List<Role> roles = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
