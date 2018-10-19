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
import com.like.LikeButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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
        private ImageView imgFood, imgHeart;


        RecipeViewHolder(final View itemView) {
            super(itemView);
            imgFood = (ImageView) itemView.findViewById(R.id.image_food);
            txtRecipeLabel = (TextView) itemView.findViewById(R.id.txt_recipe_label);
            //txtRecipeIngredients = (TextView) itemView.findViewById(R.id.txt_recipe_ingredients);
            txtRecipeRating = (TextView) itemView.findViewById(R.id.txt_recipe_rating);
            imgHeart = (ImageView) itemView.findViewById(R.id.star_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RecipeActivity.class);
                    intent.putExtra("recipeId", dataList.get(getAdapterPosition()).getId());
                    intent.putExtra("recipePosition",Integer.toString(getAdapterPosition()));
                    //shared element transition
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) v.getContext(),(View)imgFood,"myImage");
                    ((Activity) v.getContext()).startActivityForResult(intent,1,optionsCompat.toBundle());
                    //((Activity) v.getContext()).startActivity(intent,optionsCompat.toBundle());
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
    public void onBindViewHolder(final RecipeViewHolder holder, final int position) {
        holder.txtRecipeLabel.setText(dataList.get(position).getRecipeName());
        //holder.txtRecipeIngredients.setText(dataList.get(position).getIngredients().toString());
        holder.txtRecipeRating.setText("Rating: " + dataList.get(position).getRating());
        //String pictureUrl = dataList.get(position).getImageUrlsBySize().get90();

        //Some of them dont have pictures, need try catch
        try {
            Picasso.get().load(dataList.get(position).getImageUrlsBySize().get90().replace("=s90", "=s360")).into(holder.imgFood);
        }catch (NullPointerException e){

        }


        // Query to search in the database
        final String RecipeID = dataList.get(position).getId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        try{
            List<ParseObject> objects = query.find();
            if (objects.size() > 0){
                //Log.i("size",String.valueOf(objects.size()));
                // Iterate through all the user's favourite recipe's
                for (ParseObject object : objects){
                    if (object.getString("recipeId").equals(RecipeID) && object.getString("username").equals(ParseUser.getCurrentUser().getUsername().toString())) {
                        holder.imgHeart.setVisibility(View.VISIBLE);
                        break;
                    }else{
                        holder.imgHeart.setVisibility(View.INVISIBLE);
                    }
                }
            }

        }catch(ParseException e){

        }
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e == null){
//                    if (objects.size() > 0){
//                        //Log.i("size",String.valueOf(objects.size()));
//                        // Iterate through all the user's favourite recipe's
//                        for (ParseObject object : objects){
//                            if (object.getString("recipeId").equals(RecipeID) && object.getString("username").equals(ParseUser.getCurrentUser().getUsername().toString())) {
//                                holder.imgHeart.setVisibility(View.VISIBLE);
//                                break;
//                            }else{
//                                holder.imgHeart.setVisibility(View.INVISIBLE);
//                            }
//                        }
//                    }
//                }
//            }
//        });
        //Log.d("TAG", "Position: " + position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}