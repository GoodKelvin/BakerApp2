package com.kelvingabe.bakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.util.ArrayList;


public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnStepInteractionListener {

    public static final String _ARRAY = "_array";
    public static final String RECIPE_TO_DISPLAY = "recipe_to_display";
    public static final String _INDEX = "_index";
    public String TAG = "RecipeActivity";
    private Recipe recipe;


    private boolean twoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_details);
        ActionBar actionBar = getSupportActionBar();

        FrameLayout secondPaneFrame = findViewById(R.id.step_detail);
        twoPane = secondPaneFrame != null;

        Intent intent = getIntent();
        if (intent != null && intent.getParcelableExtra(MainActivity.SELECTED_RECIPE) != null) {
            recipe = intent.getParcelableExtra(MainActivity.SELECTED_RECIPE);

        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (recipe != null) {
                actionBar.setTitle(recipe.name);
            }
        }

        if (savedInstanceState == null) {
            setupFragment();
            if (twoPane) {
                setupStepDetailFrag(0);
            }
        }
    }

    private void setupFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_TO_DISPLAY, recipe);
        RecipeFragment recipeDetailFragment = new RecipeFragment();
        recipeDetailFragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.recipe_detail_fragment, recipeDetailFragment);
        fragmentTransaction.commit();
    }

    private void setupStepDetailFrag(int index) {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(_ARRAY, (ArrayList<Step>) recipe.steps);
        bundle.putInt(_INDEX, index);
        StepsFragment stepsFragment = new StepsFragment();
        stepsFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_detail, stepsFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onStepInteraction(int index) {
        if (twoPane) {
            setupStepDetailFrag(index);
        } else {
            Intent stepDetailActivityIntent = new Intent(this, StepsActivity.class);
            stepDetailActivityIntent.putParcelableArrayListExtra(_ARRAY, (ArrayList<Step>) recipe.steps);
            stepDetailActivityIntent.putExtra(_INDEX, index);
            startActivity(stepDetailActivityIntent);
        }
    }

}
