package com.example.android.bakingapp.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.SampleIdlingResource;
import com.example.android.bakingapp.adapter.RecipesSelectionAdapter;
import com.example.android.bakingapp.model.RecipesItem;
import com.example.android.bakingapp.retrofit.ApiClient;
import com.example.android.bakingapp.retrofit.ApiInterface;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment {

    private final String TAG = RecipesFragment.class.getSimpleName();
    private ArrayList<RecipesItem> mRecipesItems;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorTextView;
    private RecyclerView mRecyclerView;
    // The Idling Resource which will be null in production.
    @Nullable
    private SampleIdlingResource mIdlingResource;

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
        showLoadingIndicator();
        if (mIdlingResource != null) {
            Log.d(TAG, "onStart: idlingResource is called with false");
            mIdlingResource.setIdleState(false);
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<RecipesItem>> call = apiService.getRecipes();
        call.enqueue(new Callback<ArrayList<RecipesItem>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipesItem>> call, Response<ArrayList<RecipesItem>> response) {
                mRecipesItems = response.body();
                setRecyclerViewAdapter();
                if (mIdlingResource != null) {
                    Log.d(TAG, "onResponse: idlingResource is called with true");
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecipesItem>> call, Throwable t) {
                showErrorMessage();
                if (mIdlingResource != null) {
                    Log.d(TAG, "onFailure: idlingResource is called with true");
                    mIdlingResource.setIdleState(true);
                }
                Log.e(TAG, "retrofit error: "+t.toString() );
            }
        });
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

    private void setRecyclerViewAdapter() {
        showRecyclerView();
        RecipesSelectionAdapter adapter = new RecipesSelectionAdapter(mRecipesItems);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Only called from test, creates and returns a new {@link SampleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SampleIdlingResource();
        }
        return mIdlingResource;
    }
}
