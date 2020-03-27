
package com.example.problemsolver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyPoint {


    @SerializedName("pos")
    @Expose
    private static String pos;

    public String getPos() {
        return pos;
    }


}
