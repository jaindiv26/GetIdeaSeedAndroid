package com.dexa.getideaseed;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dev on 04/03/18.
 */

public class PrefManager {

    private static PrefManager instance;
    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefManager() {}

    public PrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences("GetIdeaSeedPref", Context.MODE_PRIVATE);
    }

    public static PrefManager getInstance() {
        if (instance == null) {
            instance = new PrefManager(GetIdeaSeedApplication.getContext());
        }
        return instance;
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key,0);
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key,false);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public SharedPreferences getPrefs() {
        return sharedPreferences;
    }



}
