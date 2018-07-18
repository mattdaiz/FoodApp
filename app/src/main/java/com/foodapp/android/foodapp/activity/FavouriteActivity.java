package com.foodapp.android.foodapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.foodapp.android.foodapp.R;

public class FavouriteActivity extends AppCompatActivity {

    private BottomNavigationView navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        navigationBar = (BottomNavigationView) findViewById(R.id.navigationbar);

        //set up for navigation bar
        navigationBar.setItemIconTintList(null);
        navigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        Intent i = new Intent(FavouriteActivity.this, MainActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.favourites:
                        return true;
                }
                return true;
            }
        });
    }
}
