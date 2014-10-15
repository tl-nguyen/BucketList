package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.LoginSettingsManager;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startGoalScreen();
        } else if (id == R.id.action_editProfile) {
            return true;
        } else if (id == R.id.action_logout) {
            LoginSettingsManager.getInstance().setSharedPreferences(getSharedPreferences(getString(R.string.sharedPreferencesName), 0));
            LoginSettingsManager.getInstance().resetSettings();
            RemoteDbManager.getInstance().logout();

            Intent loginScreen = new Intent(this, LoginActivity.class);
            startActivity(loginScreen);
        }
        return super.onOptionsItemSelected(item);
    }

    private void startGoalScreen() {
        Intent goalsScreen = new Intent(ProfileActivity.this, MainActivity.class);
        goalsScreen.putExtra(getString(R.string.FRAGMENT), getResources().getInteger(R.integer.GOALS_FRAGMENT));
        this.startActivity(goalsScreen);
    }
}
