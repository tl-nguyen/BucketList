package com.telerikacademy.jasmine.thebucketlistapp.persisters;

import android.content.SharedPreferences;

public class LoginSettingsManager {
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

        editor.putBoolean("remember_me", rememberMe);
        editor.putString("current_user", currentUser);

        editor.commit();
    }

    public void resetSettings() {

        editor.putBoolean("remember_me", false);
        editor.putString("current_user", null);

        editor.commit();
    }

    public String getCurrentUser() {
        String currentUser = sharedPreferences.getString("current_user", null);

        return currentUser;
    }

    public boolean getRememberMe() {
        boolean rememberMe = sharedPreferences.getBoolean("remember_me", false);

        return rememberMe;
    }
}
