package com.foodapp.android.foodapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>  {
    private Context context;
    private List<String> ingredientList;


    public IngredientListAdapter(Context context, List<String> ingredientList){
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_recipe_ingredient_list, parent, false);
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

    class IngredientViewHolder extends RecyclerView.ViewHolder{
        TextView txtIngredient;

        IngredientViewHolder (View itemView){
            super(itemView);
            txtIngredient = (TextView) itemView.findViewById(R.id.textView_ingredient);
            txtIngredient.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i("TEST","Name clicked : "+getAdapterPosition());
                }
            });
        }
    }
}
