package com.foodapp.android.foodapp.model.RecipeDetails;

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class Recipe {
    // fields must have same names as fields in JSON. watch out!
    @SerializedName("recipeName")
    private String recipeName;
    @SerializedName("id")
    private String id;
    @SerializedName("imageUrlsBySize")
    private Map<String,String> Url;
    @SerializedName("ingredients")
    private ArrayList<String> ingredients;
    @SerializedName("rating")
    private String rating;

    public String getRecipeName() {
        return recipeName;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return Url.get("90");
    }

    public String getIngredients() {
        return ingredients.toString();
    }

    public String getRating() {
        return rating;
    }
}
