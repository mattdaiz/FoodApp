package com.foodapp.android.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.adapter.IngredientSearchAdapter;
import com.foodapp.android.foodapp.model.RecipeSearch.Match;
import com.foodapp.android.foodapp.model.RecipeSearch.RecipeList;
import com.foodapp.android.foodapp.network.GetRecipeDataService;
import com.foodapp.android.foodapp.network.RetrofitInstance;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IngredientSearchActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private IngredientSearchAdapter adapter;
    private RecyclerView recyclerView;
    private EditText mSearch;
    private RelativeLayout backgroundRelativeLayout;
    private Button searchButton;
    private BottomNavigationView navigationBar;
    private TextView resultsText;
    ProgressBar loadBar;

    private List<Match> allMatches = new ArrayList<>();
    private int resultPagination;

    private final String APP_ID = "c64ff1e0";
    private final String APP_KEY = "0e7ff170e9c952c81bf4bf7b2fb0988c";
    private static final String TAG = IngredientSearchActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //print username of user
        //Log.i("USER", ParseUser.getCurrentUser().getUsername().toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_search);
        backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        mSearch = (EditText) findViewById(R.id.editText_input);
        searchButton = (Button) findViewById(R.id.button_search);
        navigationBar = (BottomNavigationView) findViewById(R.id.navigationbar);
        resultsText = (TextView) findViewById(R.id.results_text);
        loadBar = (ProgressBar) findViewById(R.id.progressBar_load);
        loadBar.setVisibility(View.INVISIBLE);

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
                        Intent i = new Intent(IngredientSearchActivity.this, FavouriteActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        //finish();
                        return true;
                    case R.id.logout:
                        ParseUser.logOut();
                        Intent j = new Intent(IngredientSearchActivity.this, LoginActivity.class);
                        j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SharedPreference.clearUserName(getApplicationContext());
                        startActivity(j);
                        finish();
                        return true;
                }
                return true;
            }
        });


        // Endless Pagination
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_recipe_list);
        // Setting the RecyclerView in a Grid layout
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy != 0) {
                    // Load more results here
                    loadBar.setVisibility(View.VISIBLE);
                    resultPagination += 10;
                    String searchQuery = mSearch.getText().toString();
                    //create string for allowedIngredients
                    String result[] = searchQuery.trim().split("\\s*,\\s*");
                    String urlString = "/v1/api/recipes?_app_id=" + APP_ID + "&_app_key=" + APP_KEY + "&maxResult=10" + "&start=" + resultPagination;
                    //goes through array and append allowedIngredient onto it if matches alphabet
                    for (String s : result) {
                        //Log.i("Word",s);
                        //matches correctly even if user enters garlic, , cognac
                        if (s.matches("[a-zA-Z]+")) {
                            //Log.i("DING","DING");
                            urlString = urlString + "&allowedIngredient[]=" + s;
                        }
                    }
                    //Log.i("STRING", urlString);

                    GetRecipeDataService service = RetrofitInstance.getRetrofitInstance().create(GetRecipeDataService.class);

                    /*Call the method with parameter in the interface to get the recipe data*/
                    //Call<RecipeList> call = service.searchForRecipe(APP_ID, APP_KEY, "soup", 2, 0);
                    //Call<RecipeList> call = service.searchForRecipe(APP_ID, APP_KEY, searchQuery);
                    Call<RecipeList> call = service.allowedIngredients(urlString);

                    /*Log the URL called*/
                    Log.wtf("URL Called", call.request().url() + "");

                    // Utilize call object via async or sync
                    // let's use the Call asynchronously
                    call.enqueue(new Callback<RecipeList>() {

                        // 1. Need onResponse
                        @Override
                        public void onResponse(Call<RecipeList> call, Response<RecipeList> response) {
                            UpdateRecipeList(response.body().getMatches());
                            loadBar.setVisibility(View.INVISIBLE);
                        }

                        // 2. Need onFailure
                        @Override
                        public void onFailure(Call<RecipeList> call, Throwable t) {
                            Log.e("OnFailure", "Fail");
                            Toast.makeText(IngredientSearchActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            loadBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }

    //Hides keyboard onClick of the background or press search button
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_main || view.getId() == R.id.recycler_view_recipe_list) {
            //Log.i("CLICKED","CKLIED");
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } else if (view.getId() == R.id.button_search) {
            //Log.i("CLICKED","search");
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            onClickSearchRecipe(view);
            loadBar.setVisibility(View.VISIBLE);
        }
    }

    //keyboard gone once click enter and also searches
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            onClickSearchRecipe(view);
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }

    public void onClickSearchRecipe(View view) {
        //make sure text is blank at beginning
        resultsText.setText("");
        String searchQuery = mSearch.getText().toString();
        resultPagination = 0;
        //create string for allowedIngredients
        String result[] = searchQuery.trim().split("\\s*,\\s*");
        String urlString = "/v1/api/recipes?_app_id=" + APP_ID + "&_app_key=" + APP_KEY + "&maxResult=10" + "&start=" + resultPagination;
        //goes through array and append allowedIngredient onto it if matches alphabet
        for (String s : result) {
            //Log.i("Word",s);
            //matches correctly even if user enters garlic, , cognac
            if (s.matches("[a-zA-Z]+")) {
                //Log.i("DING","DING");
                urlString = urlString + "&allowedIngredient[]=" + s;
            }
        }
        //Log.i("STRING", urlString);

        GetRecipeDataService service = RetrofitInstance.getRetrofitInstance().create(GetRecipeDataService.class);

        /*Call the method with parameter in the interface to get the recipe data*/
        //Call<RecipeList> call = service.searchForRecipe(APP_ID, APP_KEY, "soup", 2, 0);
        //Call<RecipeList> call = service.searchForRecipe(APP_ID, APP_KEY, searchQuery);
        Call<RecipeList> call = service.allowedIngredients(urlString);

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
                //check if results are empty.
                if (response.body().getMatches().isEmpty()){
                    resultsText.setText("No Results");
                }
                generateRecipeList(response.body().getMatches());
                loadBar.setVisibility(View.INVISIBLE);
            }

            // 2. Need onFailure
            @Override
            public void onFailure(Call<RecipeList> call, Throwable t) {
                Log.e("OnFailure", "Fail");
                Toast.makeText(IngredientSearchActivity.this, "Error", Toast.LENGTH_SHORT).show();
                loadBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    /*Method to generate List of recipes using RecyclerView with custom adapter*/
    private void generateRecipeList(List<Match> recipeDataList) {
        allMatches.clear();
        allMatches.addAll(recipeDataList);
        adapter = new IngredientSearchAdapter(allMatches);
        recyclerView.setAdapter(adapter);
    }

    /*Method to generate List of recipes using RecyclerView with custom adapter*/
    private void UpdateRecipeList(List<Match> recipeDataList) {
        List<Match> moreMatches = recipeDataList;
        int curSize = adapter.getItemCount();
        allMatches.addAll(moreMatches);
        adapter.notifyItemRangeInserted(curSize, allMatches.size() - 1);
    }


}


