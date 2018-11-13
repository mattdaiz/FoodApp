package com.foodapp.android.foodapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class IngredientSearchFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private IngredientSearchAdapter adapter;
    private RecyclerView recyclerView;
    private EditText mSearch;
    private RelativeLayout backgroundRelativeLayout;
    private Button searchButton;
    private BottomNavigationView navigationBar;
    private TextView resultsText;
    ProgressBar loadBar;
    private RelativeLayout relativeLayout;
    private String searchQuery = "";

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
        //backgroundRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.activity_main);
        //mSearch = (EditText) rootView.findViewById(R.id.editText_input);
        resultsText = (TextView) rootView.findViewById(R.id.results_text);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relativeView);
        //loadBar = (ProgressBar) rootView.findViewById(R.id.progressBar_load);
        //loadBar.setVisibility(View.INVISIBLE);

        searchButton = (Button) rootView.findViewById(R.id.button_search);
        //searchButton.setOnClickListener(new View.OnClickListener());


//        //when enter on keyboard is pressed, auto search and keyboard hide.
//        mSearch.setOnKeyListener(this);

        // Endless Pagination
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_recipe_list);

//        //Code for when keyboard is up and pressed on background, keyboard goes away
        //recyclerView.setOnTouchListener(this);
        relativeLayout.setOnTouchListener(
                new RelativeLayout.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent m) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                        return true;
                    }
                }
        );
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
                    resultPagination += 20;
                    //create string for allowedIngredients
                    String result[] = searchQuery.trim().split("\\s*,\\s*");
                    String urlString = "/v1/api/recipes?_app_id=" + APP_ID + "&_app_key=" + APP_KEY + "&maxResult=20" + "&start=" + resultPagination;
                    //goes through array and append allowedIngredient onto it if matches alphabet
                    int numberOfIngredients = 0;
                    for (String s : result) {
                        numberOfIngredients ++;
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
                    final int finalNumberOfIngredients = numberOfIngredients;
                    call.enqueue(new Callback<RecipeList>() {

                        // 1. Need onResponse
                        @Override
                        public void onResponse(Call<RecipeList> call, Response<RecipeList> response) {
                            UpdateRecipeList(response.body().getMatches(), finalNumberOfIngredients);
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
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            //onClickSearchRecipe(view);
            AddRecipeBox(view);
            //loadBar.setVisibility(View.VISIBLE);
        }
    }

    public void AddRecipeBox(View view){
        // setup the alert builder
        //AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        //builder.setTitle("Add Ingredients");
        final Dialog dialog = new Dialog(view.getContext());
        //builder.setView(LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_recipebox,null));

        dialog.setContentView(R.layout.dialog_recipebox);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ListView mShoppingList;
        final EditText mItemEdit;
        Button mAddButton, mSearchButton;
        final ArrayAdapter<String> mAdapter;
        final ArrayList<String> food = new ArrayList<String>();
        Log.i("Test",searchQuery);
        if (!searchQuery.isEmpty()){
             ArrayList<String> temp = new ArrayList(Arrays.asList(searchQuery.split(",")));
             for (String s: temp){
                 food.add(s);
             }
             Log.i("Test1",food.toString());
        }


        // create and show the alert dialog
        //AlertDialog dialog = builder.create();

        mShoppingList = (ListView) dialog.findViewById(R.id.shopping_listView);
        mItemEdit = (EditText) dialog.findViewById(R.id.item_editText);
        mAddButton = (Button) dialog.findViewById(R.id.add_button);
        mSearchButton = (Button) dialog.findViewById(R.id.search_button);
        mAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,food);
        mShoppingList.setAdapter(mAdapter);

        mItemEdit.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!food.isEmpty()) {
                    Log.d("test1", food.toString());
                    String searchQuery = "";

                    for (String s : food) {
                        searchQuery += s + ",";
                    }
                    searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
                    Log.d("test", searchQuery);
                    dialog.dismiss();
                    onClickSearchRecipe(v, searchQuery);
                }else{
                    CharSequence text = "No Ingredients!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(v.getContext(), text, duration);
                    toast.show();
                }
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString().trim();
                if(item.matches("[a-zA-Z]+")) {
                    //mAdapter.add(item);
                    food.add(item);
                    Log.i("test3",food.toString());
                    mAdapter.notifyDataSetChanged();
                    mShoppingList.setSelection(mAdapter.getCount() - 1);
                    mItemEdit.setText("");
                }else{
                    mItemEdit.setText("");
                }
            }
        });

        mShoppingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.remove(food.get(position));
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //Log.i("test","Closed");
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //Log.i("test","Closed");
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        dialog.show();
        //dialog.getWindow().setLayout(1000,1000);

    }

    public void onClickSearchRecipe(View view, String searchIngredients) {

        //make sure text is blank at beginning
        resultsText.setText("");
        searchQuery = searchIngredients;
        //Log.d("test",searchQuery);
        resultPagination = 0;
        //create string for allowedIngredients
        String result[] = searchQuery.trim().split("\\s*,\\s*");
        String urlString = "/v1/api/recipes?_app_id=" + APP_ID + "&_app_key=" + APP_KEY + "&maxResult=20" + "&start=" + resultPagination;
        //goes through array and append allowedIngredient onto it if matches alphabet
        int numberOfIngredients = 0;
        for (String s : result) {
            numberOfIngredients ++;
            //Log.i("Word",s);
            //matches correctly even if user enters garlic, , cognac
            if (s.matches("[a-zA-Z]+")) {
                //Log.i("DING","DING");
                s = s.toLowerCase();
                urlString = urlString + "&allowedIngredient[]=" + s;
            }
        }
        Log.i("STRING", urlString);

        GetRecipeDataService service = RetrofitInstance.getRetrofitInstance().create(GetRecipeDataService.class);

        /*Call the method with parameter in the interface to get the recipe data*/
        //Call<RecipeList> call = service.searchForRecipe(APP_ID, APP_KEY, "soup", 2, 0);
        //Call<RecipeList> call = service.searchForRecipe(APP_ID, APP_KEY, searchQuery);
        Call<RecipeList> call = service.allowedIngredients(urlString);

        /*Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");


        // Utilize call object via async or sync
        // let's use the Call asynchronously
        final int finalNumberOfIngredients = numberOfIngredients;
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
                generateRecipeList(response.body().getMatches(), finalNumberOfIngredients);
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

    /* Generates List of Recipes when user clicks search */
    private void generateRecipeList(List<Match> recipeDataList, int ingredients) {
        allMatches.clear();
        // Produces numerical score for each recipe
        weightedSearchByIngredients(recipeDataList, ingredients);
        allMatches.addAll(recipeDataList);
        adapter = new IngredientSearchAdapter(allMatches);
        recyclerView.setAdapter(adapter);
        for (Match recipe : recipeDataList){
            System.out.println("Recipe: " + recipe.getRecipeName()
                    + "| Weight: " + recipe.getWeight()
                    + "| Ingredients: " + recipe.getIngredients().size());
        }
    }

    /* Generates more Recipes when user clicks scrolls down scrollView */
    private void UpdateRecipeList(List<Match> recipeDataList, int ingredients) {
        weightedSearchByIngredients(recipeDataList, ingredients);
        List<Match> moreMatches = recipeDataList;
        int curSize = adapter.getItemCount();
        allMatches.addAll(moreMatches);
        adapter.notifyItemRangeInserted(curSize, allMatches.size() - 1);
//        for (Match recipe : allMatches){
//            System.out.println("Recipe: " + recipe.getRecipeName()
//                    + "| Weight: " + recipe.getWeight()
//                    + "| Ingredients: " + recipe.getIngredients().size());
//        }
    }

    /* Search based on the overall ingredients available - Sorted by lower weighted recipes */
    public void weightedSearchByIngredients(List <Match> recipeDataList, int ingredients){
        for (Match recipe : recipeDataList){
            //recipe frequency + rating/10
            recipe.setWeight(((float)ingredients/((float)recipe.getIngredients().size()))+((float)recipe.getRating()/((float)10)));
            System.out.println("Recipe: " + recipe.getRecipeName() + "| Weight: " + recipe.getWeight());
        }
        // Sorts the ArrayList based on weight score
        Collections.sort(recipeDataList, new Comparator<Match>() {
            @Override
            public int compare(Match recipe1, Match recipe2) {
                return Float.compare( recipe2.getWeight(),recipe1.getWeight());
            }
        });
    }

    //keyboard gone once click enter and also searches
//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//            //onClickSearchRecipe(v);
//            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//        }
//        return false;
//    }

    //when recycleview is touched, make sure keyboard is gone.
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        return false;
    }
}
