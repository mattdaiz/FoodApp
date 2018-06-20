package com.foodapp.android.foodapp.model.RecipeDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("hostedSmallUrl")
    @Expose
    private String hostedSmallUrl;
    @SerializedName("hostedMediumUrl")
    @Expose
    private String hostedMediumUrl;
    @SerializedName("hostedLargeUrl")
    @Expose
    private String hostedLargeUrl;
    @SerializedName("imageUrlsBySize")
    @Expose
    private ImageUrlsBySize imageUrlsBySize;

    public String getHostedSmallUrl() {
        return hostedSmallUrl;
    }

    public void setHostedSmallUrl(String hostedSmallUrl) {
        this.hostedSmallUrl = hostedSmallUrl;
    }

    public String getHostedMediumUrl() {
        return hostedMediumUrl;
    }

    public void setHostedMediumUrl(String hostedMediumUrl) {
        this.hostedMediumUrl = hostedMediumUrl;
    }

    public String getHostedLargeUrl() {
        return hostedLargeUrl;
    }

    public void setHostedLargeUrl(String hostedLargeUrl) {
        this.hostedLargeUrl = hostedLargeUrl;
    }

    public ImageUrlsBySize getImageUrlsBySize() {
        return imageUrlsBySize;
    }

    public void setImageUrlsBySize(ImageUrlsBySize imageUrlsBySize) {
        this.imageUrlsBySize = imageUrlsBySize;
    }
}
