package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.model.result.CreateResultItem;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
import com.telerikacademy.jasmine.thebucketlistapp.models.Idea;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.LoginSettingsManager;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;

import java.util.UUID;

public class NewGoalActivity extends Activity {

    private EditText mGoalTitle;
    private EditText mGoalDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        this.mGoalTitle = (EditText) findViewById(R.id.etGoalTitle);
        this.mGoalDescription = (EditText) findViewById(R.id.etGoalDescriptrion);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mGoalTitle.setText("");
        this.mGoalDescription.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startGoalScreen();
        } else if (id == R.id.action_cancel) {
            startGoalScreen();
        } else if (id == R.id.action_save) {
            saveGoal();
        } else if (id == R.id.action_logout) {
            LoginSettingsManager.getInstance().setSharedPreferences(getSharedPreferences(getString(R.string.sharedPreferencesName), 0));
            LoginSettingsManager.getInstance().resetSettings();
            RemoteDbManager.getInstance().logout();

            Intent loginScreen = new Intent(this, LoginActivity.class);
            startActivity(loginScreen);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean areValidInputs() {
        boolean areValid = true;

        if (this.mGoalTitle.getText().toString().equals("")) {
            areValid = false;
        }

        return areValid;
    }

    private void saveGoal() {

        if(!areValidInputs()) {

            processInvalidInputs();

            return;
        }

        Idea idea = new Idea(this.mGoalTitle.getText().toString(),
                this.mGoalDescription.getText().toString());

        RemoteDbManager.getInstance().createIdea(idea, new RequestResultCallbackAction() {

            @Override
            public void invoke(RequestResult requestResult) {
                if (requestResult.getSuccess()) {
                    CreateResultItem resultItem = (CreateResultItem) requestResult.getValue();

                    Goal goal = new Goal(mGoalTitle.getText().toString(),
                            mGoalDescription.getText().toString(),
                            UUID.fromString(resultItem.getServerId().toString()));

                    RemoteDbManager.getInstance().createGoal(goal, new RequestResultCallbackAction() {

                        @Override
                        public void invoke(RequestResult requestResult) {
                            if (requestResult.getSuccess()) {
                                startGoalScreen();
                            } else {
                                processError(requestResult);
                            }
                        }
                    });
                } else {
                    processError(requestResult);
                }
            }
        });
    }

    private void processInvalidInputs() {
        final String errorMessage = getString(R.string.goal_title_warning);

        NewGoalActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                showAlert(NewGoalActivity.this, errorMessage);
            }

        });
    }

    private void startGoalScreen() {
        Intent goalsScreen = new Intent(NewGoalActivity.this, MainActivity.class);
        goalsScreen.putExtra(getString(R.string.FRAGMENT), getResources().getInteger(R.integer.GOALS_FRAGMENT));
        this.startActivity(goalsScreen);
    }

    private void processError(RequestResult requestResult) {
        final String errorMessage = requestResult.getError().getMessage();

        NewGoalActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                showAlert(NewGoalActivity.this, errorMessage);
            }

        });
    }

    private void showAlert(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
