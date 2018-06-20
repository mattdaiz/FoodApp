package com.foodapp.android.foodapp.model.RecipeSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutritionRestrictions {
    @SerializedName("FAT")
    @Expose
    private FAT fat;

    public FAT getFAT() {
        return fat;
    }

    public void setFAT(FAT fat) {
        this.fat = fat;
    }
}
