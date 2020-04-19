
package com.example.problemsolver.Organization.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Area {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("areaName")
    @Expose
    private String areaName;

    public Area(String areaName){
        this.areaName = areaName;
    }

    public String getId() {
        return id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

}
