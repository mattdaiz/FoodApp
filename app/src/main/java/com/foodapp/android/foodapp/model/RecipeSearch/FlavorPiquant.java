package com.foodapp.android.foodapp.model.RecipeSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlavorPiquant {
    @SerializedName("min")
    @Expose
    private Double min;
    @SerializedName("max")
    @Expose
    private Integer max;

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
