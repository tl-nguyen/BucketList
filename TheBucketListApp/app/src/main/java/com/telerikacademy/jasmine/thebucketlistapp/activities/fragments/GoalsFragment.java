package com.telerikacademy.jasmine.thebucketlistapp.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.activities.GoalDetailActivity;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;
import com.telerikacademy.jasmine.thebucketlistapp.utils.GoalAdapter;

import java.util.ArrayList;

public class GoalsFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{

    private ListView mGoalListView;
    private View mRootView;
    private Menu mMenu;

    private GoalAdapter goalAdapter;

    public GoalAdapter getGoalAdapter() {
        return goalAdapter;
    }

    private void initializeComponents () {
        this.mGoalListView = (ListView) mRootView.findViewById(R.id.lvGoals);

        this.goalAdapter = new GoalAdapter(this.getActivity(), R.layout.fragment_list_row_goal, LoggedUser.getInstance().getGoals());

        this.mGoalListView.setAdapter(this.goalAdapter);

        this.loadGoals(mGoalListView, this.getActivity(), this);

        this.mGoalListView.setOnItemClickListener(this);
        this.mGoalListView.setOnItemLongClickListener(this);
    }

    private void loadGoals(final ListView listView, final Activity activity, final GoalsFragment goalsFragment) {
        RemoteDbManager.getInstance().retrieveGoals(new RequestResultCallbackAction<ArrayList<Goal>>() {

            @Override
            public void invoke(final RequestResult<ArrayList<Goal>> requestResult) {
                if (requestResult.getSuccess()) {
                    try {
                        LoggedUser.getInstance().getGoals().clear();
                        for (Goal goal : requestResult.getValue()) {
                            LoggedUser.getInstance().getGoals().add(goal);
                        }
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                goalsFragment.getGoalAdapter().notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        Log.d("BucketList", "something is really wrong with the post");
                    }
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, requestResult.getError().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        this.mRootView = inflater.inflate(R.layout.fragment_goals, container, false);

        initializeComponents();

        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (areGoalSelected()) {
            menu.findItem(R.id.action_delete).setVisible(true);
        } else {
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        this.mMenu = menu;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent goalDetail = new Intent(mRootView.getContext(), GoalDetailActivity.class);
        goalDetail.putExtra(getString(R.string.GOAL_POSITION), position);
        this.startActivity(goalDetail);
    }

    private boolean areGoalSelected() {
        for (Goal goal : LoggedUser.getInstance().getGoals()) {
            if (goal.isSelected()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {

            RemoteDbManager.getInstance().deleteGoals(new RequestResultCallbackAction() {
                @Override
                public void invoke(RequestResult requestResult) {
                    if (requestResult.getSuccess()) {

                        mGoalListView.post(new Runnable() {
                            @Override
                            public void run() {
                                mMenu.findItem(R.id.action_delete).setVisible(false);
                                GoalsFragment.this.getGoalAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        boolean isSelected = LoggedUser.getInstance().getGoals().get(position).isSelected();
        LoggedUser.getInstance().getGoals().get(position).setSelected(!isSelected);

        if (!isSelected) {
            view.setBackgroundColor(this.getActivity().getResources().getColor(R.color.bbutton_primary));
        } else {
            view.setBackgroundColor(Color.WHITE);
        }

        if (areGoalSelected()) {
            this.mMenu.findItem(R.id.action_delete).setVisible(true);
        } else {
            this.mMenu.findItem(R.id.action_delete).setVisible(false);
        }
        return true;
    }
}