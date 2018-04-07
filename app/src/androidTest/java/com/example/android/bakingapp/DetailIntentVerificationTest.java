package com.example.android.bakingapp;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.activity.DetailActivity;
import com.example.android.bakingapp.activity.MainActivity;
import com.example.android.bakingapp.fragment.RecipesFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Professor on 4/8/2018.
 */
@RunWith(AndroidJUnit4.class)
public class DetailIntentVerificationTest {

    private IdlingResource mIdlingResource;


    @Rule
    public IntentsTestRule<MainActivity> activityRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        RecipesFragment fragment = (RecipesFragment) activityRule.getActivity()
                .getSupportFragmentManager().findFragmentByTag(MainActivity.RECIPES_FRAGMENT_TAG);
        mIdlingResource = fragment.getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void typeNumber_ValidInput_InitiatesCall() {
        onView(withId(R.id.rv_recipes_selection))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(allOf(hasComponent(DetailActivity.class.getName())));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
