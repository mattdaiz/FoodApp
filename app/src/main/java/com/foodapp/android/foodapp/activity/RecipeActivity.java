package com.foodapp.android.foodapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.foodapp.android.foodapp.R;

public class RecipeActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        textView = (TextView) findViewById(R.id.textView_test);
        String value = getIntent().getStringExtra("recipeId");
        textView.setText(value);
    }
}
