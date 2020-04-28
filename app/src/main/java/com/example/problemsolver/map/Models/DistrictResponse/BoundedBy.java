
package com.example.problemsolver.map.Models.DistrictResponse;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BoundedBy {

    @SerializedName("Envelope")
    @Expose
    private Envelope envelope;

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

}
