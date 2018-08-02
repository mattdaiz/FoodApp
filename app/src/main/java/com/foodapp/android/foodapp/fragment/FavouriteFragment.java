package com.foodapp.android.foodapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.activity.MainActivity;
import com.foodapp.android.foodapp.adapter.FavouriteAdapter;
import com.foodapp.android.foodapp.model.FavouriteUser.Results;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class FavouriteFragment extends Fragment {
    private BottomNavigationView navigationBar;
    private ArrayList<String> recipeList;
    private TextView resultsTextView;
    RecyclerView favouriteRecyclerView;
    FavouriteAdapter adapter;
    ProgressBar loadBar;

    final List<Results> resultList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FavouriteAdapter(resultList);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);

        resultsTextView = (TextView) rootView.findViewById(R.id.results_text);
        loadBar = (ProgressBar) rootView.findViewById(R.id.progressBar_load);


        favouriteRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_favourite);
        LinearLayoutManager linearlayout = new LinearLayoutManager(getActivity());
        favouriteRecyclerView.setLayoutManager(linearlayout);

        //update bool to show in frag1
        MainActivity.isFragment1Shown=false;
        MainActivity.isFragment2Shown=true;

        loadBar.setVisibility(View.VISIBLE);
        // Parse through database and pass data to adapter
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                resultList.clear();
                int count = 0;
                if (e == null) {
                    for (ParseObject object : objects) {
                        Results result = new Results(object.getString("recipePhoto"), object.getString("recipeName"), object.getString("recipeId"));
                        resultList.add(result);
                    }
                    Log.i("Count", Integer.toString(count));
                }
                loadBar.setVisibility(View.INVISIBLE);
                favouriteRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                noResults(resultsTextView, resultList);
            }
        }); return rootView;
    }


    //displays noResults if no favourites
    public static void noResults(TextView resultsText, List<Results> list) {
        if (list.isEmpty()) {
            resultsText.setText("No Results");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //loadBar.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                resultList.clear();
                int count = 0;
                if (e == null) {
                    for (ParseObject object : objects) {
                        Results result = new Results(object.getString("recipePhoto"), object.getString("recipeName"), object.getString("recipeId"));
                        resultList.add(result);
                    }
                    Log.i("Count", Integer.toString(count));
                }
                //loadBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
