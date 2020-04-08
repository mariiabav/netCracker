
package com.example.problemsolver.Organization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.problemsolver.Organization.Area;

public class Address {

    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("building")
    @Expose
    private String building;
    @SerializedName("area")
    @Expose
    private Area area;

    public Address(String street, String building, Area area){
        this.street = street;
        this.building = building;
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

}
