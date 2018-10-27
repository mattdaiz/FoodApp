package com.foodapp.android.foodapp.model.Messaging;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Inbox")
public class MessageInbox extends ParseObject {
    public static final String USER_SENDER = "userSender";
    public static final String USER_RECEIVER = "userReceiver";

    public String getUserSender() {
        return getString(USER_SENDER);
    }

    public String getUserReceiver() {
        return getString(USER_RECEIVER);
    }

    public void setUserSender(String userId) {
        put(USER_SENDER, userId);
    }

    public void setUserReceiver(String body) {
        put(USER_RECEIVER, body);
    }
}
