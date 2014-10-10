package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.activities.fragments.GoalsFragment;

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
            Intent goalsScreen = new Intent(NewGoalActivity.this, MainActivity.class);

            this.startActivity(goalsScreen);

        } else if (v.getId() == R.id.btnSave) {

        }
    }
}
