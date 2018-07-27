package com.foodapp.android.foodapp.model.FavouriteUser;

public class Results {

    private String recipeName;

    private String username;

    private String recipeId;

    private String objectId;

    private String recipePhoto;

    public Results (String recipePhoto, String recipeName, String recipeId){
        this.recipePhoto = recipePhoto;
        this.recipeName = recipeName;
        this.recipeId = recipeId;
    }

    public String getRecipeName ()
    {
        return recipeName;
    }

    public void setRecipeName (String recipeName)
    {
        this.recipeName = recipeName;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getRecipeId ()
    {
        return recipeId;
    }

    public void setRecipeId (String recipeId)
    {
        this.recipeId = recipeId;
    }

    public String getObjectId ()
    {
        return objectId;
    }

    public void setObjectId (String objectId)
    {
        this.objectId = objectId;
    }

    public String getRecipePhoto ()
    {
        return recipePhoto;
    }

    public void setRecipePhoto (String recipePhoto)
    {
        this.recipePhoto = recipePhoto;
    }
}
