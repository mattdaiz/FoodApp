package com.foodapp.android.foodapp.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.adapter.MessageAdapter;
import com.foodapp.android.foodapp.adapter.RecipeShareAdapter;
import com.foodapp.android.foodapp.model.FavouriteUser.Results;
import com.foodapp.android.foodapp.model.Messaging.Message;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    static final String TAG = MessageActivity.class.getSimpleName();

    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;


    EditText etMessage;
    Button btSend;
    FloatingActionButton btShare;
    TextView receiverName;

    RecyclerView rvChat;
    ArrayList<Message> mMessages;
    MessageAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;
    String receiverID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        receiverID = getIntent().getStringExtra("userReceiver");
        System.out.println(receiverID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setTitle(receiverID);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    // Get the userId from the cached currentUser object
    public void startWithCurrentUser() {
        setupMessagePosting();
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    public void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    // Create a handler which can run code periodically
    static final int POLL_INTERVAL = 1000; // milliseconds
    Handler myHandler = new Handler();  // android.os.Handler
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    // Setup message field and posting
    public void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.edittext_chatbox);
        btSend = (Button) findViewById(R.id.button_chatbox_send);
        rvChat = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        btShare = (FloatingActionButton) findViewById(R.id.floatingActionButton_share);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getUsername();
        mAdapter = new MessageAdapter(MessageActivity.this, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());
        rvChat.addItemDecoration(dividerItemDecoration);


        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                if (data.trim().isEmpty()) {

                } else {
                    //ParseObject message = ParseObject.create("Message");
                    //message.put(Message.USER_ID_KEY, userId);
                    //message.put(Message.BODY_KEY, data);
                    // Using new `Message` Parse-backed model now
                    Message message = new Message();
                    message.setBody(data);
                    message.setUserId(ParseUser.getCurrentUser().getUsername());
                    message.setUserReceiverId(receiverID);
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(MessageActivity.this, "Successfully created message on Parse", Toast.LENGTH_SHORT).show();
                            refreshMessages();
                        }
                    });
                    etMessage.setText(null);
                }
            }
        });

        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_sharerecipe);
                dialog.show();


                RecyclerView rvTest = (RecyclerView) dialog.findViewById(R.id.recycler_view_shareRecipe);
                LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
                rvTest.setLayoutManager(layoutManager);

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(v.getContext(), layoutManager.getOrientation());
                rvTest.addItemDecoration(dividerItemDecoration);


                Button btShareRecipe = (Button) dialog.findViewById(R.id.share_button);
                final List<Results> resultList = new ArrayList<>();
                final String[] recipeId = new String[3];
                final RecipeShareAdapter rvAdapter = new RecipeShareAdapter(v.getContext(), resultList, new RecipeShareAdapter.RecipeIdAdapterListener() {
                    @Override
                    public void classOnClick(View v, int position, String id, String photo, String name) {
                        recipeId[0] = id;
                        recipeId[1] = photo;
                        recipeId[2] = name;
                    }
                });
                rvTest.setAdapter(rvAdapter);

                // Parse through database and pass data to adapter
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourite");
                query.orderByAscending("createdAt");
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        resultList.clear();
                        if (e == null) {
                            for (ParseObject object : objects) {
                                Results result = new Results(object.getString("recipePhoto"), object.getString("recipeName"), object.getString("recipeId"));
                                resultList.add(result);
                            }
                        }
                        rvAdapter.notifyDataSetChanged();
                    }
                });

                btShareRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //System.out.println("Shared");
                        //System.out.println(recipeId[0]);
                        shareRecipe(recipeId[0], recipeId[1], recipeId[2]);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void shareRecipe(String recipeId, String photo, String name) {
        Message message = new Message();
        message.setBody("RID:" + recipeId + "+" + photo + "++" + name);
        message.setUserId(ParseUser.getCurrentUser().getUsername());
        message.setUserReceiverId(receiverID);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                refreshMessages();
            }
        });
    }

    // Query messages from Parse so we can load them into the chat adapter
    // Query messages from Parse so we can load them into the chat adapter
    public void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");

        // get only messages to receiver
        //query.whereEqualTo("userId", ParseUser.getCurrentUser().getUsername());
        //query.whereEqualTo("userReceiverId", receiverID);
        //query.whereEqualTo("userReceiverId", ParseUser.getCurrentUser().getUsername());
        //query.include("userId");


        //query.whereEqualTo("userReceiverId", ParseUser.getCurrentUser().getUsername());

        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();

                    for (Message message : messages) {
                        if (message.getUserId().equals(ParseUser.getCurrentUser().getUsername()) && message.getUserReceiverId().equals(receiverID)) {
                            mMessages.add(message);
                        } else if (message.getUserId().equals(receiverID) && message.getUserReceiverId().equals(ParseUser.getCurrentUser().getUsername())) {
                            mMessages.add(message);
                        }
                    }

                    //mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
        //Log.i("message", "Refresh1");
    }
}
