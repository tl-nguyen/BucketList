package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;

public class GoalDetailActivity extends Activity {

    private Goal mGoal;
    private Menu mMenu;
    private TextView mGoalTitle;
    private TextView mGoalDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_detail);

        int position = getIntent().getIntExtra(getString(R.string.GOAL_POSITION), -1);

        if (position == -1) {
            startGoalScreen();
        } else {
            mGoal = LoggedUser.getInstance().getGoals().get(position);
        }

        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
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
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.goal_detail, menu);

        if (mGoal.isDone()) {
            menu.findItem(R.id.set_as_completed).setVisible(false);
            menu.findItem(R.id.take_a_shot).setVisible(false);
        } else {
            menu.findItem(R.id.brag).setVisible(false);
        }

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
        } else if (id == R.id.set_as_completed) {
            this.mGoal.setDone(true);
            RemoteDbManager.getInstance().updateGoal(mGoal, new RequestResultCallbackAction() {
                @Override
                public void invoke(final RequestResult requestResult) {
                    if (requestResult.getSuccess()) {
                        GoalDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMenu.findItem(R.id.set_as_completed).setVisible(false);
                                mMenu.findItem(R.id.take_a_shot).setVisible(false);
                                mMenu.findItem(R.id.brag).setVisible(true);
                                showAlert(GoalDetailActivity.this, String.format(getString(R.string.goal_completed), mGoal.getTitle()));
                            }
                        });
                    }
                }
            });
        } else if (id == R.id.brag) {
            //TODO: brag about this archivement
        }

        return super.onOptionsItemSelected(item);
    }

    private void startGoalScreen() {
        Intent goalsScreen = new Intent(GoalDetailActivity.this, MainActivity.class);
        goalsScreen.putExtra(getString(R.string.FRAGMENT), getResources().getInteger(R.integer.GOALS_FRAGMENT));
        this.startActivity(goalsScreen);
    }

    private void showAlert(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
