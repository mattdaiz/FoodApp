package com.foodapp.android.foodapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.activity.MainActivity;
import com.foodapp.android.foodapp.adapter.IngredientSearchAdapter;
import com.foodapp.android.foodapp.model.RecipeSearch.Match;
import com.foodapp.android.foodapp.model.RecipeSearch.RecipeList;
import com.foodapp.android.foodapp.network.GetRecipeDataService;
import com.foodapp.android.foodapp.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class IngredientSearchFragment extends Fragment implements View.OnClickListener, View.OnKeyListener, View.OnTouchListener {
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
    private static final String TAG = IngredientSearchFragment.class.getSimpleName();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_ingredient_search, container, false);
        backgroundRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.activity_main);
        mSearch = (EditText) rootView.findViewById(R.id.editText_input);
        resultsText = (TextView) rootView.findViewById(R.id.results_text);

        //loadBar = (ProgressBar) rootView.findViewById(R.id.progressBar_load);
        //loadBar.setVisibility(View.INVISIBLE);

        searchButton = (Button) rootView.findViewById(R.id.button_search);
        //searchButton.setOnClickListener(new View.OnClickListener());


//        //when enter on keyboard is pressed, auto search and keyboard hide.
        mSearch.setOnKeyListener(this);

        // Endless Pagination
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_recipe_list);

//        //Code for when keyboard is up and pressed on background, keyboard goes away
        recyclerView.setOnTouchListener(this);
        searchButton.setOnClickListener(this);
        recyclerView.setOnClickListener(this);
        // Setting the RecyclerView in a Grid layout
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy != 0) {
                    // Load more results here
                    //loadBar.setVisibility(View.VISIBLE);
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
                            s = s.toLowerCase();
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
                            //loadBar.setVisibility(View.INVISIBLE);
                        }

                        // 2. Need onFailure
                        @Override
                        public void onFailure(Call<RecipeList> call, Throwable t) {
                            Log.e("OnFailure", "Fail");
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            //loadBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        return rootView;
    }


    //Hides keyboard onClick  press search button
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_search) {
            Log.i("CLICKED","search");
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            onClickSearchRecipe(view);
            //loadBar.setVisibility(View.VISIBLE);
        }
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
                s = s.toLowerCase();
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
                //loadBar.setVisibility(View.INVISIBLE);
            }

            // 2. Need onFailure
            @Override
            public void onFailure(Call<RecipeList> call, Throwable t) {
                Log.e("OnFailure", "Fail");
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                //loadBar.setVisibility(View.INVISIBLE);
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

    //keyboard gone once click enter and also searches
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            onClickSearchRecipe(v);
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }

    //when recycleview is touched, make sure keyboard is gone.
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        return false;
    }
}
