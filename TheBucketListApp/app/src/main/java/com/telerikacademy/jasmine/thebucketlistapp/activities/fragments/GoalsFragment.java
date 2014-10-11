package com.telerikacademy.jasmine.thebucketlistapp.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.activities.MainActivity;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;
import com.telerikacademy.jasmine.thebucketlistapp.utils.GoalAdapter;

import java.util.ArrayList;

public class GoalsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ImageButton mCameraBtn;
    private ListView mGoalListView;
    private View mRootView;

    private GoalAdapter goalAdapter;

    public GoalAdapter getGoalAdapter() {
        return goalAdapter;
    }

    private void startCamera() {
        Intent camera = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(camera, 100);
    }

    private void initializeComponents () {
        mCameraBtn = (ImageButton) mRootView.findViewById(R.id.btnCamera);
        mGoalListView = (ListView) mRootView.findViewById(R.id.lvGoals);

        this.goalAdapter = new GoalAdapter(this.getActivity(), R.layout.fragment_list_row_goal, LoggedUser.getInstance().getGoals());

        mGoalListView.setAdapter(this.goalAdapter);

        this.loadGoals(mGoalListView, this.getActivity(), this);

        mGoalListView.setOnItemClickListener(this);

        mCameraBtn.setOnClickListener(this);
    }

    private void loadGoals(final ListView target, final Activity activity, final GoalsFragment goalsFragment) {
        RemoteDbManager.getInstance().retrieveGoals(new RequestResultCallbackAction<ArrayList<Goal>>() {

            @Override
            public void invoke(RequestResult<ArrayList<Goal>> requestResult) {

                if (requestResult.getSuccess()) {
                    LoggedUser.getInstance().getGoals().clear();
                    for (Goal goal : requestResult.getValue()) {
                        LoggedUser.getInstance().getGoals().add(goal);
                    }

                    target.post(new Runnable() {
                        @Override
                        public void run() {
                            goalsFragment.getGoalAdapter().notifyDataSetChanged();
                        }
                    });
                } else {
                    Toast.makeText(activity, "Cannot retrieve goals", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this.getActivity(), LoggedUser.getInstance().getGoals().get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }
}