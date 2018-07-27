package com.foodapp.android.foodapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.adapter.IngredientListAdapter;
import com.foodapp.android.foodapp.model.RecipeDetails.Image;
import com.foodapp.android.foodapp.model.RecipeDetails.RecipeInfo;
import com.foodapp.android.foodapp.network.GetRecipeDataService;
import com.foodapp.android.foodapp.network.RetrofitInstance;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity implements View.OnClickListener {

    private IngredientListAdapter adapter;
    private RecyclerView recyclerView;
    private String prep_url;
    private String photo_url;
    private String recipeName;

    TextView txtRecipeName, txtPrepTime, txtRecipeYield;
    ImageView imgFood;
    Button btnPreparation;
    SimpleRatingBar ratingBar;
    ToggleButton favouriteRecipe;
    LikeButton likeButton;


    private final String APP_ID = "c64ff1e0";
    private final String APP_KEY = "0e7ff170e9c952c81bf4bf7b2fb0988c";
    private static final String TAG = IngredientSearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        String value = getIntent().getStringExtra("recipeId");


        imgFood = (ImageView) findViewById(R.id.imageView_Recipe_image);
        txtRecipeName = (TextView) findViewById(R.id.textView_name);
        txtPrepTime = (TextView) findViewById(R.id.textView_prepTime);
        txtRecipeYield = (TextView) findViewById(R.id.textView_yield);
        ratingBar = findViewById(R.id.ratingBar_recipe);
        btnPreparation = (Button) findViewById(R.id.button_prep_url);
        btnPreparation.setOnClickListener(this);


        //******************************************************************************************
        likeButton = findViewById(R.id.star_button);

        Log.i("User logged in", ParseUser.getCurrentUser().getUsername());
        // Checks if user has previously favourite a recipe

        // Query to search in the database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Intent intent = getIntent();
                if (e == null){
                    if (objects.size() > 0){
                        Log.i("size",String.valueOf(objects.size()));
                        // Iterate through all the user's favourite recipe's
                        for (ParseObject object : objects){
                            if (object.getString("recipeId").equals(intent.getStringExtra("recipeId")) && object.getString("username").equals(ParseUser.getCurrentUser().getUsername().toString())) {
                                Log.i("Recipe in Database", "YES");
                                likeButton.setLiked(true);
                                break;
                            } else{
                                //Log.i("Recipe in Database", "NO");
                                likeButton.setLiked(false);
                            }
                        }
                    }
                }
            }
        });


        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ParseObject favourite = new ParseObject("Favourite");
                favourite.put("username", ParseUser.getCurrentUser().getUsername());
                Intent intent = getIntent();
                favourite.put("recipeName", recipeName);
                favourite.put("recipeId", intent.getStringExtra("recipeId"));
                favourite.put("recipePhoto", photo_url);
                Log.i("photoURL", photo_url);
                favourite.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Log.i("Recipe Result", "SAVED");
                        }else{
                            Log.i("Recipe Result", "ERROR");
                        }
                    }
                });

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                // Query to search in the database
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                Intent intent = getIntent();
                query.whereEqualTo("recipeId", intent.getStringExtra("recipeId"));
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null){
                            object.deleteInBackground();
                            Log.i("Recipe Result", "DELETED");
                        }
                    }
                });
            }
        });

        //******************************************************************************************

        /*Call the method with parameter in the interface to get the recipe data*/
        //Creating a retrofit object
        //creating the api interface
        GetRecipeDataService service = RetrofitInstance.getRetrofitInstance().create(GetRecipeDataService.class);
        Call<RecipeInfo> call = service.recipeDetail(value, APP_ID, APP_KEY);

        /*Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        // Utilize call object via async or sync
        // let's use the Call asynchronously
        call.enqueue(new Callback<RecipeInfo>() {

            // 1. Need onResponse
            @Override
            public void onResponse(Call<RecipeInfo> call, Response<RecipeInfo> response) {
                List<Image> Images = (response.body()).getImages();
                String url = Images.get(0).getImageUrlsBySize().get360();
                photo_url = url;
                Picasso.get().load(url).into(imgFood);
                ratingBar.setRating(response.body().getRating());
                txtRecipeName.setText(response.body().getName());
                recipeName = response.body().getName();
                txtPrepTime.setText(getString(R.string.prepTime, response.body().getTotalTime()));
                txtRecipeYield.setText(getString(R.string.yield, response.body().getNumberOfServings()));
                generateRecipeInfo(response.body().getIngredientLines());
                prep_url = (response.body().getSource().getSourceRecipeUrl());
            }

            // 2. Need onFailure
            @Override
            public void onFailure(Call<RecipeInfo> call, Throwable t) {
                Log.e("OnFailure", "Fail in RecipeActivity");
                Toast.makeText(RecipeActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*Method to generate recipe info using RecyclerView with custom adapter*/
    private void generateRecipeInfo(List<String> recipeIngredientList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ingredients);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new IngredientListAdapter(RecipeActivity.this, recipeIngredientList);
        recyclerView.setAdapter(adapter);
    }

    public void onClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(prep_url));
        startActivity(browserIntent);
    }
}
