package com.foodapp.android.foodapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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



public class MainActivity extends AppCompatActivity {

    private RecipeAdapter adapter;
    private RecyclerView recyclerView;
    private EditText mSearch;


    private final String APP_ID = "c64ff1e0";
    private final String APP_KEY = "0e7ff170e9c952c81bf4bf7b2fb0988c";
    private static final String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearch = (EditText) findViewById(R.id.editText_input);
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


