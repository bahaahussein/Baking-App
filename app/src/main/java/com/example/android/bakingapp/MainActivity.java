package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
        implements RecipesSelectionAdapter.ListItemClickListener{

    public static final String RECIPES_ITEM_KEY = "recipes-item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_activity_main);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_container,
                new RecipesFragment()).commit();
            /*
            // Get a support ActionBar corresponding to this toolbar
            ActionBar ab = getSupportActionBar();

            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            */
        RecipesSelectionAdapter.setRecipesSelectionAdapterListener(this);

    }
    @Override
    public void onListItemClick(RecipesItem recipesItem) {
        Bundle extras = new Bundle();
        extras.putParcelable(RECIPES_ITEM_KEY, recipesItem);
        Intent startDetailActivityIntent = new Intent(this, DetailActivity.class);
        startDetailActivityIntent.putExtras(extras);
        startActivity(startDetailActivityIntent);
    }
}
