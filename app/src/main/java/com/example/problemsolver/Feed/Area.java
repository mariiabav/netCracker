
package com.example.problemsolver.Feed;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Area {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("areaPersons")
    @Expose
    private List<Object> areaPersons = null;
    @SerializedName("areaName")
    @Expose
    private String areaName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getAreaPersons() {
        return areaPersons;
    }

    public void setAreaPersons(List<Object> areaPersons) {
        this.areaPersons = areaPersons;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

}
