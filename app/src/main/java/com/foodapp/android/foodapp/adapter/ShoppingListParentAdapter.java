package com.foodapp.android.foodapp.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.model.ShoppingList.SLResults;
import com.squareup.picasso.Picasso;

import java.util.List;

// Adapter for the Recipe Image/Label
public class ShoppingListParentAdapter extends RecyclerView.Adapter<ShoppingListParentAdapter.ListViewHolder> {
    private List<SLResults> ParseList;
    private Activity activity;
    public ShoppingListParentAdapter(List<SLResults> ParseList, Activity activity){
        this.ParseList = ParseList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ShoppingListParentAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_shopping_list_parent, parent, false);
        return new ShoppingListParentAdapter.ListViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Picasso.get()
                .load(ParseList.get(position).getRecipePhoto())
                .into(holder.recipePhoto);
        holder.recipeLabel.setText(ParseList.get(position).getRecipeName());

        if (ParseList.get(position).getIngredientList().size() > 0){

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        holder.ingredientRecyclerView.setLayoutManager(layoutManager);
        ShoppingListChildAdapter adapter = new ShoppingListChildAdapter(ParseList.get(position).getIngredientList(),activity);
        holder.ingredientRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return ParseList.size();
    }


    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView recipePhoto;
        TextView recipeLabel;
        RecyclerView ingredientRecyclerView;

        ListViewHolder(View itemView){
            super(itemView);
            recipePhoto = (ImageView)itemView.findViewById(R.id.list_image);
            recipeLabel = (TextView)itemView.findViewById(R.id.list_title);
            ingredientRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_ingredients_list);
        }
    }
}
