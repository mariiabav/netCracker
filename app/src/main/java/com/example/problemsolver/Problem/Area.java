
package com.example.problemsolver.Problem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Area {

    @SerializedName("areaName")
    @Expose
    private String areaName;

    public Area(String areaName){
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
