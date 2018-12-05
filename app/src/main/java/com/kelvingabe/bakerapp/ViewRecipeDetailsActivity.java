package com.kelvingabe.bakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.ArrayList;


//class to display a recipe's details
public class ViewRecipeDetailsActivity extends AppCompatActivity implements ViewRecipeDetailsFragment.OnStepInteractionListener {


    public static final String RECIPE_TO_DISPLAY = "recipe_to_display";
    public static final String STEP_LIST = "step_array";
    public static final String STEP_TO_DISPLAY_INDEX = "recipe_to_display_index";
    private Recipe mSelectedRecipe;


    //boolean to hold mode of activity false = single pane , true = two pane
    private boolean mTwoPaneMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_details);
        ActionBar actionBar = getSupportActionBar();

        //find is the second pane is available. if not, set two pane mode to false and return
        FrameLayout secondPaneFrame = findViewById(R.id.step_detail);
        mTwoPaneMode = secondPaneFrame != null;
        Log.d("XXX", "Pane: " + mTwoPaneMode);

        Intent intent = getIntent();
        if (intent != null && intent.getParcelableExtra(MainActivity.SELECTED_RECIPE_INGREDIENTS) != null) {
            mSelectedRecipe = intent.getParcelableExtra(MainActivity.SELECTED_RECIPE_INGREDIENTS);

        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (mSelectedRecipe != null) {
                actionBar.setTitle(mSelectedRecipe.name);
            }
        }

        if (savedInstanceState == null) {
            setupRecipeDetailFragment();
            //if two pane mode, show step detail fragment
            if (mTwoPaneMode) {
                setupStepDetailFragment(0);
            }
        }
    }

    private void setupRecipeDetailFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_TO_DISPLAY, mSelectedRecipe);
        ViewRecipeDetailsFragment recipeDetailFragment = new ViewRecipeDetailsFragment();
        recipeDetailFragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.recipe_detail_fragment, recipeDetailFragment);
        fragmentTransaction.commit();
    }

    private void setupStepDetailFragment(int index) {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEP_LIST, (ArrayList<Step>) mSelectedRecipe.steps);
        bundle.putInt(STEP_TO_DISPLAY_INDEX, index);
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_detail, stepDetailFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onStepInteraction(int index) {
        //if two pane mode, display step in current activity
        if (mTwoPaneMode) {
            setupStepDetailFragment(index);
        } else {
            Intent stepDetailActivityIntent = new Intent(this, StepDetailActivity.class);
            stepDetailActivityIntent.putParcelableArrayListExtra(STEP_LIST, (ArrayList<Step>) mSelectedRecipe.steps);
            stepDetailActivityIntent.putExtra(STEP_TO_DISPLAY_INDEX, index);
            startActivity(stepDetailActivityIntent);
        }
    }


//    private void displayStepFragment(int index) {
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(STEP_LIST,(ArrayList<Step>)mSelectedRecipe.steps );
//        bundle.putInt(STEP_TO_DISPLAY_INDEX,index);
//        StepDetailFragment stepDetailFragment = new StepDetailFragment();
//        stepDetailFragment.setArguments(bundle);
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.step_detail, stepDetailFragment);
//        fragmentTransaction.commit();
//    }
}
