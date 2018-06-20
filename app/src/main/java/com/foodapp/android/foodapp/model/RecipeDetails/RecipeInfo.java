package com.foodapp.android.foodapp.model.RecipeDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RecipeInfo {
    @SerializedName("yield")
    @Expose
    private String yield;
    @SerializedName("nutritionEstimates")
    @Expose
    private List<Object> nutritionEstimates = null;
    @SerializedName("totalTime")
    @Expose
    private String totalTime;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ingredientLines")
    @Expose
    private List<String> ingredientLines = null;
    @SerializedName("attribution")
    @Expose
    private Attribution attribution;
    @SerializedName("numberOfServings")
    @Expose
    private Integer numberOfServings;
    @SerializedName("totalTimeInSeconds")
    @Expose
    private Integer totalTimeInSeconds;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("flavors")
    @Expose
    private Flavors flavors;
    @SerializedName("rating")
    @Expose
    private Integer rating;

    public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }

    public List<Object> getNutritionEstimates() {
        return nutritionEstimates;
    }

    public void setNutritionEstimates(List<Object> nutritionEstimates) {
        this.nutritionEstimates = nutritionEstimates;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public Attribution getAttribution() {
        return attribution;
    }

    public void setAttribution(Attribution attribution) {
        this.attribution = attribution;
    }

    public Integer getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public Integer getTotalTimeInSeconds() {
        return totalTimeInSeconds;
    }

    public void setTotalTimeInSeconds(Integer totalTimeInSeconds) {
        this.totalTimeInSeconds = totalTimeInSeconds;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Flavors getFlavors() {
        return flavors;
    }

    public void setFlavors(Flavors flavors) {
        this.flavors = flavors;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
