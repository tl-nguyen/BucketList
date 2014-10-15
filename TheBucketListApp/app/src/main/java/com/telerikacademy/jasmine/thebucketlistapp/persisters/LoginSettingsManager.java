package com.telerikacademy.jasmine.thebucketlistapp.persisters;

import android.content.SharedPreferences;

public class LoginSettingsManager {
    private static final String REMEMBER_ME = "remember_me";
    private static final String CURRENT_USER = "current_user";

    private static LoginSettingsManager instance;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private LoginSettingsManager () {
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.editor = this.sharedPreferences.edit();
    }

    public static LoginSettingsManager getInstance() {
        if (instance == null) {
            instance = new LoginSettingsManager();
        }
        return instance;
    }

    public void putSettings(boolean rememberMe, String currentUser) {

        editor.putBoolean(REMEMBER_ME, rememberMe);
        editor.putString(CURRENT_USER, currentUser);

        editor.commit();
    }

    public void resetSettings() {

        editor.putBoolean(REMEMBER_ME, false);
        editor.putString(CURRENT_USER, null);

        editor.commit();
    }

    public String getCurrentUser() {
        String currentUser = sharedPreferences.getString(CURRENT_USER, null);

        return currentUser;
    }

    public boolean getRememberMe() {
        boolean rememberMe = sharedPreferences.getBoolean(REMEMBER_ME, false);

        return rememberMe;
    }
}
