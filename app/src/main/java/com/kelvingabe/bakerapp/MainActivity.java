package com.kelvingabe.bakerapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.kelvingabe.bakerapp.adapter.RecipeItemDecoration;
import com.kelvingabe.bakerapp.adapter.RecipesAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAsyncTask.RecipeLoadTaskComplete, RecipesAdapter.RecipeSelectedListener {

    public static final String SELECTED_RECIPE_INGREDIENTS = "selected_recipe";

    @BindView(R.id.recipe_recycler)
    RecyclerView mRecipeRecyclerView;

    @BindView(R.id.recipe_loading_state)
    View mLoadingState;

    @BindView(R.id.recipe_empty_state)
    View mEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipes);
        ButterKnife.bind(this);
        boolean isSmallestWidth600 = getResources().getBoolean(R.bool.is_sw600);

        //if orientation is landscape or smallest width is 600dp use 3 columns
        RecyclerView.LayoutManager mLayoutManager;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE || isSmallestWidth600) {
            mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        } else// use 2 columns
        {
            mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        RecyclerView.ItemDecoration recipeDecoration = new RecipeItemDecoration(this, R.dimen.item_offset);
        mRecipeRecyclerView.setLayoutManager(mLayoutManager);
        mRecipeRecyclerView.addItemDecoration(recipeDecoration);
        mRecipeRecyclerView.setHasFixedSize(true);
        showLoadingState();
        new RecipeAsyncTask(this).execute();

    }

    private void showLoadingState() {
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyState.setVisibility(View.INVISIBLE);
        mLoadingState.setVisibility(View.VISIBLE);
    }

    private void showEmptyState() {
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingState.setVisibility(View.INVISIBLE);
        mEmptyState.setVisibility(View.VISIBLE);
    }

    private void showList() {
        mLoadingState.setVisibility(View.INVISIBLE);
        mEmptyState.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void recipesLoadCompleted(List<Recipe> recipeList) {
        Log.d("XXX", "" + recipeList);
        if (recipeList != null && recipeList.size() > 0) {
            mRecipeRecyclerView.setAdapter(new RecipesAdapter(recipeList, this));
            showList();
        } else {
            showEmptyState();
        }


    }


    @Override
    public void recipeSelected(Recipe selectedRecipe) {
        Intent recipeDetailIntent = new Intent(this, ViewRecipeDetailsActivity.class);
        //pass recipe to activity
        recipeDetailIntent.putExtra(SELECTED_RECIPE_INGREDIENTS, selectedRecipe);
        startActivity(recipeDetailIntent);
    }
}
