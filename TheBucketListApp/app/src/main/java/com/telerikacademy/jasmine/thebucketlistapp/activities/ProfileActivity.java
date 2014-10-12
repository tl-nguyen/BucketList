package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.telerikacademy.jasmine.thebucketlistapp.R;

/**
 * Created by Boyko on 12.10.2014 г..
 */
public class ProfileActivity extends Activity {

    private Button mSaveProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mSaveProfileBtn = (Button) findViewById(R.id.btnSaveProfile);
    }


}
