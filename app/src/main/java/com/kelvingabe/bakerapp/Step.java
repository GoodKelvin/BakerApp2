package com.kelvingabe.bakerapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;


public class Step implements Parcelable {
    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
    @Json(name = "id")
    public final long id;
    @Json(name = "shortDescription")
    public final String shortDescription;
    @Json(name = "description")
    public final String description;
    @Json(name = "videoURL")
    public final String videoURL;
    @Json(name = "thumbnailURL")
    public final String thumbnailURL;

    protected Step(Parcel inParcel) {
        id = inParcel.readLong();
        shortDescription = inParcel.readString();
        description = inParcel.readString();
        videoURL = inParcel.readString();
        thumbnailURL = inParcel.readString();
    }

    @Override
    public void writeToParcel(Parcel destinationParcel, int flags) {
        destinationParcel.writeLong(id);
        destinationParcel.writeString(shortDescription);
        destinationParcel.writeString(description);
        destinationParcel.writeString(videoURL);
        destinationParcel.writeString(thumbnailURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
