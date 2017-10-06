package com.example.android.bakingapp;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity
        implements StepsAdapter.StepsItemClickListener {

    private final static String TAG = DetailActivity.class.getSimpleName();
    private static final String STATE_POSITION_KEY = "position-key";
    private static final String STATE_ITEM = "state-item-activity";
    private int mPosition;
    private RecipesItem mRecipesItem;
    private final static String DETAILS_FRAGMENT_TAG = "details-fragment";
    private final static String STEP_FRAGMENT_TAG = "step-fragment";
    private final static String STATE_STEP_POSITION_KEY = "state-stop-position-key";
    public final static String STEP_KEY = "step-key";
    public final static String STEP_POSITION_KEY = "step-position-key";
    public final static String STEP_SIZE_KEY = "step-size-key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        StepsAdapter.setListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar_activity_detail);
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            mRecipesItem = extras.getParcelable(MainActivity.RECIPES_ITEM_KEY);
            startDetailFragment(extras);
            if(getResources().getBoolean(R.bool.isTablet)) {
                startStepFragment(0);
            }
            updateWidget((RecipesItem) extras.getParcelable(MainActivity.RECIPES_ITEM_KEY));
        } else {
            mRecipesItem = savedInstanceState.getParcelable(STATE_ITEM);
            mPosition = savedInstanceState.getInt(STATE_STEP_POSITION_KEY);
        }
        setToolbarTitle(toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_ITEM, mRecipesItem);
        outState.putInt(STATE_STEP_POSITION_KEY, mPosition);
    }

    private void startDetailFragment(Bundle extras) {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        recipeDetailsFragment.setArguments(extras);
        if(getResources().getBoolean(R.bool.isTablet)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,
                    recipeDetailsFragment, DETAILS_FRAGMENT_TAG).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.step_fragment_container,
                    recipeDetailsFragment, DETAILS_FRAGMENT_TAG).commit();
        }
    }

    @Override
    public void onStepsItemClick(int position) {
        mPosition = position;
        startStepFragment(mPosition);
    }

    private void startStepFragment(int position) {
        Bundle extras = new Bundle();
        ArrayList<Step> steps = mRecipesItem.getSteps();
        extras.putParcelable(STEP_KEY, steps.get(position));
        extras.putInt(STEP_POSITION_KEY, position);
        extras.putInt(STEP_SIZE_KEY, steps.size());
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(extras);
        if(getResources().getBoolean(R.bool.isTablet)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.step_fragment_container,
                    stepFragment, STEP_FRAGMENT_TAG).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.step_fragment_container,
                    stepFragment, STEP_FRAGMENT_TAG).addToBackStack(null).commit();
        }
    }

    private void updateWidget(RecipesItem recipesItem) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BackingWidgetProvider.class));
        //Now update all widgets
        BackingWidgetProvider.updateAppWidgets(this, appWidgetManager, recipesItem, appWidgetIds);
    }

    // launched when next step in step fragment is clicked
    public void nextStep(View view) {
        startStepFragment(++mPosition);
    }

    // launched when previous step in step fragment  is clicked
    public void previousStep(View view) {
        startStepFragment(--mPosition);
    }

    private void setToolbarTitle(Toolbar toolbar) {
        if(getResources().getBoolean(R.bool.isTablet)) {
            toolbar.setTitle(R.string.app_name);
        } else {
            toolbar.setTitle(mRecipesItem.getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

