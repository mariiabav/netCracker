
package com.example.problemsolver.Problem.Models.DistrictResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DependentLocality_ {

    @SerializedName("DependentLocalityName")
    @Expose
    private String dependentLocalityName;

    public String getDependentLocalityName() {
        return dependentLocalityName;
    }

    public void setDependentLocalityName(String dependentLocalityName) {
        this.dependentLocalityName = dependentLocalityName;
    }

}
