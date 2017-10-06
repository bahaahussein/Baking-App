package com.example.android.bakingapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {


    private static final String STATE_SCROLL_POSITION = "state-scroll-position";
    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();
    private static final String STATE_INGREDIENTS = "state-ingredients";
    private static final String STATE_ITEM = "state-item";
    private TextView mIngreidentsTextView;
    private RecipesItem mRecipesItem;
    private RecyclerView mStepsRecyclerView;
    private ScrollView mScrollView;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        mIngreidentsTextView = view.findViewById(R.id.tv_ingredients);
        mStepsRecyclerView = view.findViewById(R.id.rv_steps);
        mScrollView = view.findViewById(R.id.scrollview_fragment_recipe_details);
        mStepsRecyclerView.setFocusable(false);
        view.findViewById(R.id.linearlayout_fragment_recipe_details).requestFocus();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            final int position = savedInstanceState.getInt(STATE_SCROLL_POSITION, 0);
            Log.d(TAG, "saved instance state not null " + position);
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(0, position);
                }
            });
            mRecipesItem = savedInstanceState.getParcelable(STATE_ITEM);
            mIngreidentsTextView.setText(savedInstanceState.getString(STATE_INGREDIENTS));
        } else {
            Bundle extras = getActivity().getIntent().getExtras();
            mRecipesItem = extras.getParcelable(MainActivity.RECIPES_ITEM_KEY);
            mRecipesItem = getArguments().getParcelable(MainActivity.RECIPES_ITEM_KEY);
            ArrayList<Ingredient> ingredients = mRecipesItem.getIngredients();
            for(Ingredient ingredient : ingredients) {
                mIngreidentsTextView.append(ingredient.toString()+"\n");
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mStepsRecyclerView.setLayoutManager(layoutManager);
        mStepsRecyclerView.setHasFixedSize(true);
        mStepsRecyclerView.setAdapter(new StepsAdapter(mRecipesItem.getSteps()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int y = mScrollView.getScrollY();
        Log.d(TAG, "saved instance state called in fragment " + y);
        outState.putInt(STATE_SCROLL_POSITION, y);
        outState.putString(STATE_INGREDIENTS, mIngreidentsTextView.getText().toString());
        outState.putParcelable(STATE_ITEM, mRecipesItem);
    }
}
