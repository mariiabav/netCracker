
package com.example.problemsolver.Models.NewProblemResponse;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeatureMember {

    @SerializedName("GeoObject")
    @Expose
    private GeoObject geoObject;

    public GeoObject getGeoObject() {
        return geoObject;
    }

    public void setGeoObject(GeoObject geoObject) {
        this.geoObject = geoObject;
    }

}
