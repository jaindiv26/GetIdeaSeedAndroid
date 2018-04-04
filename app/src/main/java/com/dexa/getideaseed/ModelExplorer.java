package com.dexa.getideaseed;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Dev on 10/02/18.
 */

public class ModelExplorer implements Parcelable {

    private String uniqueId;
    private String title;
    private String description;
    private String userName;
    private String userID;
    private ArrayList<String> isLightbulbedBy;
    private Boolean isPrivate,isPublic;
    private int lightbulbs,orginality,progress,difficulty;
    private boolean hasLiked;

    public ModelExplorer() {
    }


    protected ModelExplorer(Parcel in) {
        uniqueId = in.readString();
        title = in.readString();
        description = in.readString();
        userName = in.readString();
        userID = in.readString();
        isLightbulbedBy = in.createStringArrayList();
        byte tmpIsPrivate = in.readByte();
        isPrivate = tmpIsPrivate == 0 ? null : tmpIsPrivate == 1;
        byte tmpIsPublic = in.readByte();
        isPublic = tmpIsPublic == 0 ? null : tmpIsPublic == 1;
        lightbulbs = in.readInt();
        orginality = in.readInt();
        progress = in.readInt();
        difficulty = in.readInt();
    }

    public static final Creator<ModelExplorer> CREATOR = new Creator<ModelExplorer>() {
        @Override
        public ModelExplorer createFromParcel(Parcel in) {
            return new ModelExplorer(in);
        }

        @Override
        public ModelExplorer[] newArray(int size) {
            return new ModelExplorer[size];
        }
    };

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getIsLightbulbedBy() {
        return isLightbulbedBy;
    }

    public void setIsLightbulbedBy(ArrayList<String> isLightbulbedBy) {
        this.isLightbulbedBy = isLightbulbedBy;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public int getLightbulbs() {
        return lightbulbs;
    }

    public void setLightbulbs(int lightbulbs) {
        this.lightbulbs = lightbulbs;
    }

    public int getOrginality() {
        return orginality;
    }

    public void setOrginality(int orginality) {
        this.orginality = orginality;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uniqueId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(userName);
        dest.writeString(userID);
        dest.writeStringList(isLightbulbedBy);
        dest.writeByte((byte) (isPrivate == null ? 0 : isPrivate ? 1 : 2));
        dest.writeByte((byte) (isPublic == null ? 0 : isPublic ? 1 : 2));
        dest.writeInt(lightbulbs);
        dest.writeInt(orginality);
        dest.writeInt(progress);
        dest.writeInt(difficulty);
    }
}
