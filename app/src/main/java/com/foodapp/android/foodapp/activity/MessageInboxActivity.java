package com.foodapp.android.foodapp.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.adapter.MessageInboxAdapter;
import com.foodapp.android.foodapp.model.Messaging.Message;
import com.foodapp.android.foodapp.model.Messaging.MessageInbox;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import com.foodapp.android.foodapp.adapter.MessageInboxAdapter;

public class MessageInboxActivity extends AppCompatActivity {
    RecyclerView rvInbox;
    ArrayList<MessageInbox> mMessages;
    MessageInboxAdapter mAdapter;
    Set<String> userNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_inbox);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inbox);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setTitle("Inbox");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rvInbox = findViewById(R.id.inbox_recyclerView);
        mMessages = new ArrayList<>();
        userNames = new HashSet<>();


        mAdapter = new MessageInboxAdapter(MessageInboxActivity.this, mMessages);
        rvInbox.setAdapter(mAdapter);
        updateInbox();

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageInboxActivity.this);
        rvInbox.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());
        rvInbox.addItemDecoration(dividerItemDecoration);

        FloatingActionButton userAddConvo = (FloatingActionButton) (findViewById(R.id.floatingActionButton));
        userAddConvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserConvo(v);
            }
        });
    }

    public void addUserConvo(View view) {
        // setup the alert builder
        //AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        //builder.setTitle("Add Ingredients");
        final Dialog dialog = new Dialog(view.getContext());
        //builder.setView(LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_recipebox,null));

        final Button addUserButton;
        final EditText addUserEditText;

        dialog.setContentView(R.layout.dialog_adduser);
        dialog.setCanceledOnTouchOutside(true);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // create and show the alert dialog
        //AlertDialog dialog = builder.create();

        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        addUserEditText = dialog.findViewById(R.id.addUser_editText);
        addUserButton = dialog.findViewById(R.id.addUser_button);

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = addUserEditText.getText().toString();
                String currentUser = ParseUser.getCurrentUser().getUsername();
                System.out.println(currentUser);
                if (user.equals(currentUser) || userNames.contains(user)) {
                    Toast.makeText(MessageInboxActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    userNames.add(user);
                    searchUser(user);
                }
            }
        });
        dialog.show();
    }


    public void searchUser(final String username) {
        // Parse through database and pass data to adapter
        //String user = username;
        System.out.println(username);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    System.out.println(objects.size());
                    if (objects.size() == 0) {
                        Toast.makeText(MessageInboxActivity.this, "User Does Not Exist", Toast.LENGTH_SHORT).show();
                    }
                    for (ParseUser object : objects) {
                        if (object.getUsername().equals(username)) {
                            Toast.makeText(MessageInboxActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            createUserConvo(username, ParseUser.getCurrentUser().getUsername());
                            createReceiverConvo(username);
                            updateInbox();
                        }
                    }
                }
            }
        });
    }

    public void createUserConvo(final String usernameR, final String usernameS) {
        MessageInbox messageInbox = new MessageInbox();
        messageInbox.setUserSender(usernameS);
        messageInbox.setUserReceiver(usernameR);
        messageInbox.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(MessageInboxActivity.this, "Successfully created message on Parse", Toast.LENGTH_SHORT).show();
                //refreshMessages();
            }
        });
    }

    public void createReceiverConvo(final String username) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Inbox");
        query.whereEqualTo("userReceiver", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("userSender", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    System.out.println(objects.size());
                    if (objects.size() != 0) {
                        System.out.println("Found");
                    } else {
                        System.out.println("Not Found");
                        createUserConvo(ParseUser.getCurrentUser().getUsername(), username);
                        createReceiverHello(username, ParseUser.getCurrentUser().getUsername());

                    }
                }
            }
        });
    }

    public void createReceiverHello(final String usernameR, final String usernameS) {
        Message message = new Message();
        message.setUserId(usernameS);
        message.setUserReceiverId(usernameR);
        message.setBody("Hey, " + usernameS + " wanted to chat with you!");
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(MessageInboxActivity.this, "Successfully created message on Parse", Toast.LENGTH_SHORT).show();
                //refreshMessages();
            }
        });
    }


    public void updateInbox() {
        // Construct query to execute
        ParseQuery<MessageInbox> query = ParseQuery.getQuery(MessageInbox.class);

        //
        query.whereEqualTo("userSender", ParseUser.getCurrentUser().getUsername());

        // Configure limit and sort order
        //query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        //query.orderByDescending("createdAt");

        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<MessageInbox>() {
            public void done(List<MessageInbox> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
//                    if (mFirstLoad) {
//                        rvChat.scrollToPosition(0);
//                        mFirstLoad = false;
                    for (MessageInbox message : messages){
                        userNames.add(message.getUserReceiver());
                    }
//                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
                System.out.println(userNames);
            }
        });
    }
}
