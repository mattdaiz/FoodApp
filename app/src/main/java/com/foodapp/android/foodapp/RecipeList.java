package com.foodapp.android.foodapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipeList {
    @SerializedName("matches")
    private ArrayList<Recipe> recipeList;


    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
}

