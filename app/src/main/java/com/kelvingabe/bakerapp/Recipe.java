package com.kelvingabe.bakerapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.List;


public class Recipe implements Parcelable {

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    @Json(name = "id")
    public final long id;
    @Json(name = "name")
    public final String name;
    @Json(name = "ingredients")
    public final List<Ingredient> ingredients;
    @Json(name = "steps")
    public final List<Step> steps;
    @Json(name = "servings")
    public final String servings;
    @Json(name = "image")
    public final String imageUrl;

    protected Recipe(Parcel inParcel) {
        id = inParcel.readLong();
        name = inParcel.readString();
        ingredients = inParcel.createTypedArrayList(Ingredient.CREATOR);
        steps = inParcel.createTypedArrayList(Step.CREATOR);
        servings = inParcel.readString();
        imageUrl = inParcel.readString();
    }

    @Override
    public void writeToParcel(Parcel destinationParcel, int flags) {
        destinationParcel.writeLong(id);
        destinationParcel.writeString(name);
        destinationParcel.writeTypedList(ingredients);
        destinationParcel.writeTypedList(steps);
        destinationParcel.writeString(servings);
        destinationParcel.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
