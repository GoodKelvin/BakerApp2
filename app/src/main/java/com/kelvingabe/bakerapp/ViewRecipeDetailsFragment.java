package com.kelvingabe.bakerapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelvingabe.bakerapp.adapter.StepAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kelvingabe.bakerapp.ViewRecipeDetailsActivity.RECIPE_TO_DISPLAY;

//fragment to display a recipe's details
// uses Butter knife  from Jake Wharton (http://jakewharton.github.io/butterknife/) to bind views
public class ViewRecipeDetailsFragment extends Fragment implements StepAdapter.StepSelectedListener {

    private static final String TAG = "RecipeDetailsFragment";
    @BindView(R.id.recipe_ingredients_text)
    TextView mIngredientTextView;
    @BindView(R.id.recipe_steps_recycler)
    RecyclerView mStepsRecyclerView;
    private Recipe mRecipeToDisplay;
    private OnStepInteractionListener onStepInteractionListener;
    private Context mContext;

    public ViewRecipeDetailsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            if (getArguments() != null && getArguments().getParcelable(RECIPE_TO_DISPLAY) != null) {
                mRecipeToDisplay = getArguments().getParcelable(RECIPE_TO_DISPLAY);

            }
        } else {
            mRecipeToDisplay = savedInstanceState.getParcelable(RECIPE_TO_DISPLAY);
        }
        Log.d(TAG, "Created " + mRecipeToDisplay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, view);

        if (mRecipeToDisplay != null && mRecipeToDisplay.ingredients != null && mRecipeToDisplay.ingredients.size() > 0) {
            mIngredientTextView.setText(Appender.ingredientsToNewLineString(mRecipeToDisplay.ingredients));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        StepAdapter stepAdapter = new StepAdapter(mRecipeToDisplay.steps, this);
        DividerItemDecoration lineItemDecoration = new DividerItemDecoration(
                mStepsRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mStepsRecyclerView.setLayoutManager(linearLayoutManager);
        mStepsRecyclerView.addItemDecoration(lineItemDecoration);
        mStepsRecyclerView.setAdapter(stepAdapter);
        mStepsRecyclerView.setFocusable(false);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_TO_DISPLAY, mRecipeToDisplay);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepInteractionListener) {
            onStepInteractionListener = (OnStepInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepSelectedFragmentListener");
        }
        mContext = context;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        onStepInteractionListener = null;
    }

    @Override
    public void stepSelected(Step step, int index) {
        onStepInteractionListener.onStepInteraction(index);
    }

    public interface OnStepInteractionListener {
        void onStepInteraction(int stepIndex);
    }
}
