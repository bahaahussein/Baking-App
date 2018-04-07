package com.example.android.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.internal.util.Checks.checkNotNull;

import static org.hamcrest.Matchers.equalToIgnoringCase;


import com.example.android.bakingapp.activity.MainActivity;
import com.example.android.bakingapp.fragment.RecipesFragment;


/**
 * Created by Professor on 4/7/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    public static final String RECIPE_NAME = "nutella pie";
    public static final String STEP_NAME = "recipe introduction";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        RecipesFragment fragment = (RecipesFragment) mActivityRule.getActivity()
                .getSupportFragmentManager().findFragmentByTag(MainActivity.RECIPES_FRAGMENT_TAG);
        mIdlingResource = fragment.getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void adapterTest() {
        onView(withId(R.id.rv_recipes_selection))
                .check(matches(atPosition(0, hasDescendant(withText(equalToIgnoringCase(RECIPE_NAME))))));
    }

    @Test
    public void detailActivityRecyclerviewTest() {
        onView(withId(R.id.rv_recipes_selection))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.rv_steps))
                .check(matches(atPosition(0, hasDescendant(withText(equalToIgnoringCase(STEP_NAME))))));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
