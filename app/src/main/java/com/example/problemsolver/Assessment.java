
package com.example.problemsolver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Assessment {

    @SerializedName("likesCount")
    @Expose
    private String likesCount;
    @SerializedName("dislikesCount")
    @Expose
    private String dislikesCount;

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(String dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

}
