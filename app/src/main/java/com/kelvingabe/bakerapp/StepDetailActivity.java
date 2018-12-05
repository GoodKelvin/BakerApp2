package com.kelvingabe.bakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static com.kelvingabe.bakerapp.ViewRecipeDetailsActivity.STEP_LIST;
import static com.kelvingabe.bakerapp.ViewRecipeDetailsActivity.STEP_TO_DISPLAY_INDEX;


//Activity to display a list of step
public class StepDetailActivity extends AppCompatActivity {

    private List<Step> mStepList;

    //index of step to display
    private int mStepToDisplayIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLandscape = getResources().getBoolean(R.bool.is_landscape);
        setContentView(R.layout.activity_step_detail);

        Intent intent = getIntent();
        //set up the list
        if (intent != null && intent.getParcelableArrayListExtra(STEP_LIST) != null) {
            mStepList = intent.getParcelableArrayListExtra(STEP_LIST);
            mStepToDisplayIndex = intent.getIntExtra(STEP_TO_DISPLAY_INDEX, 0);

        }


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //make action bar available on if not in landscape
            if (!isLandscape) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            } else {
                actionBar.hide();
            }
        }


        //check if this is a recreated activity to prevent creating another fragment
        if (savedInstanceState == null) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(STEP_LIST, (ArrayList<Step>) mStepList);
            bundle.putInt(STEP_TO_DISPLAY_INDEX, mStepToDisplayIndex);
            stepDetailFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.step_fragment, stepDetailFragment);
            fragmentTransaction.commit();
        }
    }

}
