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
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;

public class GoalDetailActivity extends Activity {

    private Goal mGoal;
    private TextView mGoalTitle;
    private TextView mGoalDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_detail);

        int position = getIntent().getIntExtra(getString(R.string.GOAL_POSITION), -1);

        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        if (position == -1) {
            //TODO: something is wrong, go back to main activity
        } else {
            mGoal = LoggedUser.getInstance().getGoals().get(position);
        }

        this.mGoalTitle = (TextView) findViewById(R.id.goalDetailTitle);
        this.mGoalDescription = (TextView) findViewById(R.id.goalDetailDescription);

        this.mGoalTitle.setText(mGoal.getTitle());
        this.mGoalDescription.setText(mGoal.getDescription());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.goal_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startGoalScreen();
        } else if (id == R.id.action_logout) {
            RemoteDbManager.getInstance().logout();

            Intent loginScreen = new Intent(this, LoginActivity.class);
            startActivity(loginScreen);
        }

        return super.onOptionsItemSelected(item);
    }

    private void startGoalScreen() {
        Intent goalsScreen = new Intent(this, MainActivity.class);
        goalsScreen.putExtra(getString(R.string.FRAGMENT), getResources().getInteger(R.integer.GOALS_FRAGMENT));
        this.startActivity(goalsScreen);
    }
}
