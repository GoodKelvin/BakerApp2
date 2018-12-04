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

    protected Step(Parcel in) {
        id = in.readLong();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
