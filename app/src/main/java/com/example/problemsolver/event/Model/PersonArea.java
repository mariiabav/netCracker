
package com.example.problemsolver.event.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonArea {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("areaName")
    @Expose
    private String areaName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

}
