package com.foodapp.android.foodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.foodapp.android.foodapp.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder> {
    private Context context;
    private List<String> ingredientList;
    private Intent ingredientIntent;

    public IngredientListAdapter(Context context, List<String> ingredientList, Intent intent) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.ingredientIntent = intent;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row_recipe_ingredient_list, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.txtIngredient.setText(ingredientList.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView txtIngredient;
        LikeButton ingredientSave;
        List<String> ingredientList;
        boolean result = false;

        IngredientViewHolder(final View itemView) {
            super(itemView);

            txtIngredient = (TextView) itemView.findViewById(R.id.textView_ingredient);
            ingredientSave = (LikeButton) itemView.findViewById(R.id.ingredientSave_button);
            // Check if ingredient is already saved in database
            // Set the add/remove button
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo("recipeId", ingredientIntent.getStringExtra("recipeId"));
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        // Create new ingredient list
                        if (object.getList("ingredientList") != null) {
                            ingredientList = object.getList("ingredientList");
                            for (String ingredient : ingredientList) {
                                if (ingredient.equals(txtIngredient.getText().toString())) {
                                    ingredientSave.setLiked(true);
                                    break;
                                }
                            }
                        } else {
                            ingredientList = new ArrayList<>();
                        }
                    }
                }
            });

            // Listener to check if user adds/removes an ingredient
            ingredientSave.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    //Log.i("ADDED", "Ingredient: " + txtIngredient.getText());
                    //Log.i("LIKED", "Position: " + getAdapterPosition());
                    //Log.i("RecipeId", ingredientIntent.getStringExtra("recipeId"));
                    //Log.i("User", ParseUser.getCurrentUser().getUsername());
                    saveIngredient(txtIngredient.getText().toString());
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    //Log.i("REMOVED", "Ingredient: " + txtIngredient.getText());
                    //Log.i("UNLIKE", "Position: " + getAdapterPosition());
                    //Log.i("RecipeId", ingredientIntent.getStringExtra("recipeId"));
                    //Log.i("User", ParseUser.getCurrentUser().getUsername());
                    removeIngredient(txtIngredient.getText().toString());
                }
            });
        }

        // Add specific ingredient from database
        // Recipe must be favourite before saving ingredients
        public void saveIngredient(final String ingredientToAdd) {
            // Query to search in the database
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo("recipeId", ingredientIntent.getStringExtra("recipeId"));
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    // If recipe is in database
                    if (e == null) {
                        // Create new ingredient list
                        List<String> ingredientList = new ArrayList<>();
                        // Check if ingredient list exists in database
                        List<String> tempList;
                        if (object.getList("ingredientList") == null) {
                            tempList = new ArrayList<>();
                        } else {
                            tempList = object.getList("ingredientList");
                        }
                        // Append old ingredients to new ingredient list
                        ingredientList.addAll(tempList);
                        // Append new ingredient to new ingredient list
                        ingredientList.add(ingredientToAdd);
                        object.put("ingredientList", ingredientList);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i("Ingredient List", "SAVED");
                            }
                        });
                    }
                    // If recipe has not been favourite, does not allow user to save ingredients
                    else {
                        ingredientSave.setLiked(false);
                        Toast.makeText(itemView.getContext(), "Must Save Recipe First", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Remove specific ingredient from database
        public void removeIngredient(final String ingredientToRemove) {
            // Query to search in the database
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo("recipeId", ingredientIntent.getStringExtra("recipeId"));
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        // Create new ingredient list
                        List<String> ingredientList = object.getList("ingredientList");
                        // Find ingredient to delete
                        ingredientList.remove(ingredientToRemove);
                        // Update ingredient list in database
                        object.put("ingredientList", ingredientList);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i("Ingredient List", "DELETED");
                            }
                        });
                    }
                }
            });
        }
    }
}
