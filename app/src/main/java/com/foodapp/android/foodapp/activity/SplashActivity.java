package com.foodapp.android.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SharedPreference.getUserName(SplashActivity.this).length() == 0)
        {
            // call Login Activity - user not yet logged in
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
        }
        else
        {
            // call IngredientSearch Activity - user previously logged in
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }
        finish();
    }
}