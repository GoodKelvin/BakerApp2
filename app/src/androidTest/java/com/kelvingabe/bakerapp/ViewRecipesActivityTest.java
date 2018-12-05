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

import cr8ivley.io.bakerapp.ui.ViewRecipesActivity;

@RunWith(AndroidJUnit4.class)
public class ViewRecipesActivityTest {

    final String recipe0thName = "Nutella Pie";
    @Rule
    public ActivityTestRule<ViewRecipesActivity> viewRecipesActivityActivityTestRule
            = new ActivityTestRule(ViewRecipesActivity.class);

    @Test
    public void recipeSelectionTest() {
        //match data on the the recipe recycler view and perform a click on the 0th item
        //gotten from https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        Espresso.onView(ViewMatchers.withId(R.id.recipe_recycler))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click())
                );

        //expected result is that the appbar displays the recipe name
        //got this idea from http://blog.sqisland.com/2015/05/espresso-match-toolbar-title.html
        Espresso.onView(
                Matchers.allOf(
                        ViewMatchers.withParent(ViewMatchers.isAssignableFrom(Toolbar.class))
                        , ViewMatchers.isAssignableFrom(TextView.class)
                )
        ).check(ViewAssertions.matches(ViewMatchers.withText(recipe0thName)));

    }
}
