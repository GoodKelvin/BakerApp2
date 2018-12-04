package com.kelvingabe.bakerapp;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class AppAsyncTask extends AsyncTask<Void, Void, List<Recipe>> {
    private static final String TAG = "AppAsyncTask";
    private RecipeLoadTaskComplete mRecipeLoadTaskComplete;


    public AppAsyncTask(RecipeLoadTaskComplete recipeLoadTaskComplete) {
        if (recipeLoadTaskComplete == null) {
            throw new IllegalArgumentException("Null object not allowed");
        }
        mRecipeLoadTaskComplete = recipeLoadTaskComplete;
    }

    @Override
    protected List<Recipe> doInBackground(Void... urls) {
        List<Recipe> recipeList = null;
        //TODO check is network is available
        //check if network is available
        if (BakeApp.isConnectionAvailable()) {
            String responseJson = AppService.fetchRecipes();

            //check if response string is not empty
            if (!TextUtils.isEmpty(responseJson)) {
                //convert string into json
                recipeList = AppService.recipeStringToJSON(responseJson);
            }
        } else {
            Log.d(TAG, "Network not available");
        }
        return recipeList;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipeList) {
        super.onPostExecute(recipeList);

        //notify interested object
        mRecipeLoadTaskComplete.recipesLoadCompleted(recipeList);
    }


    //interface to be implemented by classes that will be notified when task is completed
    public interface RecipeLoadTaskComplete {
        void recipesLoadCompleted(List<Recipe> recipeList);
    }
}
