package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.LoginSettingsManager;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;

public class ProfileActivity extends Activity {

    private TextView mDisplayName;
    private TextView mUserName;
    private TextView mEmail;
    private TextView mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        mDisplayName = (TextView) findViewById(R.id.tvGreeting);
        mUserName = (TextView) findViewById(R.id.tvUsername);
        mEmail = (TextView) findViewById(R.id.tvEmail);
        mProgress = (TextView) findViewById(R.id.tvProgress);

        String displayName = LoggedUser.getInstance().getLoggedUser().getDisplayName();
        String userName = LoggedUser.getInstance().getLoggedUser().getUsername();
        String email = LoggedUser.getInstance().getLoggedUser().getEmail() == null ? "" : LoggedUser.getInstance().getLoggedUser().getEmail();
        int allGoal = LoggedUser.getInstance().getGoals().size();
        int completedGoal = completedGoalsCount();

        mDisplayName.setText(String.format(getString(R.string.hello), displayName));
        mUserName.setText(String.format(getString(R.string.username), userName));
        mEmail.setText(String.format(getString(R.string.email), email));
        mProgress.setText(String.format("%s/%s goals", completedGoal, allGoal));
    }

    private int completedGoalsCount() {
        int count = 0;

        for (Goal goal : LoggedUser.getInstance().getGoals()) {
            if (goal.isDone()) {
                count++;
            }
        }

        return count;
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
