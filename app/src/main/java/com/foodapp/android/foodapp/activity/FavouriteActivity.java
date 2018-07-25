package com.foodapp.android.foodapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.foodapp.android.foodapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private BottomNavigationView navigationBar;
    private ArrayList<String> recipeList;


    public static void getFavouriteRecipeID(final ArrayList<String> list){
        //get favourite recipeIDs into list
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        //Log.i("RECIPE",object.getString("recipeId"));
                        list.add(object.getString("recipeId"));
                    }
                    Log.i("Recipe", Arrays.toString(list.toArray()));
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        navigationBar = (BottomNavigationView) findViewById(R.id.navigationbar);

        //populate recipeList with the favourited recipeIDs
        recipeList = new ArrayList<String>();
        getFavouriteRecipeID(recipeList);







        //set up for navigation bar
        navigationBar.setItemIconTintList(null);
        navigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        Intent i = new Intent(FavouriteActivity.this, IngredientSearchActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        //finish();
                        return true;
                    case R.id.favourites:
                        return true;
                    case R.id.logout:
                        Intent j = new Intent(FavouriteActivity.this, LoginActivity.class);
                        ParseUser.logOut();
                        SharedPreference.clearUserName(getApplicationContext());
                        startActivity(j);
                        finish();
                        return true;
                }
                return true;
            }
        });
    }
}
