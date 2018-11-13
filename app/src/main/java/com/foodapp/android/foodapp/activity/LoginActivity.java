/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.foodapp.android.foodapp.activity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodapp.android.foodapp.R;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.Parse;
import com.parse.SaveCallback;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    TextView changeSignupModeTextView;
    EditText usernameEditText;
    EditText passwordEditText;
    LinearLayout backgroundLinearLayout;

    //keyboard gone once click enter
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            //signUp(view);
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        return false;
    }

    public void signupClick(View view){
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(i);
    }

    public void loginClick(View view){
        login(view);
    }


    @Override
    public void onClick(View view) {
//        if(view.getId() == R.id.changeSignupModeTextView){
//
//            Button signupButton = (Button) findViewById(R.id.signupButton);
//
//            if (signUpModeActive) {
//
//                signUpModeActive = false;
//                signupButton.setText("Login");
//                changeSignupModeTextView.setText("Or, Signup");
//            }else{
//                signUpModeActive = true;
//                signupButton.setText("Signup");
//                changeSignupModeTextView.setText("Or, Login");
//
//            }
        //close keyboard if click somewhere else
        if (view.getId() == R.id.backgroundLinearLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void login(View view){


        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this,"A username and password are required", Toast.LENGTH_SHORT).show();
        }else{
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null){
                            Log.i("Signup", "Login successful");

                            // Stores information that user already logged in
                            SharedPreference.setUserName(getApplicationContext(),user.getUsername());
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }else{
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backgroundLinearLayout = (LinearLayout) findViewById(R.id.backgroundLinearLayout);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        //changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);

        //changeSignupModeTextView.setOnClickListener(this);

        passwordEditText.setOnKeyListener(this);

        backgroundLinearLayout.setOnClickListener(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


}