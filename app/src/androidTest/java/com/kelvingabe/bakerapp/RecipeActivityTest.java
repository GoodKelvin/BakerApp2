package com.kelvingabe.bakerapp;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {


    private final String string = "Recipe Introduction";

    @Rule
    public ActivityTestRule<RecipeActivity> recipeActivityActivityTestRule
            = new ActivityTestRule(RecipeActivity.class);

    @Test
    public void testSelection() {

        Espresso.onView(ViewMatchers.withId(R.id.recipe_recycler))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click())
                );

        Espresso.onView(ViewMatchers.withId(R.id.recipe_steps_recycler))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click())
                );

        Espresso.onView(ViewMatchers.withId(R.id.step_instruction_text)).check(ViewAssertions.matches(ViewMatchers.withText(string)));
    }
}
