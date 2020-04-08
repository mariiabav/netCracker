
package com.example.problemsolver.Map.Models.DistrictResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoObjectCollection {

    @SerializedName("metaDataProperty")
    @Expose
    private MetaDataProperty metaDataProperty;
    @SerializedName("featureMember")
    @Expose
    private List<FeatureMember> featureMember = null;

    public MetaDataProperty getMetaDataProperty() {
        return metaDataProperty;
    }

    public void setMetaDataProperty(MetaDataProperty metaDataProperty) {
        this.metaDataProperty = metaDataProperty;
    }

    public List<FeatureMember> getFeatureMember() {
        return featureMember;
    }

    public void setFeatureMember(List<FeatureMember> featureMember) {
        this.featureMember = featureMember;
    }

}
