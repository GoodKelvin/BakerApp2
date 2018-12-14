package com.kelvingabe.bakerapp;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    final String brownies = "Brownies";
    @Rule
    public ActivityTestRule<MainActivity> viewRecipesActivityActivityTestRule
            = new ActivityTestRule(MainActivity.class);

    @Test
    public void testSelectionRecipe() {
        Espresso.onView(ViewMatchers.withId(R.id.recipe_recycler))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click())
                );

        Espresso.onView(
                Matchers.allOf(
                        ViewMatchers.withParent(ViewMatchers.isAssignableFrom(Toolbar.class))
                        , ViewMatchers.isAssignableFrom(TextView.class)
                )
        ).check(ViewAssertions.matches(ViewMatchers.withText(brownies)));

    }
}
