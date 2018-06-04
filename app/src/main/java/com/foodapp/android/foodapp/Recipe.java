package com.foodapp.android.foodapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {
    // fields must have same names as fields in JSON. watch out!
    @SerializedName("recipeName")
    @Expose
    private String recipeName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("imageUrlsBySize")
    private ImageUrl Url;
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

    public ImageUrl getUrl() {
        return this.Url;
    }

    public String getIngredients() {
        return ingredients.toString();
    }

    public String getRating() {
        return rating;
    }


    // Sub class for Picture Url
    class ImageUrl{
        @SerializedName("90")
        private String Url;

        public String getImageUrl() {
            return Url;
        }
    }
}
