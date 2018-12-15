package com.kelvingabe.bakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static com.kelvingabe.bakerapp.RecipeActivity._ARRAY;
import static com.kelvingabe.bakerapp.RecipeActivity._INDEX;


public class StepsActivity extends AppCompatActivity {

    private List<Step> steps;
    private int stepsIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLandscape = getResources().getBoolean(R.bool.is_landscape);
        setContentView(R.layout.activity_steps);

        Intent intent = getIntent();
        if (intent != null && intent.getParcelableArrayListExtra(_ARRAY) != null) {
            steps = intent.getParcelableArrayListExtra(_ARRAY);
            stepsIndex = intent.getIntExtra(_INDEX, 0);

        }


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (!isLandscape) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            } else {
                actionBar.hide();
            }
        }


        if (savedInstanceState == null) {
            StepsFragment stepsFragment = new StepsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(_ARRAY, (ArrayList<Step>) steps);
            bundle.putInt(_INDEX, stepsIndex);
            stepsFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.step_fragment, stepsFragment);
            fragmentTransaction.commit();
        }
    }

}
