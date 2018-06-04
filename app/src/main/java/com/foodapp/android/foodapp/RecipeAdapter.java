package com.foodapp.android.foodapp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodapp.android.foodapp.activity.MainActivity;
import com.foodapp.android.foodapp.activity.RecipeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> dataList;
    private static final String TAG = RecipeAdapter.class.getSimpleName();

    public RecipeAdapter(ArrayList<Recipe> dataList){
        this.dataList= dataList;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView txtRecipeLabel, txtRecipeIngredients, txtRecipeRating;
        ImageView imgFood;
        private String mItem;

        RecipeViewHolder (final View itemView){
            super(itemView);
            imgFood = (ImageView) itemView.findViewById(R.id.image_food);
            txtRecipeLabel = (TextView) itemView.findViewById(R.id.txt_recipe_label);
            txtRecipeIngredients = (TextView) itemView.findViewById(R.id.txt_recipe_ingredients);
            txtRecipeRating = (TextView) itemView.findViewById(R.id.txt_recipe_rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Position:" + Integer.toString(getLayoutPosition()), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), RecipeActivity.class);
                    intent.putExtra("recipeId",dataList.get(getAdapterPosition()).getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position){
        holder.txtRecipeLabel.setText(dataList.get(position).getRecipeName());
        holder.txtRecipeIngredients.setText(dataList.get(position).getIngredients());
        holder.txtRecipeRating.setText("Rating: " + dataList.get(position).getRating());
        Picasso.get().load(dataList.get(position).getUrl().getImageUrl()).into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}