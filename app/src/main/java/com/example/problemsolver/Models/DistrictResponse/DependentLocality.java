
package com.example.problemsolver.Models.DistrictResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DependentLocality {

    @SerializedName("DependentLocalityName")
    @Expose
    private String dependentLocalityName;
    @SerializedName("DependentLocality")
    @Expose
    private DependentLocality_ dependentLocality;

    public String getDependentLocalityName() {
        return dependentLocalityName;
    }

    public void setDependentLocalityName(String dependentLocalityName) {
        this.dependentLocalityName = dependentLocalityName;
    }

    public DependentLocality_ getDependentLocality() {
        return dependentLocality;
    }

    public void setDependentLocality(DependentLocality_ dependentLocality) {
        this.dependentLocality = dependentLocality;
    }

}
