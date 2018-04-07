package com.example.android.bakingapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.SampleIdlingResource;
import com.example.android.bakingapp.fragment.RecipesFragment;
import com.example.android.bakingapp.adapter.RecipesSelectionAdapter;
import com.example.android.bakingapp.model.RecipesItem;

public class MainActivity extends AppCompatActivity
        implements RecipesSelectionAdapter.ListItemClickListener {

    public static final String RECIPES_ITEM_KEY = "recipes-item";

    public static String RECIPES_FRAGMENT_TAG = "recipes-fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_activity_main);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_activity_main_title);
        Typeface lobster = Typeface.createFromAsset(getAssets(), "Lobster.ttf");
        toolbarTitle.setTypeface(lobster);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        RecipesFragment fragment = new RecipesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_container,
                fragment,RECIPES_FRAGMENT_TAG).commit();
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
