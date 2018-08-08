package com.foodapp.android.foodapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.activity.RecipeActivity;
import com.foodapp.android.foodapp.model.FavouriteUser.Results;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.RecipeViewHolder> {
    private List<Results> ParseList;

    public FavouriteAdapter( List<Results> ParseList){
        this.ParseList = ParseList;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_favourite, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Picasso.get()
                .load(ParseList.get(position).getRecipePhoto())
                .placeholder(R.drawable.card_gradient)
                .into(holder.recipePhoto);
        holder.recipeLabel.setText(ParseList.get(position).getRecipeName());
    }

    @Override
    public int getItemCount() {
        return ParseList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView recipePhoto;
        TextView recipeLabel;

        RecipeViewHolder(View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.custom_favourite_cardView);
            recipePhoto = (ImageView)itemView.findViewById(R.id.recipe_photo);
            recipeLabel = (TextView)itemView.findViewById(R.id.recipe_name);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RecipeActivity.class);
                    //shared element transition
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) v.getContext(),(View)recipePhoto,"myImage");
                    intent.putExtra("recipeId", ParseList.get(getAdapterPosition()).getRecipeId());
                    v.getContext().startActivity(intent,optionsCompat.toBundle());
                }
            });
        }
    }
}
