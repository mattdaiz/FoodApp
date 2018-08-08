package com.foodapp.android.foodapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.adapter.ShoppingListParentAdapter;
import com.foodapp.android.foodapp.model.ShoppingList.SLResults;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {
    final List<SLResults> resultList = new ArrayList<>();
    ShoppingListParentAdapter adapter;
    RecyclerView shoppingListRecyclerView;
    ProgressBar loadBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ShoppingListParentAdapter(resultList, getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        //resultsTextView = (TextView) rootView.findViewById(R.id.results_text);
        loadBar = (ProgressBar) rootView.findViewById(R.id.progressBar_load);


        shoppingListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_shoppingList);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        shoppingListRecyclerView.setLayoutManager(linearLayout);


        loadBar.setVisibility(View.VISIBLE);
        // Parse through database and pass data to adapter
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                resultList.clear();
                if (e == null) {
                    for (ParseObject object : objects) {
                        List<String> tempList = object.getList("ingredientList");
                        if (tempList.size() == 0) {
                            continue;
                        }
                        SLResults result = new SLResults(object.getString("recipePhoto"), object.getString("recipeName"), object.getString("recipeId"), tempList);
                        resultList.add(result);
                    }
                }
                loadBar.setVisibility(View.INVISIBLE);
                shoppingListRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //noResults(resultsTextView, resultList);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Parse through database and pass data to adapter
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                resultList.clear();
                if (e == null) {
                    for (ParseObject object : objects) {
                        List<String> tempList = object.getList("ingredientList");
                        if (tempList.size() == 0) {
                            continue;
                        }
                        SLResults result = new SLResults(object.getString("recipePhoto"), object.getString("recipeName"), object.getString("recipeId"), tempList);
                        resultList.add(result);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


}
