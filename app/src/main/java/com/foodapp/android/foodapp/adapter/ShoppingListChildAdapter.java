package com.foodapp.android.foodapp.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;

import java.util.List;

// Adapter for the Recipe individual ingredients
public class ShoppingListChildAdapter extends RecyclerView.Adapter<ShoppingListChildAdapter.IngredientViewHolder> {
    private List<String> ingredients;
    private Activity activity;

    public ShoppingListChildAdapter(List<String> ingredients, Activity activity) {
        this.ingredients = ingredients;
        this.activity = activity;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_shopping_list_child, parent, false);
        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        if (ingredients.get(position).length() > 0) {
            holder.label.setText(ingredients.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView label;

        IngredientViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.textView_ingredient_list);
        }
    }
}
