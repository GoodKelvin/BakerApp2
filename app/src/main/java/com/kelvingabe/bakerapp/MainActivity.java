package com.kelvingabe.bakerapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class MainActivity extends AppCompatActivity implements AppAsyncTask.AsyncTaskComplete, RecipesAdapter.RecipeSelectedListener {

    public static final String SELECTED_RECIPE = "selected_recipe";

    public String TAG = "MainActivity";

    @BindView(R.id.recipe_recycler)
    RecyclerView mRecipeRecyclerView;

    @BindView(R.id.recipe_loading_state)
    View mLoadingView;

    @BindView(R.id.recipe_empty_state)
    View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipes);
        ButterKnife.bind(this);
        boolean isSmallestWidth600 = getResources().getBoolean(R.bool.is_sw600);

        RecyclerView.LayoutManager mLayoutManager;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE || isSmallestWidth600) {
            mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        } else
        {
            mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        }
        RecyclerView.ItemDecoration recipeDecoration = new RecipeItemDecoration(this, R.dimen.item_offset);
        mRecipeRecyclerView.setLayoutManager(mLayoutManager);
        mRecipeRecyclerView.addItemDecoration(recipeDecoration);
        mRecipeRecyclerView.setHasFixedSize(true);
        showLoading();
        loadTask();

    }

    private void loadTask() {
        if (isOnline()) {
            new AppAsyncTask(this).execute();
        } else {
            showEmpty();
        }
    }

    private void showLoading() {
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void showEmpty() {
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showList() {
        mLoadingView.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void asyncCompleted(List<Recipe> recipeList) {
        Log.d(TAG, "" + recipeList);
        if (recipeList != null && recipeList.size() > 0) {
            Log.d(TAG, "got here");
            mRecipeRecyclerView.setAdapter(new RecipesAdapter(recipeList, this));
            showList();
        } else {
            showEmpty();
        }


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    @Override
    public void recipeSelected(Recipe selectedRecipe) {
        Intent recipeDetailIntent = new Intent(this, RecipeActivity.class);
        recipeDetailIntent.putExtra(SELECTED_RECIPE, selectedRecipe);
        startActivity(recipeDetailIntent);
    }
}
