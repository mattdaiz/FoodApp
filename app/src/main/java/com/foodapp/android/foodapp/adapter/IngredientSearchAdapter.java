package com.foodapp.android.foodapp.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.activity.RecipeActivity;
import com.foodapp.android.foodapp.model.RecipeSearch.Match;
import com.squareup.picasso.Picasso;

import java.util.List;


public class IngredientSearchAdapter extends RecyclerView.Adapter<IngredientSearchAdapter.RecipeViewHolder> {
    private List<Match> dataList;
    private static final String TAG = IngredientSearchAdapter.class.getSimpleName();

    public IngredientSearchAdapter(List<Match> dataList) {
        this.dataList = dataList;
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView txtRecipeLabel, txtRecipeIngredients, txtRecipeRating;
        private ImageView imgFood;

        RecipeViewHolder(final View itemView) {
            super(itemView);
            imgFood = (ImageView) itemView.findViewById(R.id.image_food);
            txtRecipeLabel = (TextView) itemView.findViewById(R.id.txt_recipe_label);
            //txtRecipeIngredients = (TextView) itemView.findViewById(R.id.txt_recipe_ingredients);
            txtRecipeRating = (TextView) itemView.findViewById(R.id.txt_recipe_rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RecipeActivity.class);
                    intent.putExtra("recipeId", dataList.get(getAdapterPosition()).getId());
                    //shared element transition
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) v.getContext(),(View)imgFood,"myImage");
                    v.getContext().startActivity(intent,optionsCompat.toBundle());
                }
            });
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_ingredient_search, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.txtRecipeLabel.setText(dataList.get(position).getRecipeName());
        //holder.txtRecipeIngredients.setText(dataList.get(position).getIngredients().toString());
        holder.txtRecipeRating.setText("Rating: " + dataList.get(position).getRating());
        //String pictureUrl = dataList.get(position).getImageUrlsBySize().get90();
        Picasso.get().load(dataList.get(position).getImageUrlsBySize().get90().replace("=s90", "=s360")).into(holder.imgFood);
        //Log.d("TAG", "Position: " + position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}