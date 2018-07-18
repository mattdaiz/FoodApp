package com.foodapp.android.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foodapp.android.foodapp.model.RecipeSearch.Match;
import com.foodapp.android.foodapp.network.GetRecipeDataService;
import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.model.RecipeDetails.Recipe;
import com.foodapp.android.foodapp.adapter.RecipeAdapter;
import com.foodapp.android.foodapp.model.RecipeSearch.RecipeList;
import com.foodapp.android.foodapp.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    private RecipeAdapter adapter;
    private RecyclerView recyclerView;
    private EditText mSearch;
    private RelativeLayout backgroundRelativeLayout;
    private Button searchButton;
    private BottomNavigationView navigationBar;

    private final String APP_ID = "c64ff1e0";
    private final String APP_KEY = "0e7ff170e9c952c81bf4bf7b2fb0988c";
    private static final String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        mSearch = (EditText) findViewById(R.id.editText_input);
        searchButton = (Button) findViewById(R.id.button_search);
        navigationBar = (BottomNavigationView) findViewById(R.id.navigationbar);

        //Code for when keyboard is up and pressed on background, keyboard goes away
        backgroundRelativeLayout.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        //when enter on keyboard is pressed, auto search and keyboard hide.
        mSearch.setOnKeyListener(this);

        //set up navigation bar
        navigationBar.setItemIconTintList(null);
        navigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //switch case for whichever is pressed
                switch (item.getItemId()) {
                    case R.id.search:
                        return true;
                    case R.id.favourites:
                        Intent i = new Intent(MainActivity.this, FavouriteActivity.class);
                        startActivity(i);
                        return true;
                }
                return true;
            }
        });
    }

    //Hides keyboard onClick of the background or press search button
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_main){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }else if (view.getId() == R.id.button_search){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            onClickSearchRecipe(view);
        }
    }

    //keyboard gone once click enter and also searches
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            onClickSearchRecipe(view);
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        return false;
    }

    /*Method to generate List of recipes using RecyclerView with custom adapter*/
    private void generateRecipeList(List<Match> recipeDataList){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_recipe_list);
        adapter = new RecipeAdapter(recipeDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onClickSearchRecipe(View view) {
        String searchQuery = mSearch.getText().toString();


        GetRecipeDataService service = RetrofitInstance.getRetrofitInstance().create(GetRecipeDataService.class);

        /*Call the method with parameter in the interface to get the recipe data*/
        //Call<RecipeList> call = service.searchForRecipe(APP_ID, APP_KEY, "soup", 2, 0);
        Call<RecipeList> call = service.searchForRecipe(APP_ID, APP_KEY, searchQuery);

        /*Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");


        // Utilize call object via async or sync
        // let's use the Call asynchronously
        call.enqueue(new Callback<RecipeList>() {

            // 1. Need onResponse
            @Override
            public void onResponse(Call<RecipeList> call, Response<RecipeList> response) {
//                Log.d(TAG, "Total number of questions fetched : " + response.body());
//                Log.e("OnResponse", "OK");
//                Log.d("TEST", response.message());
//                Log.d("TEST2", "TESTTEST" + response.body());
                generateRecipeList(response.body().getMatches());
            }

            // 2. Need onFailure
            @Override
            public void onFailure(Call<RecipeList> call, Throwable t) {
                Log.e("OnFailure", "Fail");
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}


