package com.foodapp.android.foodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.activity.RecipeActivity;
import com.foodapp.android.foodapp.model.Messaging.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;

    public MessageAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.custom_message_chat, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Message message = mMessages.get(position);
        //System.out.println(message.getUserId() + " | " + message.getUserReceiverId()  + " | "  + message.getBody());
        final boolean isMe = message.getUserId() != null && message.getUserId().equals(mUserId);
        if (isMe && !message.getBody().contains("RID")) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            holder.body.setText(message.getBody());
        } else if (isMe && message.getBody().contains("RID")) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.VISIBLE);
            // Loads image
            Picasso.get().load(message.getBody().substring(message.getBody().indexOf("+") + 1, message.getBody().indexOf("++"))).into(holder.imageOther);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

            // Sets recipe body
            String header = "<b>" + "Check This Recipe Out: " + "</b>";
            String recipeName = message.getBody().substring(message.getBody().lastIndexOf("++") + 2);
            String styledRecipeName = "<u><font color=#2962FF>" + recipeName + "</font></u>";
            holder.body.setText(Html.fromHtml(header + styledRecipeName));

            // Sets recipe Id
            holder.recipeId = message.getBody().substring(message.getBody().indexOf("RID:") + 4, message.getBody().indexOf("+"));
        } else if (!isMe && !message.getBody().contains("RID")) {
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.imageMe.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.body.setText(message.getBody());
        } else if (!isMe && message.getBody().contains("RID")){
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.VISIBLE);
            // Loads image
            Picasso.get().load(message.getBody().substring(message.getBody().indexOf("+") + 1, message.getBody().indexOf("++"))).into(holder.imageMe);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

            // Sets recipe body
            String header = "<b>" + "Check This Recipe Out: " + "</b>";
            String recipeName = message.getBody().substring(message.getBody().lastIndexOf("++") + 2);
            String styledRecipeName = "<u><font color=#2962FF>" + recipeName + "</font></u>";
            holder.body.setText(Html.fromHtml(header + styledRecipeName));

            // Sets recipe Id
            holder.recipeId = message.getBody().substring(message.getBody().indexOf("RID:") + 4, message.getBody().indexOf("+"));
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView body;
        String recipeId;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView) itemView.findViewById(R.id.ivProfileOther);
            imageMe = (ImageView) itemView.findViewById(R.id.ivProfileMe);
            body = (TextView) itemView.findViewById(R.id.tvBody);
            body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recipeId != null){
                        Intent intent = new Intent(v.getContext(), RecipeActivity.class);
                        intent.putExtra("recipeId", recipeId);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}