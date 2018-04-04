package com.dexa.getideaseed;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dev on 22/03/18.
 */

public class ModelSearch implements Parcelable {
    private String difficulty,lightbulbs,originality,progress,term,username;

    protected ModelSearch(Parcel in) {
        difficulty = in.readString();
        lightbulbs = in.readString();
        originality = in.readString();
        progress = in.readString();
        term = in.readString();
        username = in.readString();
    }

    public static final Creator<ModelSearch> CREATOR = new Creator<ModelSearch>() {
        @Override
        public ModelSearch createFromParcel(Parcel in) {
            return new ModelSearch(in);
        }

        @Override
        public ModelSearch[] newArray(int size) {
            return new ModelSearch[size];
        }
    };

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLightbulbs() {
        return lightbulbs;
    }

    public void setLightbulbs(String lightbulbs) {
        this.lightbulbs = lightbulbs;
    }

    public String getOriginality() {
        return originality;
    }

    public void setOriginality(String originality) {
        this.originality = originality;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(difficulty);
        dest.writeString(lightbulbs);
        dest.writeString(originality);
        dest.writeString(progress);
        dest.writeString(term);
        dest.writeString(username);
    }
}
