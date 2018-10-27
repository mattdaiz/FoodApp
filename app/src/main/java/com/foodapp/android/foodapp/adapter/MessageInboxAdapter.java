package com.foodapp.android.foodapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.activity.MainActivity;
import com.foodapp.android.foodapp.activity.MessageActivity;
import com.foodapp.android.foodapp.activity.MessageInboxActivity;
import com.foodapp.android.foodapp.activity.RecipeActivity;
import com.foodapp.android.foodapp.model.Messaging.MessageInbox;

import java.util.List;

public class MessageInboxAdapter extends RecyclerView.Adapter<MessageInboxAdapter.ViewHolder> {
    private List<MessageInbox> mMessages;
    private Context mContext;

    public MessageInboxAdapter(Context context, List<MessageInbox> messages) {
        mMessages = messages;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.custom_message_inbox_user, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageInbox message = mMessages.get(position);
        holder.username.setText(message.getUserReceiver());
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView username;
        RelativeLayout touch;

        public ViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.inbox_userphoto);
            username = (TextView) itemView.findViewById(R.id.inbox_username);
            touch = (RelativeLayout) itemView.findViewById(R.id.inbox_layout);

            touch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MessageActivity.class);
                    intent.putExtra("userReceiver", username.getText());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}