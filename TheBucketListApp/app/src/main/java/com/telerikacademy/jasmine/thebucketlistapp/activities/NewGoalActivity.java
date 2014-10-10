package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.model.result.CreateResultItem;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
import com.telerikacademy.jasmine.thebucketlistapp.models.Idea;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;

import java.util.UUID;

public class NewGoalActivity extends Activity implements View.OnClickListener {

    private EditText mGoalTitle;
    private EditText mGoalDescription;
    private Button mCancelBtn;
    private Button mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        this.mGoalTitle = (EditText) findViewById(R.id.etGoalTitle);
        this.mGoalDescription = (EditText) findViewById(R.id.etGoalDescriptrion);
        this.mCancelBtn = (Button) findViewById(R.id.btnCancel);
        this.mSaveBtn = (Button) findViewById(R.id.btnSave);

        this.mCancelBtn.setOnClickListener(this);
        this.mSaveBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancel) {
            startGoalScreen();
        } else if (v.getId() == R.id.btnSave) {
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
    }

    private void startGoalScreen() {
        Intent goalsScreen = new Intent(NewGoalActivity.this, MainActivity.class);
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
