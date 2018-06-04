package com.foodapp.android.foodapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


// Retrofit turns your HTTP API into a Java interface
// Passes the end point or filename that will provide the JSON data
public interface GetRecipeDataService {
    @GET("/v1/api/recipes")
    Call<RecipeList> searchForRecipe(
            @Query("_app_id") String id
            ,@Query("_app_key") String key
            ,@Query("q")String search
            //,@Query("maxResult") int integerOne
            //,@Query("start") int integerTwo
    );

    /* Implement for RecipeActivity - the recipe details
    @GET("/v1/api/recipe/{recipeid}")
    Call<RecipeDetail> RecipeDetail
            @Path("recipeid") String id
    */
}
