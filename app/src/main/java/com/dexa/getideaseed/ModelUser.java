package com.dexa.getideaseed;

/**
 * Created by Dev on 03/03/18.
 */

public class ModelUser {
    private int __v;
    private String userId,userSalt,userHash,userName,email,userRegisteredDate;
    private Boolean userActive,isEarlyAdopter;

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRegisteredDate() {
        return userRegisteredDate;
    }

    public void setUserRegisteredDate(String userRegisteredDate) {
        this.userRegisteredDate = userRegisteredDate;
    }

    public Boolean getUserActive() {
        return userActive;
    }

    public void setUserActive(Boolean userActive) {
        this.userActive = userActive;
    }

    public Boolean getEarlyAdopter() {
        return isEarlyAdopter;
    }

    public void setEarlyAdopter(Boolean earlyAdopter) {
        isEarlyAdopter = earlyAdopter;
    }
}
