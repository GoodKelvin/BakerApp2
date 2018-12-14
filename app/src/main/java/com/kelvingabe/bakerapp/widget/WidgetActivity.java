package com.kelvingabe.bakerapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RemoteViews;

import com.kelvingabe.bakerapp.AppAsyncTask;
import com.kelvingabe.bakerapp.Ingredient;
import com.kelvingabe.bakerapp.R;
import com.kelvingabe.bakerapp.Recipe;
import com.kelvingabe.bakerapp.adapter.WidgetAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static com.kelvingabe.bakerapp.MainActivity.SELECTED_RECIPE;

public class WidgetActivity extends AppCompatActivity implements AppAsyncTask.AsyncTaskComplete, WidgetAdapter.WidgetRecipeListener {

    @BindView(R.id.widget_select_recipe)
    RecyclerView mRecipeRecyclerView;

    @BindView(R.id.recipe_loading_state)
    View mLoadingState;

    @BindView(R.id.recipe_empty_state)
    View mEmptyState;

    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_widget_config);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mAppWidgetId = intent.getExtras().getInt(EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration lineItemDecoration = new DividerItemDecoration(
                mRecipeRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecipeRecyclerView.setLayoutManager(linearLayoutManager);
        mRecipeRecyclerView.addItemDecoration(lineItemDecoration);
        showLoading();
        new AppAsyncTask(this).execute();

    }

    @Override
    public void asyncCompleted(List<Recipe> recipeList) {
        if (recipeList != null && recipeList.size() > 0) {
            WidgetAdapter recipeWidgetAdapter = new WidgetAdapter(recipeList, this);
            mRecipeRecyclerView.setAdapter(recipeWidgetAdapter);
            showList();
        } else {
            showEmpty();
        }

    }

    @Override
    public void recipeSelected(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.baker_app_widget);
        Intent intent = new Intent(this, WidgetService.class);

        List<String> ingredientStringList = new ArrayList<>();
        for (Ingredient ingredient : recipe.ingredients) {
            ingredientStringList.add(ingredient.name);
        }
        intent.putStringArrayListExtra(SELECTED_RECIPE, (ArrayList<String>) ingredientStringList);
        views.setTextViewText(R.id.widget_title_recipe_name, recipe.name);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
        Intent resultIntent = new Intent();

        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void showLoading() {
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyState.setVisibility(View.INVISIBLE);
        mLoadingState.setVisibility(View.VISIBLE);
    }

    private void showEmpty() {

        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingState.setVisibility(View.INVISIBLE);
        mEmptyState.setVisibility(View.VISIBLE);
    }

    private void showList() {
        mLoadingState.setVisibility(View.INVISIBLE);
        mEmptyState.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);

    }
}
