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

import cr8ivley.io.bakerapp.ui.ViewRecipesActivity;

@RunWith(AndroidJUnit4.class)
public class ViewRecipeDetailsActivityTest {


    private final String recipe0thStep0thInstruction = "Recipe Introduction";

    @Rule
    public ActivityTestRule<ViewRecipesActivity> viewRecipesActivityActivityTestRule
            = new ActivityTestRule(ViewRecipesActivity.class);

    @Test
    public void recipeStepSelectionTest() {

        //perform click on recipe
        Espresso.onView(ViewMatchers.withId(R.id.recipe_recycler))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click())
                );

        //perform click on step
        Espresso.onView(ViewMatchers.withId(R.id.recipe_steps_recycler))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click())
                );

        Espresso.onView(ViewMatchers.withId(R.id.step_instruction_text)).check(ViewAssertions.matches(ViewMatchers.withText(recipe0thStep0thInstruction)));
    }
}
