package com.kelvingabe.bakerapp;

import java.util.List;

public class Appender {
    public static String ingredientsToNewLineString(List<Ingredient> ingredientList) {
        StringBuilder ingredientsStringBuilder = new StringBuilder("");
        //Loop thr
        for (Ingredient ingredient : ingredientList) {

            ingredientsStringBuilder.append(ingredient.toString()).append(".\n\n");
        }
        return ingredientsStringBuilder.toString();
    }
}
