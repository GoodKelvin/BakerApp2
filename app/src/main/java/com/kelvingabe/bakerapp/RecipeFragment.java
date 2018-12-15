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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kelvingabe.bakerapp.RecipeActivity.RECIPE_TO_DISPLAY;

public class RecipeFragment extends Fragment implements StepAdapter.StepSelectedListener {

    private static final String TAG = "RecipeFragment";
    @BindView(R.id.recipe_ingredients_text)
    TextView ingredientTextView;
    @BindView(R.id.recipe_steps_recycler)
    RecyclerView stepsRecyclerView;
    private Recipe recipe;
    private OnStepInteractionListener onStepInteractionListener;
    private Context context;

    public RecipeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            if (getArguments() != null && getArguments().getParcelable(RECIPE_TO_DISPLAY) != null) {
                recipe = getArguments().getParcelable(RECIPE_TO_DISPLAY);

            }
        } else {
            recipe = savedInstanceState.getParcelable(RECIPE_TO_DISPLAY);
        }
        Log.d(TAG, "Created " + recipe);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);

        if (recipe != null && recipe.ingredients != null && recipe.ingredients.size() > 0) {
            ingredientTextView.setText(ingredientToNewLine(recipe.ingredients));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        StepAdapter stepAdapter = new StepAdapter(recipe.steps, this);
        DividerItemDecoration lineItemDecoration = new DividerItemDecoration(
                stepsRecyclerView.getContext(), linearLayoutManager.getOrientation());
        stepsRecyclerView.setLayoutManager(linearLayoutManager);
        stepsRecyclerView.addItemDecoration(lineItemDecoration);
        stepsRecyclerView.setAdapter(stepAdapter);
        stepsRecyclerView.setFocusable(false);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_TO_DISPLAY, recipe);
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
        this.context = context;

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

    public String ingredientToNewLine(List<Ingredient> ingredientList) {
        StringBuilder ingredientsStringBuilder = new StringBuilder("");
        for (Ingredient ingredient : ingredientList) {

            ingredientsStringBuilder.append(ingredient.toString()).append(".\n\n");
        }
        return ingredientsStringBuilder.toString();
    }
}
