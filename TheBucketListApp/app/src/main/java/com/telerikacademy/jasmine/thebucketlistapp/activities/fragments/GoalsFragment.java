package com.telerikacademy.jasmine.thebucketlistapp.activities.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.activities.MainActivity;
import com.telerikacademy.jasmine.thebucketlistapp.activities.NewGoalActivity;

public class GoalsFragment extends Fragment implements View.OnClickListener{

    private ImageButton mCameraBtn;
    private View mRootView;

    private void startCamera() {
        Intent camera = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(camera, 100);
    }

    private void initializeComponents () {
        mCameraBtn = (ImageButton) mRootView.findViewById(R.id.btnCamera);

        mCameraBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.mRootView = inflater.inflate(R.layout.fragment_goals, container, false);

        initializeComponents();

        return mRootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCamera) {
            startCamera();
        }
    }
}