package com.foodapp.android.foodapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.model.FavouriteUser.Results;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeShareAdapter extends RecyclerView.Adapter<RecipeShareAdapter.ViewHolder> {
    private List<Results> results;
    private Context mContext;

    public RecipeShareAdapter(Context context, List<Results> results) {
        mContext = context;
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.custom_dialog_sharerecipe, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get().load(results.get(position).getRecipePhoto()).into(holder.recipePhoto);
        holder.recipeLabel.setText(results.get(position).getRecipeName());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView recipePhoto;
        TextView recipeLabel;
        RecyclerView ingredientRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            recipePhoto = (ImageView) itemView.findViewById(R.id.list_image_shareRecipe);
            recipeLabel = (TextView) itemView.findViewById(R.id.list_title_shareRecipe);
        }
    }
}