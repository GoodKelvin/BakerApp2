package com.kelvingabe.bakerapp;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.List;

public class AppAsyncTask extends AsyncTask<Void, Void, List<Recipe>> {
    private AsyncTaskComplete mAsyncTaskComplete;


    public AppAsyncTask(AsyncTaskComplete asyncTaskComplete) {
        if (asyncTaskComplete == null) {
            throw new IllegalArgumentException("Null object not allowed");
        }
        mAsyncTaskComplete = asyncTaskComplete;
    }

    @Override
    protected List<Recipe> doInBackground(Void... urls) {
        List<Recipe> recipeList = null;
        String responseJson = AppService.fetchRecipes();

        if (!TextUtils.isEmpty(responseJson)) {
            recipeList = AppService.recipeStringToJSON(responseJson);
        }

        return recipeList;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipeList) {
        super.onPostExecute(recipeList);
        mAsyncTaskComplete.asyncCompleted(recipeList);
    }


    public interface AsyncTaskComplete {
        void asyncCompleted(List<Recipe> recipeList);
    }

}
