package com.foodapp.android.foodapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.model.Messaging.Message;

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
        Message message = mMessages.get(position);
        //System.out.println(message.getUserId() + " | " + message.getUserReceiverId()  + " | "  + message.getBody());
        final boolean isMe = message.getUserId() != null
                && message.getUserId().equals(mUserId);
        if (isMe) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.imageMe.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;
        //Glide.with(mContext).load(getProfileUrl(message.getUserId())).into(profileView);
        holder.body.setText(message.getBody());
    }

    // Create a gravatar image based on the hash value obtained from userId
//    private static String getProfileUrl(final String userId) {
//        String hex = "";
//        try {
//            final MessageDigest digest = MessageDigest.getInstance("MD5");
//            final byte[] hash = digest.digest(userId.getBytes());
//            final BigInteger bigInt = new BigInteger(hash);
//            hex = bigInt.abs().toString(16);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
//    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView body;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            body = (TextView)itemView.findViewById(R.id.tvBody);
        }
    }
}