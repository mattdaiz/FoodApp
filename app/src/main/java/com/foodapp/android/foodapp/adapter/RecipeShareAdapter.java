package com.foodapp.android.foodapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.model.FavouriteUser.Results;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeShareAdapter extends RecyclerView.Adapter<RecipeShareAdapter.ViewHolder> {
    private List<Results> results;
    private Context mContext;
    public RecipeIdAdapterListener onClickListener;

    // SparseBooleanArrays map integers to booleans
    private SparseBooleanArray selectedItems;

    public RecipeShareAdapter(Context context, List<Results> results, RecipeIdAdapterListener listener) {
        mContext = context;
        this.results = results;
        this.onClickListener = listener;
        this.selectedItems = new SparseBooleanArray();
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
        holder.recipeId = results.get(position).getRecipeId();
        holder.photo = results.get(position).getRecipePhoto();
        holder.name = results.get(position).getRecipeName();

        // Set the selected state of the row depending on the position
        // State color dependant on selector_row.xml
        holder.relativeLayout.setSelected(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView recipePhoto;
        TextView recipeLabel;
        RelativeLayout relativeLayout;
        String recipeId, photo, name;

        ViewHolder(View itemView) {
            super(itemView);
            recipePhoto = (ImageView) itemView.findViewById(R.id.list_image_shareRecipe);
            recipeLabel = (TextView) itemView.findViewById(R.id.list_title_shareRecipe);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_shareRecipe);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.classOnClick(v, getAdapterPosition(), recipeId, photo, name);
                    selectedItems.clear();
                    selectedItems.put(getAdapterPosition(), true);
                    notifyDataSetChanged();
                }
            });
        }
    }

    // Interface to keep track of clicked recipeId
    public interface RecipeIdAdapterListener {
        void classOnClick(View v, int position, String id, String photo, String name);
    }
}