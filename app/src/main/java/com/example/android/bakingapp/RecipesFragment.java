package com.example.android.bakingapp;


import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<String> {

    private final String TAG = RecipesFragment.class.getSimpleName();
    private static final int FETCH_DATA_LOADER = 11;
    private ArrayList<RecipesItem> mRecipesItems;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorTextView;
    private RecyclerView mRecyclerView;

    public RecipesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        mLoadingIndicator = view.findViewById(R.id.pb_fragment_recipes);
        mErrorTextView = view.findViewById(R.id.tv_fragment_recipes_error_message);
        mRecyclerView = view.findViewById(R.id.rv_recipes_selection);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getResources().getBoolean(R.bool.isTablet) ||
                getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Initialize the loader
        getActivity().getSupportLoaderManager().initLoader(FETCH_DATA_LOADER, null, this);
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showRecyclerView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(getContext()) {

            String mData;

            @Override
            protected void onStartLoading() {
                showLoadingIndicator();
                if (mData != null) {
                    deliverResult(mData);
                } else {
                    Log.d(TAG, "onStartLoading: staaaaaaaaaaaaart");
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                try {
                    URL url = NetworkUtils.buildUrl();
                    String recipesResults = NetworkUtils.getResponseFromHttpUrl(url);
                    return recipesResults;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "loadInBackground: IO exception");
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mData = data;
                super.deliverResult(data);
            }
        };
    }

    private void setRecyclerViewAdapter() {
        showRecyclerView();
        RecipesSelectionAdapter adapter = new RecipesSelectionAdapter(mRecipesItems);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data == null) {
            Log.d(TAG, "onLoadFinished: data is null");
            showErrorMessage();
            return;
        }
        if(mRecipesItems == null) {
            try {
                fetchDataFromJson(data);
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage();
                Log.d(TAG, "onLoadFinished: error in json");
                return;
            }
        }
        setRecyclerViewAdapter();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void fetchDataFromJson(String dataString) throws JSONException {
        mRecipesItems = new ArrayList<RecipesItem>();

        final String ITEM_NAME = "name";
        final String ITEM_INGREDIENTS = "ingredients";
        final String INGREDIENTS_QUANTITY = "quantity";
        final String INGREDIENTS_MEASURE = "measure";
        final String INGREDIENTS_INGREDIENT = "ingredient";
        final String ITEM_STEPS = "steps";
        final String STEP_SHORT_DESCRIPTION = "shortDescription";
        final String STEP_DESCRIPTION = "description";
        final String STEP_VIDEO_URL = "videoURL";
        final String STEP_THUMBNAIL_URL = "thumbnailURL";

        JSONArray forecastJsonArray = new JSONArray(dataString);
        for (int i=0; i<forecastJsonArray.length(); i++) {
            JSONObject itemForecast = forecastJsonArray.getJSONObject(i);
            RecipesItem recipesItem = new RecipesItem();
            recipesItem.setName(itemForecast.getString(ITEM_NAME));
            JSONArray ingredientsJsonArray = itemForecast.getJSONArray(ITEM_INGREDIENTS);
            recipesItem.initializeIngredients(ingredientsJsonArray.length());
            for (int j=0; j<ingredientsJsonArray.length(); j++) {
                JSONObject ingredientForecast = ingredientsJsonArray.getJSONObject(j);
                Ingredient ingredient = new Ingredient();
                ingredient.setQuantity(ingredientForecast.getInt(INGREDIENTS_QUANTITY));
                ingredient.setMeasure(ingredientForecast.getString(INGREDIENTS_MEASURE));
                ingredient.setIngredient(ingredientForecast.getString(INGREDIENTS_INGREDIENT));
                recipesItem.addIngredient(ingredient);
            }
            JSONArray stepsJsonArray = itemForecast.getJSONArray(ITEM_STEPS);
            recipesItem.intializeSteps(stepsJsonArray.length());
            for (int j=0; j<stepsJsonArray.length(); j++) {
                JSONObject stepForecast = stepsJsonArray.getJSONObject(j);
                Step step = new Step();
                step.setShortDescription(stepForecast.getString(STEP_SHORT_DESCRIPTION));
                step.setDescription(stepForecast.getString(STEP_DESCRIPTION));
                step.setVideoUrl(stepForecast.getString(STEP_VIDEO_URL));
                step.setThumbnailUrl(stepForecast.getString(STEP_THUMBNAIL_URL));
                recipesItem.addStep(step);
            }

            mRecipesItems.add(recipesItem);
        }
    }
}
