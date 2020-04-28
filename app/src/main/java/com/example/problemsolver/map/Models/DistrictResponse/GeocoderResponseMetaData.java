
package com.example.problemsolver.map.Models.DistrictResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeocoderResponseMetaData {

    @SerializedName("Point")
    @Expose
    private Point point;
    @SerializedName("request")
    @Expose
    private String request;
    @SerializedName("results")
    @Expose
    private String results;
    @SerializedName("found")
    @Expose
    private String found;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }

}
