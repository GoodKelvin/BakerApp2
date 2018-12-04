package com.kelvingabe.bakerapp;

import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppService {
    private static final String TAG = "AppService";


    private static final String RECIPE_API_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static final OkHttpClient sOkHttpClient = new OkHttpClient();
    private static final Moshi sMoshi = new Moshi.Builder().build();
    private static final Type sJSONTypeToHandle = Types.newParameterizedType(List.class, Recipe.class, Step.class, Ingredient.class);


    private static final JsonAdapter<List<Recipe>> sRecipeJsonAdapter = sMoshi.adapter(sJSONTypeToHandle);


    public static String fetchRecipes() {
        String responseString = null;
        Request request = new Request.Builder().url(RECIPE_API_URL).build();
        Log.d(TAG, "URL: " + RECIPE_API_URL);
        Response response;
        try {
            response = sOkHttpClient.newCall(request).execute();
            if (response != null) {
                responseString = response.body() != null ? response.body().string() : null;
            }
        } catch (IOException ioEx) {
            Log.e(TAG, ioEx.getMessage());
        }

        return responseString;
    }


    public static List<Recipe> recipeStringToJSON(String recipeJSON) {
        List<Recipe> recipeList = null;
        try {
            recipeList = sRecipeJsonAdapter.fromJson(recipeJSON);
        } catch (IOException ioEx) {
            Log.e(TAG, ioEx.getMessage());
        }
        return recipeList;
    }

}
