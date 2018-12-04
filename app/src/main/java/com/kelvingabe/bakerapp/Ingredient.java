package com.kelvingabe.bakerapp;


import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;


public class Ingredient implements Parcelable {

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    @Json(name = "quantity")
    public final double quantity;
    @Json(name = "measure")
    public final String measure;
    @Json(name = "ingredient")
    public final String name;

    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return quantity + " " + measure + "(s) of " + name;
    }
}
